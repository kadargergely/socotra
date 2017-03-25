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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gergely Kadar
 */
public class Player {

    private String name;
    private int score;
    private Tile tileInHand;
    private PlayerType playerType;
    private Tray tray;

    public enum PlayerType {
        HUMAN,
        COMPUTER,
        REMOTE
    }

    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.score = 0;
        this.tileInHand = null;
        this.playerType = playerType;
        this.tray = new Tray();
    }

    /**
     * The player alters the tray at the given index. The player does this, using his/her tile in hand. If there is no
     * modification on the tray, the method returns false. This happens when the player has no tile in hand, and there
     * is no tile at the given index on the tray either.
     *
     * @param index the index to alter the tray at
     * @return {@code true} if there was a modification
     */
    public boolean alterTray(int index) {
        if (tileInHand == null) {
            tileInHand = tray.pickTile(index);
            if (tileInHand == null) {
                return false;
            }
        } else if (tray.getTileAt(index) == null) {
            if (tileInHand == null) {
                return false;
            }
            tray.setTile(tileInHand, index);
            tileInHand = null;
        } else {
            Tile pickedUpTile = tray.pickTile(index);
            tray.setTile(tileInHand, index);
            tileInHand = pickedUpTile;
        }

        return true;
    }

    /**
     * The player draws tiles from the bag given to fill his/her tray. This step is performed at the end of a turn.
     *
     * @param bag the bag from which the player draws tiles
     */
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

    /**
     * The player puts back all his/her tiles into the bag, and draws new ones.
     *
     * @param bag the bag to perform the action with
     */
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

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the score of the player.
     *
     * @param score the new score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the score of the player.
     *
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the tile in the hand of the player. This method is called by the GameManager when the player alters the
     * board.
     *
     * @param tile the tile to be set
     */
    public void setTileInHand(Tile tile) {
        this.tileInHand = tile;
    }

    /**
     * Returns the tile in the hand of the player.
     *
     * @return the tile
     */
    public Tile getTileInHand() {
        return tileInHand;
    }

    /**
     * Returns the player's tiles from the tray. This is needed to show the tiles on the UI.
     *
     * @return the player's tiles from the tray
     */
    public Tile[] getTrayTiles() {
        return tray.getTiles();
    }

    /**
     * Returns the type of the player.
     *
     * @return the type of the player
     */
    public PlayerType getPlayerType() {
        return playerType;
    }

}
