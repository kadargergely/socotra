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
package hu.unideb.kg.socotra.controller;

import hu.unideb.kg.socotra.model.GameManager;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.Players;
import hu.unideb.kg.socotra.model.networking.GameClient;
import hu.unideb.kg.socotra.model.networking.GameServer;
import hu.unideb.kg.socotra.util.StringConstants;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Gergely Kadar
 */
public class GameInitializer {

    public static final int MAX_PLAYERS = 4;
    public static final int DEFAULT_THINKING_TIME = 180;
    public static final int DEFAULT_TIME_EXTENSIONS = 3;

    private Players players;
    private GameManager gameManager;
//    private List<GameObserver> observers;
//    private GameEndPoint gameEndPoint;

    public GameInitializer(InputStreamReader inputStream) throws IOException {
        players = new Players();
        gameManager = new GameManager(inputStream, players);
//        observers = new ArrayList<>();
//        gameEndPoint = null;
    }

    public void addPlayer(String name, Player.PlayerType type) {
        Player player = new Player(name, type);
        players.add(player);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    public void remotePlayerJoined(String playerName) {
        addPlayer(playerName, Player.PlayerType.REMOTE);
    }

    public void remotePlayerLeft(String playerName) {
        players.remove(playerName);
    }

    public void finalizePlayers() {
        for (Player player : players.getPlayersList()) {
            if (player.getPlayerType() == Player.PlayerType.HUMAN) {
                GameWindowController gameWindowCtr = new GameWindowController(gameManager, player);
                gameManager.addObserver(gameWindowCtr);
            }
        }
    }

    public Players getPlayers() {
        return players;
    }

    public void createServer(int port, LobbyController lobbyController) throws IOException {
        try {
            GameServer gameServer = new GameServer(port, lobbyController, gameManager, players);
            gameManager.addObserver(gameServer);
        } catch (IOException e) {
            throw new IOException(StringConstants.SERVER_CREATION_FAILED_MSG, e.getCause());
        }
    }

    public void createClient(String host, int port, LobbyController lobbyController) throws IOException {
        GameClient gameClient = new GameClient(host, port, lobbyController, gameManager, players);
        gameManager.addObserver(gameClient);
    }

}
