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

/**
 *
 * @author Gergely Kadar
 */
public interface Player {

    /**
     * Using this method, a player can be notified about a change on the game board and can act
     * accordingly.
     *
     * @param row the row index of the changed game board field
     * @param col the column index of the changed game board field
     * @param tile the new tile on the field ({@code null}, if the tile has been picked up) 
     */
    public void boardAltered(int row, int col, Tile tile);

    /**
     * Using this method, a player can be notified about the end of a turn. The player should check
     * if he/she is the next one.
     *
     * @param action the way the turn ended
     */
    public void endOfTurn(GameManager.TurnAction action);

    /**
     * The player draws tiles from the bag given.
     *
     * @param bag the bag from which the player draws tiles
     */
    public void drawTiles(Bag bag);

    /**
     * Using this method, a player can be notified about the number of letters left in the bag. Most
     * useful to remote players who synchronize their bag based on this information.
     *
     * @param size
     */
    public void setBagSize(int size);

    /**
     * Used to produce an identic permutation of the letters in the bag for all players at the
     * beginning of the game.
     *
     * @param seed the seed used to shuffle the letters in the bag
     */
    public void setBagSeed(long seed);

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName();

    /**
     * Sets the score of the player.
     *
     * @param score the new score
     */
    public void setScore(int score);

    /**
     * Returns the score of the player.
     *
     * @return the score of the player
     */
    public int getScore();

    /**
     * Sets the tile in the hand of the player.
     *
     * @param tile the tile to be set
     */
    public void setTileInHand(Tile tile);

    /**
     * Returns the tile in the hand of the player.
     *
     * @return the tile
     */
    public Tile getTileInHand();
}
