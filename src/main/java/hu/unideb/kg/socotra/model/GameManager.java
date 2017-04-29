/*
 * Copyright (C) 2016 Gergely Kadar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package hu.unideb.kg.socotra.model;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class GameManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

    /**
     * The possible actions a player can perform during his/her turn.
     */
    public enum TurnAction {
        /**
         * The player played one or more words.
         */
        PLAY,
        /**
         * The player swapped letters.
         */
        SWAP,
        /**
         * The player passed his/her turn.
         */
        PASS
    }

    public class PlayedWord {

        private int turn;
        private String playerName;
        private String word;
        private int score;

        public PlayedWord(int turn, String playerName, String word, int score) {
            this.turn = turn;
            this.playerName = playerName;
            this.word = word;
            this.score = score;
        }

        public int getTurn() {
            return turn;
        }

        public void setTurn(int turn) {
            this.turn = turn;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

    }

    public class ThinkingTimeCountdownTimer extends TimerTask {

        AtomicInteger remainingSeconds = new AtomicInteger(maxThinkingTime);
        boolean timeExtended = false;

        @Override
        public void run() {
            int currentValue = remainingSeconds.decrementAndGet();
            observers.stream().forEach(o -> o.updateTimer(currentValue));
            if (currentValue == 0) {
                thinkingTimeOver();
            }
        }

        public void extendTime() {
            if (!timeExtended) {
                timeExtended = true;
                remainingSeconds.addAndGet(extensionsLength);
            }
        }

        public void reset() {
            remainingSeconds.set(maxThinkingTime);
        }
    }

    private Players players;
    private boolean boardLegal;
    private boolean movableTiles;
    private boolean wordsCorrect;
    private int turn;
    private int numOfPasses;
    private List<PlayedWord> playedWords;

    private int maxThinkingTime;
    private int maxExtensions;
    private int extensionsLength;
    private ThinkingTimeCountdownTimer thinkingTimeCountdownTimer;

    private static final String REDRAW = "<csere>";
    private static final String PASS = "<passz>";

    private GameBoard gameBoard;
    private Bag bag;
    private long bagSeed;
    private Dictionary dictionary;

    private List<GameObserver> observers;

    public GameManager(InputStreamReader inputStream, Players players)
            throws FileNotFoundException {
        this.gameBoard = new GameBoard();
        this.observers = new ArrayList<>();
        this.players = players;
        this.bag = new Bag();
        this.dictionary = new Dictionary(inputStream);
        this.playedWords = new ArrayList<>();
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void addObservers(List<GameObserver> observers) {
        this.observers = observers;
    }

    public void gameWindowReady() {
        startGame(bagSeed);
    }

    private void startGame(long bagSeed) {
        boardLegal = false;
        movableTiles = false;
        wordsCorrect = false;
        turn = 1;
        numOfPasses = 0;

        this.bagSeed = bagSeed;
        bag.shuffle(bagSeed);

        for (int i = 0; i < Tray.TRAY_SIZE; i++) {
            for (Player player : players.getPlayersList()) {
                player.drawTile(bag);
            }
        }

        observers.forEach(o -> o.gameStarted(players.getCurrentPlayer()));

        if (thinkingTimeLimitExists()) {
            new Timer().scheduleAtFixedRate(thinkingTimeCountdownTimer, 0, 1000);
        }
    }

    public boolean alterBoard(int row, int col, Player player) {
        boolean boardAltered = false;
        if (players.getCurrentPlayer() == player) {
            if (player.getTileInHand() == null) {
                Tile pickedUpTile = gameBoard.pickUpTile(row, col);
                if (pickedUpTile != null) {
                    if (pickedUpTile.isJoker()) {
                        pickedUpTile.resetJokerLetter();
                    }
                    player.setTileInHand(pickedUpTile);
                    boardAltered = true;
                }
            } else {
                if (player.getTileInHand().isJoker() && !player.getTileInHand().jokerLetterSpecified()) {
                    for (GameObserver obs : observers) {
                        String letter = obs.jokerLetterRequested(player);
                        if (letter != null) {
                            player.getTileInHand().setJokerLetter(letter);
                            observers.forEach(o -> o.jokerLetterChosen(letter, player));
                            break;
                        }
                    }
                }
                if (gameBoard.setTile(row, col, player.getTileInHand())) {
                    player.setTileInHand(null);
                    boardAltered = true;
                }
            }

            boardLegal = gameBoard.isLegal(gameBoard.isEmpty());
            LOGGER.trace("boardLegal: " + String.valueOf(boardLegal));
            if (boardLegal) {
                int points = gameBoard.calcPoints();
                List<String> wordsCurrentTurn = gameBoard.getPlayedWords();
                StringJoiner sj = new StringJoiner(", ");
                wordsCurrentTurn.stream().forEach(w -> sj.add(w));
                if (!playedWords.isEmpty()) {
                    PlayedWord lastPlayedWord = playedWords.get(playedWords.size() - 1);
                    if (lastPlayedWord.getTurn() == turn) {
                        lastPlayedWord.setScore(points);
                        lastPlayedWord.setWord(sj.toString());
                    } else {
                        playedWords.add(new PlayedWord(turn, player.getName(), sj.toString(), points));
                    }
                } else {
                    playedWords.add(new PlayedWord(turn, player.getName(), sj.toString(), points));
                }
                wordsCorrect = gameBoard.wordsAreCorrect(dictionary);
            } else if (!playedWords.isEmpty()) {
                PlayedWord lastPlayedWord = playedWords.get(playedWords.size() - 1);
                if (lastPlayedWord.getTurn() == turn) {
                    playedWords.remove(playedWords.size() - 1);
                }
            }
        }
        if (boardAltered) {
            observers.forEach(o -> o.boardAltered(row, col, player));
        }
        return boardAltered;
    }

    public boolean alterTray(int index, Player player) {
        boolean trayAltered = player.alterTray(index);
        if (trayAltered) {
            observers.forEach(o -> o.trayAltered(index, player));
        }
        return trayAltered;
    }

    public boolean endTurn(TurnAction action, Player player) {
        boolean turnEnded = false;
        if (players.getCurrentPlayer() == player) {
            switch (action) {
                case PLAY:
                    if (gameBoard.isLegal(gameBoard.isEmpty()) && player.getTileInHand() == null && gameBoard.wordsAreCorrect(dictionary)) {
                        numOfPasses = 0;
                        int points = gameBoard.calcPoints();
                        player.setScore(player.getScore() + points);
                        player.drawTiles(bag);
                        gameBoard.finalizeFields();
                        turnEnded = true;
                    }
                    break;
                case SWAP:
                    if (gameBoard.getMovableTiles() == 0 && player.getTileInHand() == null && bag.getNumOfTiles() > 0) {
                        numOfPasses = 0;
                        playedWords.add(new PlayedWord(turn, player.getName(), REDRAW, 0));
                        player.redrawTiles(bag);
                        bag.shuffle(bagSeed);
                        turnEnded = true;
                    }
                    break;
                case PASS:
                    if (gameBoard.getMovableTiles() > 0 || player.getTileInHand() != null) {
                        returnTiles(player);
                    }
                    numOfPasses++;
                    if (gameEnded()) {
                        // TODO handle game ended
                    }
                    playedWords.add(new PlayedWord(turn, player.getName(), PASS, 0));
                    turnEnded = true;

                    break;
                default:
                    break;
            }
        }
        if (turnEnded) {
            turn++;
            players.next();
            if (thinkingTimeLimitExists()) {
                thinkingTimeCountdownTimer.reset();
            }
            boardLegal = false;
            observers.forEach(o -> o.turnEnded(action, player));
        }
        return turnEnded;
    }

    public void extendThinkingTime(Player player) {
        if (thinkingTimeLimitExists() && playerCanExtendTime(player)) {
            thinkingTimeCountdownTimer.extendTime();
            player.timeExtended();
            observers.forEach(o -> o.thinkingTimeExtended(player));
        }
    }

    private void thinkingTimeOver() {
        observers.forEach(o -> o.thinkingTimeOver(players.getCurrentPlayer()));
    }

    private void returnTiles(Player player) {
        List<Tile> tilesToReturn = new ArrayList<>();
        tilesToReturn.addAll(gameBoard.pickUpMovableTiles());
        tilesToReturn.add(player.getTileInHand());
        player.setTileInHand(null);
        player.addTilesToTray(tilesToReturn);
    }

    public void localPlayerLeft(Player player) {
        observers.forEach(o -> o.localPlayerLeft(player));
    }

    public void remotePlayerLeft(Player player) {
        players.remove(player);
        observers.forEach(o -> o.remotePlayerLeft(player));
    }

    public void serverLeft() {
        observers.forEach(o -> o.serverLeft());
    }

    private boolean gameEnded() {
        return numOfPasses == 2 * players.getNumOfPlayers();
    }

    public List<String> getPlayerNames() {
        return players.getPlayerNames();
    }

    public List<Integer> getPlayerScores() {
        return players.getPlayerScores();
    }

    public List<PlayedWord> getPlayedWords() {
        return playedWords;
    }

    public boolean playerCanEndTurn(Player player, TurnAction action) {
        boolean playerCanEndTurn = false;
        switch (action) {
            case PLAY:
                playerCanEndTurn = players.getCurrentPlayer() == player && boardLegal
                        && wordsCorrect && player.getTileInHand() == null;
                break;
            case SWAP:
                playerCanEndTurn = players.getCurrentPlayer() == player && player.getTileInHand() == null
                        && gameBoard.getMovableTiles() == 0 && bag.getNumOfTiles() > 0;
                break;
            case PASS:
                playerCanEndTurn = players.getCurrentPlayer() == player && player.getTileInHand() == null
                        && gameBoard.getMovableTiles() == 0;
                break;
            default:
                break;
        }
        return playerCanEndTurn;
    }

    public boolean playerCanExtendTime(Player player) {
        return players.getCurrentPlayer() == player && !player.isTimeExtendedCurrentTurn() && player.getTimeExtensions() < maxExtensions;
    }

    public int getTurn() {
        return turn;
    }

    public Tile[][] getGameBoardTiles() {
        return gameBoard.getTiles();
    }

    public void setThinkingTimeConstraints(int maxThinkingTime, int maxExtensions, int extensionsLength) {
        this.maxThinkingTime = maxThinkingTime;
        this.maxExtensions = maxExtensions;
        this.extensionsLength = extensionsLength;
        this.thinkingTimeCountdownTimer = new ThinkingTimeCountdownTimer();
    }

    public boolean thinkingTimeLimitExists() {
        return thinkingTimeCountdownTimer != null;
    }

    public void setBagSeed(long bagSeed) {
        this.bagSeed = bagSeed;
    }

}
