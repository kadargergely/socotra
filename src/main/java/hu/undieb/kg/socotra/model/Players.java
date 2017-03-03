/*
 * Copyright (C) 2017 Gergely Kadar
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

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gergely Kadar
 */
public class Players {
    
    private List<Player> players;
    private int currentPlayer = 0;
    
    public Players(List<Player> players) {
        this.players = players;
    }
    
    public Player getPlayerByName(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        return null;
    }
    
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }
    
    public void next() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }
    
    public int getNumOfPlayers() {
        return players.size();
    }
    
    public List<String> getPlayerNames() {
        return players.stream().map(p -> p.getName()).collect(Collectors.toList());
    }
    
    public List<Integer> getPlayerScores() {
        return players.stream().map(p -> p.getScore()).collect(Collectors.toList());
    }
}
