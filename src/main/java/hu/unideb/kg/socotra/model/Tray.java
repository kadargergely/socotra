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

/**
 *
 * @author Gergely Kadar
 */
public class Tray {

    /**
     * The tiles on the tray.
     */
    private final Tile[] TILES;
    /**
     * The maximum number of tiles that can be placed on the tray.
     */
    public static final int TRAY_SIZE = 7;

    /**
     * Constructs a new tray with no letter tiles on it.
     */
    public Tray() {
        TILES = new Tile[TRAY_SIZE];
    }

    /**
     * Returns all the letters from the tray concatenated as a <code>String</code>.
     *
     * @return the letters from the tray as a <code>String</code>
     */
    public String getLettersAsString() {
        StringBuilder sb = new StringBuilder();
        for (Tile tile : TILES) {
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    /**
     * Returns the tile from the given index from the tray, and replaces the tile with {@code null}.
     * If there is no tile at the given index, the returned value is {@code null}.
     *
     * @param index the index on the tray from which the tile will be picked up
     * @return the tile from the given position from the tray or {@code null} if there is no tile
     */
    public Tile pickTile(int index) {
        Tile tile = TILES[index];
        TILES[index] = null;
        return tile;
    }

    /**
     * Places the given tile on the tray at the given index if possible. If there was no tile at the
     * index, returns <code>true</code> indicating success, otherwise returns <code>false</code>.
     *
     * @param tile the tile to be placed on the tray
     * @param index the index where the tile should be placed
     * @return <code>true</code> if the tile could be placed, <code>false</code> otherwise
     */
    public boolean setTile(Tile tile, int index) {
        if (TILES[index] == null) {
            TILES[index] = tile;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the given tile to the first available position on the tray. Returns <code>true</code> if
     * the tile has been successfully added, meaning there was still place left on the tray,
     * <code>false</code> otherwise.
     *
     * @param tile the tile that should be added to the tray
     * @return <code>true</code> if the tile has been successfully added, <code>false</code>
     * otherwise
     */
    public boolean addTile(Tile tile) {
        boolean isPlace = false;
        for (int i = 0; i < TILES.length; i++) {
            if (TILES[i] == null) {
                TILES[i] = tile;
                isPlace = true;
                break;
            }
        }
        return isPlace;
    }

    /**
     * Removes the first occurrence of a tile from the tray with the given letter. The tile on the
     * tray at this position will be replaced by {@code null}. Returns <code>true</code> if the
     * removal was successful, <code>false</code> if there was no tile on the tray with the given
     * tile code.
     *
     * @param letter the letter which should be removed
     * @return <code>true</code> if the removal is successful, <code>false</code> if couldn't find
     * the tile code on the tray
     */
    public boolean removeTile(String letter) {
        boolean removed = false;
        for (int i = 0; i < TILES.length; i++) {
            if (TILES[i] != null && TILES[i].getLetter().equals(letter)) {
                TILES[i] = null;
                removed = true;
                break;
            }
        }
        return removed;
    }

    /**
     * Returns the number of letters on the tray. It is advisable to check if the number of letters
     * is less than the size of the tray, before calling the <code>addTile</code> method.
     *
     * @return the number of letters on the tray
     */
    public int getNumOfLetters() {
        int numOfLetters = 0;
        for (Tile tile : TILES) {
            if (tile != null) {
                numOfLetters++;
            }
        }
        return numOfLetters;
    }

    /**
     * Returns the tile from the tray, from the given index. The return value is
     * {@code null} if there is no tile at the given index.
     *
     * @param index the index on the tray
     * @return a tile or {@code null} if there is no tile at the given index
     */
    public Tile getTileAt(int index) {
        return TILES[index];
    }
    
    public Tile[] getTiles() {
        return TILES;
    }
}
