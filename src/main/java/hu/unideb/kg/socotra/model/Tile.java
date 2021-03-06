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
public class Tile {

    private final String LETTER;
    private final boolean JOKER;
    private final int VALUE;

    private String jokerLetter;

    public Tile(String letter, int value) {        
        this(letter, value, false);
    }

    public Tile(String letter, int value, boolean joker) {
        this.LETTER = letter;
        this.VALUE = value;
        this.JOKER = joker;
        this.jokerLetter = Letters.UNSPECIFIED_JOKER;
    }

    public String getLetter() {
        return JOKER ? jokerLetter : LETTER;
    }

    public boolean isJoker() {
        return JOKER;
    }

    public boolean jokerLetterSpecified() {
        return !jokerLetter.equals(Letters.UNSPECIFIED_JOKER);
    }

    public void resetJokerLetter() {
        this.jokerLetter = Letters.UNSPECIFIED_JOKER;
    }

    public int getValue() {
        return VALUE;
    }

    public void setJokerLetter(String letter) {
        if (Letters.isValid(letter)) {
            this.jokerLetter = letter;            
        }
    }

}
