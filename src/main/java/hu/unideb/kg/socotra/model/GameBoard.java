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

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Gameboard} class represents the board on which the players place their tiles. An
 * instance of this class stores informations about its fields in a tow dimensional array of
 * {@link Field} objects and about how many tiles were placed on it in the current turn. The methods
 * of this class are essential pieces of the games logic, like testing if the tiles were placed
 * correctly on the board, spell checking the words, calculating the points scored in the current
 * turn.
 *
 * @author Gergely Kadar
 */
public class GameBoard {

    /**
     * The number of fields in a row or column.
     */
    public static final int BOARD_SIZE = 15;
    /**
     * Two dimensional array of <code>Field</code> objects, describing the game board.
     */
    private final Field[][] board;
    /**
     * The number of tiles that were placed on the board in the current turn.
     */
    private int movableTiles;

    /**
     * Constructs a <code>Gameboard</code> object and initializes its two dimensional array of
     * <code>Field</code> objects.
     */
    public GameBoard() {
        board = new Field[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Field();
            }
        }

        movableTiles = 0;

        board[0][0].setMultiplier(Field.Multiplier.TWORD);
        board[0][7].setMultiplier(Field.Multiplier.TWORD);
        board[0][14].setMultiplier(Field.Multiplier.TWORD);
        board[7][0].setMultiplier(Field.Multiplier.TWORD);
        board[14][0].setMultiplier(Field.Multiplier.TWORD);
        board[7][14].setMultiplier(Field.Multiplier.TWORD);
        board[14][14].setMultiplier(Field.Multiplier.TWORD);
        board[14][7].setMultiplier(Field.Multiplier.TWORD);

        board[1][1].setMultiplier(Field.Multiplier.DWORD);
        board[2][2].setMultiplier(Field.Multiplier.DWORD);
        board[3][3].setMultiplier(Field.Multiplier.DWORD);
        board[4][4].setMultiplier(Field.Multiplier.DWORD);

        board[1][13].setMultiplier(Field.Multiplier.DWORD);
        board[2][12].setMultiplier(Field.Multiplier.DWORD);
        board[3][11].setMultiplier(Field.Multiplier.DWORD);
        board[4][10].setMultiplier(Field.Multiplier.DWORD);

        board[13][1].setMultiplier(Field.Multiplier.DWORD);
        board[12][2].setMultiplier(Field.Multiplier.DWORD);
        board[11][3].setMultiplier(Field.Multiplier.DWORD);
        board[10][4].setMultiplier(Field.Multiplier.DWORD);

        board[10][10].setMultiplier(Field.Multiplier.DWORD);
        board[11][11].setMultiplier(Field.Multiplier.DWORD);
        board[12][12].setMultiplier(Field.Multiplier.DWORD);
        board[13][13].setMultiplier(Field.Multiplier.DWORD);

        board[7][7].setMultiplier(Field.Multiplier.DWORD);

        board[1][5].setMultiplier(Field.Multiplier.TLETTER);
        board[1][9].setMultiplier(Field.Multiplier.TLETTER);
        board[5][1].setMultiplier(Field.Multiplier.TLETTER);
        board[5][5].setMultiplier(Field.Multiplier.TLETTER);
        board[5][9].setMultiplier(Field.Multiplier.TLETTER);
        board[5][13].setMultiplier(Field.Multiplier.TLETTER);
        board[9][1].setMultiplier(Field.Multiplier.TLETTER);
        board[9][5].setMultiplier(Field.Multiplier.TLETTER);
        board[9][9].setMultiplier(Field.Multiplier.TLETTER);
        board[9][13].setMultiplier(Field.Multiplier.TLETTER);
        board[13][5].setMultiplier(Field.Multiplier.TLETTER);
        board[13][9].setMultiplier(Field.Multiplier.TLETTER);

