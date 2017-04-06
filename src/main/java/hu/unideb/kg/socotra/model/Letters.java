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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gergely Kadar
 */
public class Letters {

    private static final List<String> LETTERS = Arrays.asList("A", "Á", "B", "C", "CS", "D",
            "E", "É", "F", "G", "GY", "H", "I", "Í", "J", "K", "L", "LY", "M",
            "N", "NY", "O", "Ó", "Ö", "Ő", "P", "R", "S", "SZ", "T", "TY", "U",
            "Ú", "Ü", "Ű", "V", "Z", "ZS", "_");

    private static final int VALUES[] = {1, 1, 2, 5, 7, 2, 1, 3, 4, 2, 4, 3,
        1, 5, 4, 1, 1, 8, 1, 1, 5, 1, 2, 4, 7, 4, 1, 1, 3, 1, 10, 4, 7, 4,
        7, 3, 4, 8, 0};

    private static final int QUANTITIES[] = {6, 4, 3, 1, 1, 3, 6, 3, 2, 3, 2,
        2, 3, 1, 2, 6, 4, 1, 3, 4, 1, 3, 3, 2, 1, 2, 4, 3, 2, 5, 1, 2, 1,
        2, 1, 2, 2, 1, 2};

    public static final int JOKER_INDEX = LETTERS.size() - 1;

    /**
     * Return {@code true} if the alphabet contains the given letter.
     *
     * @param letter the letter
     * @return {@code true} if the alphabet contains the given letter
     */
    public static boolean isValid(String letter) {
        return LETTERS.contains(letter);
    }

    /**
     * Returns the number of the given letter in the alphabet. Returns -1 if the alphabet doesn't
     * contain the letter.
     *
     * @param letter the letter
     * @return the number of the given letter in the alphabet or -1 if the letter isn't contained by
     * the alphabet
     */
    public static int getLetterNum(String letter) {
        return LETTERS.indexOf(letter);
    }
    
    /**
     * Returns the number of different letters contained by the alphabet.
     * @return the number of different letters contained by the alphabet
     */
    public static int getNumOfLetters() {
        return LETTERS.size();
    }

    /**
     * Returns a list containing all the tiles in the game.
     *
     * @return a list containing all the tiles in the game
     */
    public static List<Tile> generateTiles() {
        List<Tile> tileList = new ArrayList<>();
        for (int i = 0; i < LETTERS.size(); i++) {
            for (int j = 0; j < QUANTITIES[i]; j++) {
                tileList.add(new Tile(LETTERS.get(i), VALUES[i], i == JOKER_INDEX));
            }
        }
        return tileList;
    }

}
