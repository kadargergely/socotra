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
package hu.undieb.kg.socotra.model;

import hu.undieb.kg.socotra.model.networking.NetworkManager;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import hu.undieb.kg.socotra.view.GameView;

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

    public enum PlayerType {
        HUMAN,
        COMPUTER,
        REMOTE
    }   

    private List<Player> players;
    private int currentPlayer;
    private boolean boardLegal;
    private boolean movableTiles;
    private boolean wordsCorrect;
    private int turn;
    private int numOfPasses;

    private GameBoard gameBoard;
    private Bag bag;
    private Dictionary dictionary;
    private NetworkManager networkManager;
    
    private GameView gameView;

    /**
     * Constructs a new {@code GameManager} object with the given players and input stream to read
     * the dictionary.
     *
     * @param inputStream input stream, the dictionary file is read from
     * @param playerNames the name of the players
     * @param playerTypes the type of the players (human, computer or remote player)
     * @param gameView
     * @throws FileNotFoundException when failed to find the dictionary file
     */
    public GameManager(InputStreamReader inputStream, List<String> playerNames, List<PlayerType> playerTypes, GameView gameView)
            throws FileNotFoundException {
        gameBoard = new GameBoard();
        players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            switch (playerTypes.get(i)) {
                case HUMAN:
                    players.add(new LocalHumanPlayer(playerNames.get(i), this, gameView));
                case COMPUTER:
                    players.add(new ComputerPlayer(playerNames.get(i), this));
                case REMOTE:
                    players.add(new RemotePlayer(playerNames.get(i), this));
            }
        }
        long bagSeed = Double.doubleToLongBits(Math.random());
        bag = new Bag(bagSeed);
        players.forEach(p -> p.setBagSeed(bagSeed));
        dictionary = new Dictionary(inputStream);

        currentPlayer = 0;
        boardLegal = true;
        movableTiles = false;
        wordsCorrect = true;
        turn = 0;
        numOfPasses = 0;
    }

    /**
     * Returns true if the current turn is the given player's one, false otherwise. This method is
     * usually called by the players to check themselves.
     *
     * @param player the player
     * @return {@code true} if the current turn is the given player's one, {@code false} otherwise
     */
    public boolean isPlayersTurn(Player player) {
        return players.get(currentPlayer) == player;
    }

    // TODO: update documentation!
    /**
     * Alters a field on the board if the given player is the current one. Returns {@code true} if
     * the board has been successfully altered, {@code false} otherwise.
     *
     * @param row the row index of the field to be altered
     * @param col the column index of the field to be altered
     * @param player the player who tries to alter the board
     * @return {@code true} if the board has been successfully altered, {@code false} otherwise
     */
    public boolean alterBoard(int row, int col, Player player) {
        if (players.get(currentPlayer) == player) {
            players.stream().filter(p -> p != player).forEach(p -> {
                p.boardAltered(row, col, players.get(currentPlayer).getTileInHand());
            });

            if (players.get(currentPlayer).getTileInHand() == null) {
                Tile pickedUpTile = gameBoard.pickUpTile(row, col);
                if (pickedUpTile != null) {
                    players.get(currentPlayer).setTileInHand(pickedUpTile);
                }
            } else if (gameBoard.setTile(row, col, players.get(currentPlayer).getTileInHand())) {
                players.get(currentPlayer).setTileInHand(null);
            } else {
                return false;
            }
            
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the graphical user interface based the current status of the game from the
     * perspective of the player given, using the {@link GameView} instance given.
     *
     * @param player the player who requests the update
     * @param gameView the {@link GameView} instance of which methods are called to update the view
     */
    public void updateGameView(Player player, GameView gameView) {

    }
    
    public List<String> getPlayerNames() {
    	List<String> playerNames = new ArrayList<>();
    	for (Player player : players) {
    		playerNames.add(player.getName());
    	}
    	return playerNames;
    }
    
    public List<Integer> getPlayerScores() {
    	List<Integer> playerScores = new ArrayList<>();
    	for (Player player : players) {
    		playerScores.add(player.getScore());
    	}
    	return playerScores;
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
    
    public GameState getGameState() {
    	GameState currentGameState = new GameState();
    	
    	List<String> playerNames = new ArrayList<>();
    	for (Player player : players) {
    		playerNames.add(player.getName());
    	}
    	currentGameState.setPlayerNames(playerNames);
    	
    	List<Integer> playerScores = new ArrayList<>();
    	for (Player player : players) {
    		playerScores.add(player.getScore());
    	}
    	currentGameState.setPlayerScores(playerScores);
    	
    	currentGameState.setGameBoardTiles(gameBoard.getTiles());
    	currentGameState.setCurrentPlayer(currentPlayer);
    	currentGameState.setBoardLegal(boardLegal);
    	currentGameState.setWordsCorrect(wordsCorrect);
    	currentGameState.setMovableTiles(movableTiles);
    	currentGameState.setTurn(turn);
    	
    	return currentGameState;
    }
}