        board[0][3].setMultiplier(Field.Multiplier.DLETTER);
        board[0][11].setMultiplier(Field.Multiplier.DLETTER);
        board[14][3].setMultiplier(Field.Multiplier.DLETTER);
        board[14][11].setMultiplier(Field.Multiplier.DLETTER);
        board[3][0].setMultiplier(Field.Multiplier.DLETTER);
        board[11][0].setMultiplier(Field.Multiplier.DLETTER);
        board[3][14].setMultiplier(Field.Multiplier.DLETTER);
        board[11][14].setMultiplier(Field.Multiplier.DLETTER);

        board[2][6].setMultiplier(Field.Multiplier.DLETTER);
        board[2][8].setMultiplier(Field.Multiplier.DLETTER);
        board[3][7].setMultiplier(Field.Multiplier.DLETTER);
        board[6][2].setMultiplier(Field.Multiplier.DLETTER);
        board[8][2].setMultiplier(Field.Multiplier.DLETTER);
        board[7][3].setMultiplier(Field.Multiplier.DLETTER);
        board[6][12].setMultiplier(Field.Multiplier.DLETTER);
        board[8][12].setMultiplier(Field.Multiplier.DLETTER);
        board[7][11].setMultiplier(Field.Multiplier.DLETTER);
        board[12][6].setMultiplier(Field.Multiplier.DLETTER);
        board[12][8].setMultiplier(Field.Multiplier.DLETTER);
        board[11][7].setMultiplier(Field.Multiplier.DLETTER);

