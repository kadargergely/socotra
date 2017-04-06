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
 *
 * @author Gergely Kadar
 */
public class DictWord {

    private final String word;
    private final byte[] auxData;

    /**
     * Returns a new {@code DictWord} object which wraps the string given. If the string contains
     * illegal characters (characters that are not lower case letters in the Hungarian language),
     * the method returns {@code null}.
     *
     * @param word the word that will be packed
     * @return a reference to the newly created {@code DictWord} object or {@code null} if the word
     * contains illegal characters
     */
    public static DictWord newWord(String word) {

        boolean legal = true;
        for (int i = 0; i < word.length(); i++) {
            if (!Letters.isValid(String.valueOf(word.charAt(i)))) {
                if (!(word.charAt(i) == 'y' && i > 0 && (word.charAt(i - 1) == 'g'
                        || word.charAt(i - 1) == 'l'
                        || word.charAt(i - 1) == 'n' || word.charAt(i - 1) == 't'))) {
                    legal = false;
                }
            }
        }
        if (legal) {
            return new DictWord(word);
        } else {
            return null;
        }
    }

    private DictWord(String word) {

        this.word = word;
        auxData = new byte[Letters.getNumOfLetters()];

        for (int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'c':
                case 'z':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 's') {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)))] += 1;
                        i++;
                    } else {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i)))] += 1;
                    }
                    break;
                case 'g':
                case 'l':
                case 'n':
                case 't':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 'y') {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)))] += 1;
                        i++;
                    } else {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i)))] += 1;
                    }
                    break;
                case 's':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 'z') {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)))] += 1;
                        i++;
                    } else {
                        auxData[Letters.getLetterNum(String.valueOf(word.charAt(i)))] += 1;
                    }
                    break;
                default:
                    int index = Letters.getLetterNum(String.valueOf(word.charAt(i)));
                    if (index != -1) {
                        auxData[index] += 1;
                    }
                    break;
            }
        }
    }

    /**
     * Returns a list of strings containing the letters that form the word wrapped by the
     * {@code DictWord} object. Needed because of letters with two characters.
     *
     * @return a list of the letters of the wrapped word
     */
    public List<String> toStringList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            switch (word.charAt(i)) {
                case 'c':
                case 'z':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 's') {
                        list.add(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)));
                        i++;
                    } else {
                        list.add(String.valueOf(word.charAt(i)));
                    }
                    break;
                case 'g':
                case 'l':
                case 'n':
                case 't':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 'y') {
                        list.add(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)));
                        i++;
                    } else {
                        list.add(String.valueOf(word.charAt(i)));
                    }
                    break;
                case 's':
                    if (i < word.length() - 1 && word.charAt(i + 1) == 'z') {
                        list.add(String.valueOf(word.charAt(i))
                                + String.valueOf(word.charAt(i + 1)));
                        i++;
                    } else {
                        list.add(String.valueOf(word.charAt(i)));
                    }
                    break;
                default:
                    list.add(String.valueOf(word.charAt(i)));
                    break;
            }
        }

        return list;
    }

    /**
     * Returns {@code true} if the word wrapped by the {@code DictWord} object given as parameter
     * equals the word wrapped by this object.
     *
     * @param other the {@code DictWord} object which will be compared with this
     * @return {@code true} if the wrapped values are the same, {@code false} otherwise
     */
    public boolean equals(DictWord other) {
        return this.word.equals(other.word);
    }

    /**
     * Returns {@code true} if the wrapped string by this object equals the string given.
     *
     * @param word the string that will be compared with the wrapped value of this object
     * @return {@code true} if the wrapped value equals the string given, {@code false} otherwise
     */
    public boolean equals(String word) {
        return this.word.equals(word);
    }

    /**
     * Returns {@code true} if the word on which is called can be formed using the letters given.
     *
     * @param letters the letters that should be able to form the word
     * @return {@code true} if the word can be formed using the letter, {@code false} otherwise
     */
    public boolean playable(DictWord letters) {
        boolean retVal = true;
        int jokers = letters.getJokers();

        for (int i = 0; i < letters.auxData.length; i++) {
            if (this.auxData[i] > letters.auxData[i]) {
                if (this.auxData[i] - letters.auxData[i] <= jokers) {
                    jokers -= this.auxData[i] - letters.auxData[i];
                } else {
                    retVal = false;
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * Returns the number of joker tiles in the word.
     *
     * @return the number of joker tiles in the word
     */
    public int getJokers() {
        return auxData[Letters.JOKER_INDEX];
    }

    /**
     * Returns an array that stores the number of letters in the word for each tile code. The array
     * is in fact a copy of the auxiliary data that the object uses, this way it is modifiable
     * without modifying the object's data.
     *
     * @return an array that stores the number of letters in the word for each tile code
     */
    public byte[] getNumOfLetters() {
        return auxData.clone();
    }

    /**
     * Returns the word wrapped by this class as a string.
     *
     * @return the word wrapped by this class as a string
     */
    public String getWord() {
        return word;
    }
}
