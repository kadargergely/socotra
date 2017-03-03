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

import hu.undieb.kg.socotra.view.GameView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gergely Kadar
 */
public class LocalHumanPlayer implements Player {

    private String name;
    private int score;
    private Tile tileInHand;
    /**
     * The LocalHumanPlayer owns a view, which shows the current state of the game to the user.
     */
    private GameView gameView; // TODO: not needed
    private Tray tray;
    
    private List<GameObserver> observers;

    public LocalHumanPlayer(String name) {
        this.name = name;
        this.score = 0;
        this.tileInHand = null;
        // this.gameView = gameView;
        this.tray = new Tray();
    }   

    // TODO: update doc!
//    /**
//     * Requests a modification on the game board from the {@link GameManager} object. This method is
//     * called when the user clicks on the game board. The modification is executed only if the
//     * current turn is the player's, otherwise, nothing happens.
//     *
//     * @param row the row index of the field to alter
//     * @param col the column index of the field to alter
//     */
//    public void mousePressedOnBoard(int row, int col) {
//        if (gameManager.alterBoard(row, col, this)) {            
//            gameView.update(gameManager.getGameState());
//        }
//    }
//    public void mousePressedOnTray(int index) {
//        if (gameManager.isPlayersTurn(this)) {
//            if (tileInHand == null) {
//                tileInHand = tray.pickTile(index);
//            } else if (tray.setTile(tileInHand, index)) {
//                tileInHand = null;
//            }
//            gameView.update(gameManager.getGameState());
//            gameView.drawTileInHand(tileInHand);
//        }
//    }
    public void alterTray(int index) {
        if (tileInHand == null) {
            tileInHand = tray.pickTile(index);
            if (tileInHand == null) {
                return;
            }
        } else if (tray.getTileAt(index) == null) {
            tray.setTile(tileInHand, index);
            tileInHand = null;
        } else {
            Tile pickedUpTile = tray.pickTile(index);
            tray.setTile(tileInHand, index);
            tileInHand = pickedUpTile;
        }
        observers.forEach(o -> o.trayAltered(index, this));
    }

    @Override
    public void boardAltered(int row, int col, Tile tile) {
        gameView.update();
    }

    @Override
    public void endOfTurn(GameManager.TurnAction action, Player player) {
        gameView.update();
    }

    @Override
    public void gameStarted(long bagSeed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void drawTiles(Bag bag) {
        while (tray.getNumOfLetters() < Tray.TRAY_SIZE) {
            Tile drawnTile = bag.getTile();
            if (drawnTile == null) {
                break;
            } else {
                tray.addTile(drawnTile);
            }
        }
    }

    @Override
    public void redrawTiles(Bag bag) {
        // TODO make this better!
        List<Tile> newTiles = new ArrayList<>();
        for (int i = 0; i < Tray.TRAY_SIZE; i++) {
            Tile newTile = bag.getTile();
            if (newTile != null) {
                newTiles.add(newTile);
            } else {
                break;
            }
        }
        for (int i = 0; i < Tray.TRAY_SIZE; i++) {
            Tile pickedTile = tray.pickTile(i);
            if (pickedTile != null) {
                bag.putBackTile(pickedTile);
            }
        }
        newTiles.forEach(tile -> tray.addTile(tile));
    }    

    @Override
    public void setName(String name) {
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
    public Tile[] getTrayTiles() {
        return tray.getTiles();
    }

    @Override
    public void setTileInHand(Tile tile) {
        this.tileInHand = tile;        
    }

    @Override
    public Tile getTileInHand() {
        return tileInHand;
    }

}
