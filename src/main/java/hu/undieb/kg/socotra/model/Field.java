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
public class Field {

    /**
     * The possible statuses of a field in a turn.
     */
    public enum Status {
        /**
         * Means, there is no tile on the field.
         */
        EMPTY,
        /**
         * Means, there is a tile on the field placed in the current turn, and can be picked up.
         */
        MOVABLE,
        /**
         * Means, there is a tile on the field placed in one of the preceding turns and can't be
         * moved.
         */
        FIXED
    }

    /**
     * The possible point multipliers a field can give.
     */
    public enum Multiplier {
        /**
         * Means, the field has no multiplier.
         */
        NONE,
        /**
         * Means, the field gives a double multiplier to the value of a word of which letter is
         * newly placed on this field.
         */
        DWORD,
        /**
         * Means, the field gives a triple multiplier to the value of a word of which letter is
         * newly placed on this field.
         */
        TWORD,
        /**
         * Means, the field gives a double multiplier to the value of a letter newly placed on this
         * field.
         */
        DLETTER,
        /**
         * Means, the field gives a triple multiplier to the value of a letter newly placed on this
         * field.
         */
        TLETTER
    }

    /**
     * The tile placed on the field.
     */
    private Tile tile;
    /**
     * The status of this field in a turn.
     */
    private Status status;
    /**
     * The multiplier of the field.
     */
    private Multiplier multiplier;

    /**
     * Constructs a new <code>Field</code> object, and initializes it with no tile and no
     * multiplier.
     */
    public Field() {
        this.multiplier = Multiplier.NONE;
        this.tile = null;
        this.status = Status.EMPTY;
    }

    /**
     * Returns the tile on the field.
     *
     * @return the tile on the field
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile on the field.
     *
     * @param tile the new tile on the field
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Returns the multiplier of the field.
     *
     * @return the multiplier of the field
     */
    public Multiplier getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the multiplier of the field.
     *
     * @param multiplier the multiplier for the field
     */
    public void setMultiplier(Multiplier multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Returns the status of the field in a turn.
     *
     * @return the status of the field in a turn
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the field in a turn.
     *
     * @param status the status for the field in a turn
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
}