        board[6][6].setMultiplier(Field.Multiplier.DLETTER);
        board[8][8].setMultiplier(Field.Multiplier.DLETTER);
        board[6][8].setMultiplier(Field.Multiplier.DLETTER);
        board[8][6].setMultiplier(Field.Multiplier.DLETTER);
    }

    /**
     * Constructs a new {@code Gameboard} object and initializes it with a two dimensional array of
     * {@code Field} objects.
     *
     * @param fields the fields of the new {@code Gameboard} object
     */
    public GameBoard(Field[][] fields) {
        board = fields;
        movableTiles = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getStatus() == Field.Status.MOVABLE) {
                    movableTiles++;
                }
            }
        }
    }

    /**
     * If it is possible, places a tile at the given position on the board.
     *
     * @param row the row index where the tile should be placed
     * @param col the column index where the tile should be placed
     * @param tile the tile to be placed on the board
     * @return <code>true</code> if the tile has been successfully placed on the board,
     * <code>false</code> otherwise
     */
    public boolean setTile(int row, int col, Tile tile) {
        if (board[row][col].getStatus() == Field.Status.EMPTY) {
            board[row][col].setTile(tile);
            board[row][col].setStatus(Field.Status.MOVABLE);
            movableTiles++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the {@code Tile} object of the field with the given row and column indices, if the
     * tile is movable, and sets its value to {@code null}.
     *
     * @param row the row index from where the tile should be removed
     * @param col the column index from where the tile should be removed
     * @return the tile picked up, or {@code null} if there was no tile
     */
    public Tile pickUpTile(int row, int col) {
        if (board[row][col].getStatus() == Field.Status.MOVABLE) {
            Tile tile = board[row][col].getTile();
            board[row][col].setTile(null);
            board[row][col].setStatus(Field.Status.EMPTY);
            movableTiles--;
            return tile;
        } else {
            return null;
        }
    }

    /**
     * Checks if the disposal of the tiles on the board allows the player to end his turn, regarding
     * the rules. The method needs to know that the game is in the first turn, or not, because that
     * is a special case.
     *
     * @param firstTurn <code>true</code> if the game is in the first turn
     * @return <code>true</code> if the state of the board matches the above condition,
     * <code>false</code> otherwise
     */
    public boolean isLegal(boolean firstTurn) {

        int row = -1, col = -1;
        boolean found = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getStatus() == Field.Status.MOVABLE) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (found) {
            boolean connected = false;

            int tilesInRow = 0;
            for (int j = col; j < BOARD_SIZE; j++) {
                if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                    tilesInRow++;
                }
            }

            int tilesInCol = 0;
            for (int i = row; i < BOARD_SIZE; i++) {

                if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                    tilesInCol++;
                }
            }

            if (tilesInRow > 1) {
                if (tilesInRow < movableTiles) {
                    return false;
                } else {
                    int connectedTiles = 0;
                    for (int j = col; j < BOARD_SIZE; j++) {
                        if (board[row][j].getStatus() == Field.Status.EMPTY) {
                            break;
                        } else if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                            connectedTiles++;
                        }
                        if (!connected && !firstTurn) {
                            if ((row > 0 && board[row - 1][j].getStatus() == Field.Status.FIXED)
                                    || (row < BOARD_SIZE - 1 && board[row + 1][j]
                                    .getStatus() == Field.Status.FIXED)
                                    || (j > 0 && board[row][j - 1].getStatus() == Field.Status.FIXED)
                                    || (j < BOARD_SIZE - 1 && board[row][j + 1]
                                    .getStatus() == Field.Status.FIXED)) {
                                connected = true;
                            }
                        }
                    }
                    if (connectedTiles < movableTiles) {
                        return false;
                    }
                }

            } else if (tilesInCol > 1) {
                if (tilesInCol < movableTiles) {
                    return false;
                } else {
                    int connectedTiles = 0;
                    for (int i = row; i < BOARD_SIZE; i++) {
                        if (board[i][col].getStatus() == Field.Status.EMPTY) {
                            break;
                        } else if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                            connectedTiles++;
                        }
                        if (!connected && !firstTurn) {
                            if ((col > 0 && board[i][col - 1].getStatus() == Field.Status.FIXED)
                                    || (col < BOARD_SIZE - 1 && board[i][col + 1]
                                    .getStatus() == Field.Status.FIXED)
                                    || (i > 0 && board[i - 1][col].getStatus() == Field.Status.FIXED)
                                    || (i < BOARD_SIZE - 1 && board[i + 1][col]
                                    .getStatus() == Field.Status.FIXED)) {
                                connected = true;
                            }
                        }
                    }
                    if (connectedTiles < movableTiles) {
                        return false;
                    }
                }

            } else if (movableTiles > 1) {
                return false;

            } else if (!connected && !firstTurn) {
                if ((col > 0 && board[row][col - 1].getStatus() == Field.Status.FIXED)
                        || (col < BOARD_SIZE - 1 && board[row][col + 1]
                        .getStatus() == Field.Status.FIXED)
                        || (row > 0 && board[row - 1][col].getStatus() == Field.Status.FIXED)
                        || (row < BOARD_SIZE - 1 && board[row + 1][col]
                        .getStatus() == Field.Status.FIXED)) {
                    connected = true;
                }
            }
            if (!connected && !firstTurn) {
                return false;
            }
            if (firstTurn && board[7][7].getStatus() != Field.Status.MOVABLE) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Reads a word horizontally, starting from the given position. It is called by the public
     * method <code>getPlayedWords</code>.
     *
     * @param row the row index of the starting position
     * @param col the column index of the starting position
     * @return the word read, or <code>null</code> if there is no tile on the starting position
     */
    private String readRowWord(int row, int col) {
        int startCol = 0;
        StringBuilder word = new StringBuilder();

        if (board[row][col].getStatus() == Field.Status.EMPTY) {
            return null;
        }

        for (int j = col; j >= 0; j--) {
            if (board[row][j].getStatus() == Field.Status.EMPTY) {
                startCol = j + 1;
                break;
            }
        }

        for (int j = startCol; j < BOARD_SIZE; j++) {
            if (board[row][j].getStatus() == Field.Status.EMPTY) {
                break;
            }
            word.append(board[row][j].getTile().getLetter());
        }

        return word.toString();
    }

    /**
     * Reads a word vertically, starting from the given position. It is called by the public method
     * <code>getPlayedWords</code>.
     *
     * @param row the row index of the starting position
     * @param col the column index of the starting position
     * @return the word read, or <code>null</code> if there is no tile on the starting position
     */
    private String readColWord(int row, int col) {
        int startRow = 0;
        StringBuilder word = new StringBuilder();

        if (board[row][col].getStatus() == Field.Status.EMPTY) {
            return null;
        }

        for (int i = row; i >= 0; i--) {
            if (board[i][col].getStatus() == Field.Status.EMPTY) {
                startRow = i + 1;
                break;
            }
        }

        for (int i = startRow; i < BOARD_SIZE; i++) {
            if (board[i][col].getStatus() == Field.Status.EMPTY) {
                break;
            }
            word.append(board[row][i].getTile().getLetter());
        }

        return word.toString();
    }

    /**
     * Returns a list of the words played in the current turn. The method assumes that the state of
     * the board allows the player to end his turn regarding the rules, i.e. the
     * <code>isLegal</code> method returns <code>true</code>, otherwise the return value will be
     * undefined.
     *
     * @return a list of strings containing the words played in the current turn, or
     * <code>null</code> if there were no words played in the current turn
     */
    public List<String> getPlayedWords() {

        int col = -1, row = -1;
        List<String> playedWords = new ArrayList<>();

        boolean found = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getStatus() == Field.Status.MOVABLE) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            return null;
        }

        boolean tilesInRow = false;
        for (int j = col + 1; j < BOARD_SIZE; j++) {
            if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                tilesInRow = true;
                break;
            }
        }
        boolean tilesInCol = false;
        for (int i = row + 1; i < BOARD_SIZE; i++) {
            if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                tilesInCol = true;
                break;
            }
        }

        if (tilesInRow) {
            playedWords.add(readRowWord(row, col));
            for (int j = col; j < BOARD_SIZE; j++) {
                if (board[row][j].getStatus() == Field.Status.EMPTY) {
                    break;
                } else if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                    String w = readColWord(row, j);
                    if (w.length() > 1
                            && !(w.equals("CS") || w.equals("GY")
                            || w.equals("LY") || w.equals("NY")
                            || w.equals("SZ") || w.equals("TY") || w
                            .equals("ZS"))) {
                        playedWords.add(w);
                    }
                }
            }
        } else if (tilesInCol) {
            playedWords.add(readColWord(row, col));
            for (int i = row; i < BOARD_SIZE; i++) {
                if (board[i][col].getStatus() == Field.Status.EMPTY) {
                    break;
                } else if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                    String w = readRowWord(i, col);
                    if (w.length() > 1
                            && !(w.equals("CS") || w.equals("GY")
                            || w.equals("LY") || w.equals("NY")
                            || w.equals("SZ") || w.equals("TY") || w
                            .equals("ZS"))) {
                        playedWords.add(w);
                    }
                }
            }
        } else {
            String rowWord = readRowWord(row, col);
            if (rowWord.length() > 1
                    && !(rowWord.equals("CS") || rowWord.equals("GY")
                    || rowWord.equals("LY") || rowWord.equals("NY")
                    || rowWord.equals("SZ") || rowWord.equals("TY") || rowWord
                    .equals("ZS"))) {
                playedWords.add(rowWord);
            }
            String colWord = readColWord(row, col);
            if (colWord.length() > 1
                    && !(colWord.equals("CS") || colWord.equals("GY")
                    || colWord.equals("LY") || colWord.equals("NY")
                    || colWord.equals("SZ") || colWord.equals("TY") || colWord
                    .equals("ZS"))) {
                playedWords.add(colWord);
            }
        }

        return playedWords;
    }

    /**
     * Calculates the value in points of a word placed horizontally and including the given
     * position. This method is used by the public <code>calcPoints()</code> method.
     *
     * @param row the row index of the starting position of the word
     * @param col the column index of the starting position of the word
     * @return the value of the word in points
     */
    private int calcRowWordPts(int row, int col) {
        int startCol = 0;
        int pts = 0;
        int factor = 1;

        if (board[row][col].getStatus() == Field.Status.EMPTY) {
            return 0;
        }

        // Find factors.
        for (int j = col; j < BOARD_SIZE; j++) {
            if (board[row][j].getStatus() == Field.Status.EMPTY) {
                break;
            } else if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                if (board[row][j].getMultiplier() == Field.Multiplier.DWORD) {
                    factor *= 2;
                } else if (board[row][j].getMultiplier() == Field.Multiplier.TWORD) {
                    factor *= 3;
                }
            }
        }

        // Find the first tile of the word.
        for (int j = col; j >= 0; j--) {
            if (board[row][j].getStatus() == Field.Status.EMPTY) {
                startCol = j + 1;
                break;
            }
        }

        // Calculate points.
        for (int j = startCol; j < BOARD_SIZE; j++) {
            int value = board[row][j].getTile().getValue();

            if (board[row][j].getStatus() == Field.Status.EMPTY) {
                break;
            } else if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                switch (board[row][j].getMultiplier()) {
                    case DLETTER:
                        pts += 2 * factor * value;
                        break;
                    case TLETTER:
                        pts += 3 * factor * value;
                        break;
                    default:
                        pts += factor * value;
                        break;
                }
            } else {
                pts += factor * value;
            }
        }

        return pts;
    }

    /**
     * Calculates the value in points of a word placed vertically and including the given position.
     * This method is used by the public <code>calcPoints()</code> method.
     *
     * @param row the row index of the starting position of the word
     * @param col the column index of the starting position of the word
     * @return the value of the word in points
     */
    private int calcColWordPts(int row, int col) {
        int startRow = 0;
        int pts = 0;
        int factor = 1;

        if (board[row][col].getStatus() == Field.Status.EMPTY) {
            return 0;
        }

        // Find factors.
        for (int i = row; i < BOARD_SIZE; i++) {
            if (board[i][col].getStatus() == Field.Status.EMPTY) {
                break;
            } else if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                if (board[i][col].getMultiplier() == Field.Multiplier.DWORD) {
                    factor *= 2;
                } else if (board[i][col].getMultiplier() == Field.Multiplier.TWORD) {
                    factor *= 3;
                }
            }
        }

        // Find the first tile of the word.
        for (int i = row; i >= 0; i--) {
            if (board[i][col].getStatus() == Field.Status.EMPTY) {
                startRow = i + 1;
                break;
            }
        }

        // Calculate points.
        for (int i = startRow; i < BOARD_SIZE; i++) {
            int value = board[i][col].getTile().getValue();

            if (board[i][col].getStatus() == Field.Status.EMPTY) {
                break;
            } else if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                switch (board[i][col].getMultiplier()) {
                    case DLETTER:
                        pts += 2 * factor * value;
                        break;
                    case TLETTER:
                        pts += 3 * factor * value;
                        break;
                    default:
                        pts += factor * value;
                        break;
                }
            } else {
                pts += factor * value;
            }
        }

        return pts;
    }

    /**
     * Returns the number of points the player will get in the current turn, based on the current
     * state of the board. The method assumes that the state of the board allows the player to end
     * his turn regarding the rules, i.e. the <code>isLegal()</code> method returns
     * <code>true</code>, otherwise the return value will be undefined.
     *
     * @return the total number of points gained in the current turn, or undefined value if the
     * <code>isLegal()</code> method returns <code>false</code>
     */
    public int calcPoints() {
        int pts = 0;
        int col = -1, row = -1;

        boolean found = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getStatus() == Field.Status.MOVABLE) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            return 0;
        }

        boolean tilesInRow = false;
        for (int j = col + 1; j < BOARD_SIZE; j++) {
            if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                tilesInRow = true;
                break;
            }
        }
        boolean tilesInCol = false;
        for (int i = row + 1; i < BOARD_SIZE; i++) {
            if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                tilesInCol = true;
                break;
            }
        }

        if (tilesInRow) {
            pts += calcRowWordPts(row, col);
            for (int j = col; j < BOARD_SIZE; j++) {
                if (board[row][j].getStatus() == Field.Status.EMPTY) {
                    break;
                } else if (board[row][j].getStatus() == Field.Status.MOVABLE) {
                    if ((row != 0 && board[row - 1][j].getStatus() != Field.Status.EMPTY)
                            || (row < BOARD_SIZE - 1 && board[row + 1][j]
                            .getStatus() != Field.Status.EMPTY)) {
                        pts += calcColWordPts(row, j);
                    }
                }
            }
        } else if (tilesInCol) {
            pts += calcColWordPts(row, col);
            for (int i = row; i < BOARD_SIZE; i++) {
                if (board[i][col].getStatus() == Field.Status.EMPTY) {
                    break;
                } else if (board[i][col].getStatus() == Field.Status.MOVABLE) {
                    if ((col != 0 && board[i][col - 1].getStatus() != Field.Status.EMPTY)
                            || (col < BOARD_SIZE - 1 && board[i][col + 1]
                            .getStatus() != Field.Status.EMPTY)) {
                        pts += calcRowWordPts(i, col);
                    }
                }
            }
        } else if (board[row][col].getStatus() == Field.Status.MOVABLE) {
            if ((row != 0 && board[row - 1][col].getStatus() != Field.Status.EMPTY)
                    || (row < BOARD_SIZE - 1 && board[row + 1][col]
                    .getStatus() != Field.Status.EMPTY)) {
                pts += calcColWordPts(row, col);
            }
            if ((col != 0 && board[row][col - 1].getStatus() != Field.Status.EMPTY)
                    || (col < BOARD_SIZE - 1 && board[row][col + 1]
                    .getStatus() != Field.Status.EMPTY)) {
                pts += calcRowWordPts(row, col);
            }
        }

        return pts;
    }

    /**
     * Returns the number of tiles placed on the board in the current turn.
     *
     * @return the movableTiles the number of tiles placed on the board in the current turn
     */
    public int getMovableTiles() {
        return movableTiles;
    }

    /**
     * Checks if the tiles placed on the board in the current turn form grammatically correct words.
     * If there are no such tiles, the method will return <code>false</code>. The method receives a
     * {@link Dictionary} object with the words considered correct.
     *
     * @param dict the <code>Dictionary</code> object
     * @return <code>true</code> if all the words placed on the board in the current turn are
     * correct, <code>false</code> if there are no tiles placed on the board in the current turn, or
     * there is at least one grammatically incorrect word.
     */
    public boolean wordsAreCorrect(Dictionary dict) {
        List<String> playedWords = getPlayedWords();
        if (playedWords == null) {
            return false;
        }
        boolean areCorrect = true;

        for (String word : playedWords) {
            if (!dict.isCorrect(word)) {
                areCorrect = false;
                break;
            }
        }

        return areCorrect;
    }

    /**
     * Sets the status of each {@code MOVABLE} field to {@code FIXED} and also sets the number of
     * movable tiles to 0. Is usually called at the end of a turn.
     */
    public void finalizeFields() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].getStatus() == Field.Status.MOVABLE) {
                    board[i][j].setStatus(Field.Status.FIXED);
                }
            }
        }
        movableTiles = 0;
    }
    
    public Tile[][] getTiles() {
    	Tile[][] tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
    	for (int i = 0; i < BOARD_SIZE; i++) {
    		for (int j = 0; j < BOARD_SIZE; j++) {
    			tiles[i][j] = board[i][j].getTile();
    		}
    	}
    	return tiles;
    }
}
