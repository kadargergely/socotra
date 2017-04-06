package hu.unideb.kg.socotra.model;

import java.util.List;

public class GameState {

    private Tile[][] gameBoardTiles;
    private List<String> playerNames;
    private List<Integer> playerScores;
    private int currentPlayer;
    private boolean boardLegal;
    private boolean wordsCorrect;
    private boolean movableTiles;
    private int turn;

    public Tile[][] getGameBoardTiles() {
        return gameBoardTiles;
    }

    public void setGameBoardTiles(Tile[][] gameBoardTiles) {
        this.gameBoardTiles = gameBoardTiles;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public List<Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isBoardLegal() {
        return boardLegal;
    }

    public void setBoardLegal(boolean boardLegal) {
        this.boardLegal = boardLegal;
    }

    public boolean isWordsCorrect() {
        return wordsCorrect;
    }

    public void setWordsCorrect(boolean wordsCorrect) {
        this.wordsCorrect = wordsCorrect;
    }

    public boolean isMovableTiles() {
        return movableTiles;
    }

    public void setMovableTiles(boolean movableTiles) {
        this.movableTiles = movableTiles;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

}
