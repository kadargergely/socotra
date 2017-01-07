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

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gergely Kadar
 */
public class Bag {    

    private final Deque<Tile> BAG;

    public Bag(long seed) {
        List<Tile> tileList = Letters.generateTiles();
        Collections.shuffle(tileList, new Random(seed));
        BAG = new ArrayDeque<>(tileList);
    }

    public Tile getTile() {
        return BAG.pollFirst();
    }

    public int getNumOfTiles() {
        return BAG.size();
    }

    public void putBackTile(Tile tile) {
        BAG.addFirst(tile);
    }
}
