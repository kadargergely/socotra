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

/**
 *
 * @author Gergely Kadar
 */
public class GameManager {

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

        public final int TURN;
        public final String PLAYER_NAME;
        public final String WORD;
        public final int SCORE;

        public PlayedWord(int turn, String playerName, String word, int score) {
            this.TURN = turn;
            this.PLAYER_NAME = playerName;
            this.WORD = word;
            this.SCORE = score;
        }
    }

    private Players players;
    private int currentPlayer;
    private boolean boardLegal;
    private boolean movableTiles;
    private boolean wordsCorrect;
    private int turn;
    private int numOfPasses;
    private List<PlayedWord> playedWords;

    private static final String REDRAW = "<csere>";
    private static final String PASS = "<passz>";

    private GameBoard gameBoard;
    private Bag bag;
    private long bagSeed;
    private Dictionary dictionary;

    private List<GameObserver> observers;

    public GameManager(InputStreamReader inputStream, Players players)
            throws FileNotFoundException {
        gameBoard = new GameBoard();
        observers = new ArrayList<>();
        this.players = players;
        bag = new Bag();
        dictionary = new Dictionary(inputStream);
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void addObservers(List<GameObserver> observers) {
        this.observers = observers;
    }

    public void startGame(long bagSeed) {
//        bagSeed = Double.doubleToLongBits(Math.random());
        this.bagSeed = bagSeed;
        bag.shuffle(bagSeed);
        observers.forEach(o -> o.gameStarted(bagSeed));

        currentPlayer = 0;
        boardLegal = true;
        movableTiles = false;
        wordsCorrect = true;
        turn = 1;
        numOfPasses = 0;
    }

    /**
     * Constructs a new {@code GameManager} object with the given players and input stream to read the dictionary.
     *
     * @param inputStream input stream, the dictionary file is read from
     * @param playerNames the name of the players
     * @param playerTypes the type of the players (human, computer or remote player)
     * @param gameView
     * @throws FileNotFoundException when failed to find the dictionary file
     */
//    public GameManager(InputStreamReader inputStream, List<String> playerNames, List<PlayerType> playerTypes, GameView gameView)
//            throws FileNotFoundException {
//        gameBoard = new GameBoard();
//        players = new ArrayList<>();
//        for (int i = 0; i < playerNames.size(); i++) {
//            switch (playerTypes.get(i)) {
//                case HUMAN:
//                    players.add(new LocalHumanPlayer(playerNames.get(i), this, gameView));
//                case COMPUTER:
//                    players.add(new ComputerPlayer(playerNames.get(i), this));
//                case REMOTE:
//                    players.add(new RemotePlayer(playerNames.get(i), this));
//            }
//        }
////        long bagSeed = Double.doubleToLongBits(Math.random());
//        bag = new Bag(bagSeed);
//        players.forEach(p -> p.setBagSeed(bagSeed));
//        dictionary = new Dictionary(inputStream);
//
//        currentPlayer = 0;
//        boardLegal = true;
//        movableTiles = false;
//        wordsCorrect = true;
//        turn = 0;
//        numOfPasses = 0;
//    }
//    /**
//     * Returns true if the current turn is the given player's one, false otherwise. This method is
//     * usually called by the players to check themselves.
//     *
//     * @param player the player
//     * @return {@code true} if the current turn is the given player's one, {@code false} otherwise
//     */
//    public boolean isPlayersTurn(Player player) {
//        return players.get(currentPlayer) == player;
//    }
    // TODO: update documentation!
    /**
     * Alters a field on the board if the given player is the current one. Returns {@code true} if the board has been
     * successfully altered, {@code false} otherwise.
     *
     * @param row the row index of the field to be altered
     * @param col the column index of the field to be altered
     * @param player the player who tries to alter the board
     * @return {@code true} if the board has been successfully altered, {@code false} otherwise
     */
    public boolean alterBoard(int row, int col, Player player) {
        if (players.getCurrentPlayer() == player) {
//            players.stream().filter(p -> p != player).forEach(p -> {
//                p.boardAltered(row, col, player.getTileInHand());
//            });

            if (player.getTileInHand() == null) {
                Tile pickedUpTile = gameBoard.pickUpTile(row, col);
                if (pickedUpTile != null) {
                    player.setTileInHand(pickedUpTile);
                } else {
                    return false;
                }
            } else if (gameBoard.setTile(row, col, player.getTileInHand())) {
                player.setTileInHand(null);
            } else {
                return false;
            }

            boardLegal = gameBoard.isLegal(turn == 1);
            if (boardLegal) {

            }

            observers.forEach(o -> o.boardAltered(row, col, player));

            return true;
        } else {
            return false;
        }
    }

    public boolean endTurn(TurnAction action, Player player) {
        if (players.getCurrentPlayer() == player) {
//            players.stream().filter(p -> p != player).forEach(p -> {
//                p.endOfTurn(action, player);
//            });

            if (null != action) {
                switch (action) {
                    case PLAY:
                        numOfPasses = 0;
                        int points = gameBoard.calcPoints();
                        player.setScore(player.getScore() + points);
                        List<String> wordsCurrentTurn = gameBoard.getPlayedWords();
                        StringJoiner sj = new StringJoiner(", ");
                        wordsCurrentTurn.stream().forEach(w -> sj.add(w));
                        playedWords.add(new PlayedWord(turn, player.getName(), sj.toString(), points));
                        player.drawTiles(bag);
                        gameBoard.finalizeFields();
                        break;
                    case SWAP:
                        numOfPasses = 0;
                        playedWords.add(new PlayedWord(turn, player.getName(), REDRAW, 0));
                        player.redrawTiles(bag);
                        bag.shuffle(bagSeed);
                        break;
                    case PASS:
                        numOfPasses++;
                        if (gameEnded()) {
                            // TODO handle game ended
                        }
                        playedWords.add(new PlayedWord(turn, player.getName(), PASS, 0));
                        break;
                    default:
                        break;
                }
            }

            turn++;
            players.next();

            return true;
        } else {
            return false;
        }
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

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isBoardLegal() {
        return boardLegal;
    }

    public boolean isMovableTiles() {
        return movableTiles;
    }

    public boolean isWordsCorrect() {
        return wordsCorrect;
    }

    public int getTurn() {
        return turn;
    }

    public Tile[][] getGameBoardTiles() {
        return gameBoard.getTiles();
    }

}
