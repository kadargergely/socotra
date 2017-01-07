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

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The <code>Dictionary</code> class wraps a list of {@link DictWord} objects containing the words
 * that can be played during the game. Provides a method for checking if the list contains a given
 * word, and a method that returns the words that can be formed using the given letters.
 *
 * @author Gergely Kadar
 *
 */
public class Dictionary {

    private final List<DictWord> DICTIONARY = new ArrayList<>();

    /**
     * Constructor for creating a <code>Dictionary</code> object based on a dictionary file. The
     * wrapped list of {@link DictWord} objects is filled.
     *
     * @param inputStream a dictionary file, containing words
     * @throws FileNotFoundException if the dictionary file couldn't be found
     */
    public Dictionary(InputStreamReader inputStream) throws FileNotFoundException {
        try (Scanner in = new Scanner(inputStream)) {
            String line;
            while (in.hasNext()) {
                line = in.nextLine();
                String word = (line.split("\\s+")[0]).split("/")[0];
                word = word.trim();

                DictWord dword = DictWord.newWord(word);
                if (dword != null) {
                    DICTIONARY.add(dword);
                }
            }

            Collections.sort(DICTIONARY, 
                    (DictWord o1, DictWord o2) -> Integer.compare(o1.getWord().length(), 
                            o2.getWord().length()));
        }
    }
    
    /**
     * Returns the words as a list of strings, that can be formed using the
     * given letters. The letters are given in the form of a
     * <code>DictWord</code> object.
     * 
     * @param letters
     *            the letters, represented as a <code>DictWord</code> object
     * @return the list of the playable words, as a list of string lists
     */
    public List<List<String>> getPlayableWords(DictWord letters) {
        List<List<String>> retList = new ArrayList<>();

        DICTIONARY.stream().filter((word) -> (word.playable(letters))).forEach((word) -> {
            retList.add(word.toStringList());
        });

        return retList;
    }
    
    /**
     * Tests if the dictionary includes the given word.
     * 
     * @param word
     *            the word, represented as a <code>String</code>
     * @return <code>true</code> if the word is part of the dictionary,
     *         <code>false</code> otherwise
     */
    public boolean isCorrect(String word) {
        return DICTIONARY.stream().anyMatch((dword) -> (dword.equals(word.toLowerCase())));
    }
}
