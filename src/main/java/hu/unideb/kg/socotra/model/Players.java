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
package hu.unideb.kg.socotra.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    public Players() {
        this(new ArrayList<>());
    }

    public Player getPlayerByName(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        return null;
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                players.remove(player);
                break;
            }
        }
    }
    
    public void remove(Player player) {
        players.remove(player);
    }

    public boolean contains(String playerName) {
        return players.stream()
                .filter(p -> p.getName().equals(playerName) && p.getPlayerType() == Player.PlayerType.REMOTE)
                .findAny().isPresent();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public void next() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }

    public void setJoinedPlayerName(String playerName) {
        for (Player player : players) {
            if (player.getPlayerType() == Player.PlayerType.REMOTE) {
                player.setName(playerName);
                break;
            }
        }
    }

    public int getNumOfPlayers() {
        return players.size();
    }
    
    public String getLocalPlayerName() {
        String playerName = null;
        for (Player player : players) {
            if (player.getPlayerType() == Player.PlayerType.HUMAN) {
                playerName = player.getName();
                break;
            }
        }
        return playerName;
    }

    public List<String> getPlayerNames() {
        return players.stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    public List<Integer> getPlayerScores() {
        return players.stream().map(p -> p.getScore()).collect(Collectors.toList());
    }

    public List<Player> getPlayersList() {
        return players;
    }
    
    public void sortThenShufflePlayers(long seed) {
        Collections.sort(players, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        Collections.shuffle(players, new Random(seed));
    }
}
