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

import hu.undieb.kg.socotra.controller.GameWindowController;
import hu.undieb.kg.socotra.view.GameView;

/**
 *
 * @author Gergely Kadar
 */
public class LocalHumanPlayer implements Player {

    private String name;
    private int score;
    private Tile tileInHand;
    private GameManager gameManager;
    /**
     * Every LocalHumanPlayer owns a view, which shows the current state of the game to the user.
     * The view can be updated by the LocalHumanPlayer or by a GameManager.
     */
    private GameView gameView;
    private Tray tray;

    public LocalHumanPlayer(String name, GameManager gameManager, GameView gameView) {
        this.name = name;
        this.score = 0;
        this.tileInHand = null;
        this.gameManager = gameManager;
        this.gameView = gameView;
        this.tray = new Tray();
    }

    // TODO: update doc!
    /**
     * Requests a modification on the game board from the {@link GameManager} object. This method is
     * called when the user clicks on the game board. The modification is executed only if the
     * current turn is the player's, otherwise, nothing happens.
     *
     * @param row the row index of the field to alter
     * @param col the column index of the field to alter
     */
    public void mousePressedOnBoard(int row, int col) {
        if (gameManager.alterBoard(row, col, this)) {
            gameView.update(gameManager.getGameState());
        }
    }

    public void mousePressedOnTray(int index) {
        if (gameManager.isPlayersTurn(this)) {
            if (tileInHand == null) {
                tileInHand = tray.pickTile(index);
            } else if (tray.setTile(tileInHand, index)) {
                tileInHand = null;
            }
            gameView.update(gameManager.getGameState());
            gameView.drawTileInHand(tileInHand);
        }
    }

    @Override
    public void boardAltered(int row, int col, Tile tile) {

    }

    @Override
    public void endOfTurn(GameManager.TurnAction action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void drawTiles(Bag bag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBagSize(int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBagSeed(long seed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setTileInHand(Tile tile) {
        this.tileInHand = tile;
        gameView.drawTileInHand(tile);
    }

    @Override
    public Tile getTileInHand() {
        return tileInHand;
    }

}
