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
package hu.unideb.kg.socotra.model.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hu.unideb.kg.socotra.controller.LobbyController;
import hu.unideb.kg.socotra.model.GameManager;
import hu.unideb.kg.socotra.model.GameObserver;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.Players;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class GameServer implements GameObserver, GameEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServer.class);

    private Server server;
    private GameManager gameManager;
    private LobbyController lobbyController;
    private Players players;

    private LobbyListener lobbyListener;
    private GameListener gameListener;

    static class GameConnection extends Connection {

        public String PLAYER_NAME;
    }

    class LobbyListener extends Listener {

        @Override
        public void received(Connection c, Object object) {
            GameConnection connection = (GameConnection) c;
            if (object instanceof NetworkManager.RegisterPlayer) {
                String newPlayerName = ((NetworkManager.RegisterPlayer) object).PLAYER_NAME;
                String password = ((NetworkManager.RegisterPlayer) object).PASSWORD;
                connection.PLAYER_NAME = newPlayerName;
                lobbyController.addRemotePlayerToGame(newPlayerName);
                lobbyController.addRemotePlayerToDatabase(newPlayerName, password);
                sendToAllExceptPlayer(newPlayerName, object);
            }
        }

        @Override
        public void disconnected(Connection c) {
            GameConnection connection = (GameConnection) c;
            if (connection.PLAYER_NAME != null) {
                LOGGER.info("disconnected");
                lobbyController.removeRemotePlayerFromGame(connection.PLAYER_NAME);
                lobbyController.removeRemotePlayerFromDatabase(connection.PLAYER_NAME);
                NetworkManager.PlayerLeft playerLeft = new NetworkManager.PlayerLeft();
                playerLeft.PLAYER_NAME = connection.PLAYER_NAME;
                sendToAllExceptPlayer(connection.PLAYER_NAME, playerLeft);
            }
        }
    }

    class GameListener extends Listener {

        @Override
        public void received(Connection c, Object object) {
            GameConnection connection = (GameConnection) c;

            if (object instanceof NetworkManager.BoardAltered) {
                NetworkManager.BoardAltered boardAltered = (NetworkManager.BoardAltered) object;
                Player player = GameServer.this.players.getPlayerByName(boardAltered.PLAYER_NAME);
                if (player != null && GameServer.this.gameManager != null) {
                    GameServer.this.gameManager.alterBoard(boardAltered.ROW, boardAltered.COL, player);
                }
                return;
            }
            if (object instanceof NetworkManager.TrayAltered) {
                NetworkManager.TrayAltered trayAltered = (NetworkManager.TrayAltered) object;
                Player player = GameServer.this.players.getPlayerByName(trayAltered.PLAYER_NAME);
                if (player != null) {
                    player.alterTray(trayAltered.INDEX);
                }
                return;
            }
            if (object instanceof NetworkManager.TurnEnded) {
                NetworkManager.TurnEnded turnEnded = (NetworkManager.TurnEnded) object;
                Player player = GameServer.this.players.getPlayerByName(turnEnded.PLAYER_NAME);
                if (player != null && GameServer.this.gameManager != null) {
                    GameServer.this.gameManager.endTurn(GameManager.TurnAction.valueOf(turnEnded.TURN_ACTION), player);
                }
                return;
            }
        }
    }

    public GameServer(int port, LobbyController lobbyController, GameManager gameManager, Players players) throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new GameConnection();
            }
        };

        this.gameManager = gameManager;
        this.players = players;
        this.lobbyListener = new LobbyListener();
        this.gameListener = new GameListener();

        NetworkManager.register(server);

        server.addListener(lobbyListener);
        server.bind(port);
        server.start();

        this.lobbyController = lobbyController;
        lobbyController.setEndPoint(this);
    }

    @Override
    public void trayAltered(int index, Player player) {
        NetworkManager.TrayAltered trayAltered = new NetworkManager.TrayAltered();
        trayAltered.INDEX = index;
        sendToAllExceptPlayer(player.getName(), trayAltered);
    }

    @Override
    public void boardAltered(int row, int col, Player player) {
        NetworkManager.BoardAltered boardAltered = new NetworkManager.BoardAltered();
        boardAltered.ROW = row;
        boardAltered.COL = col;
        sendToAllExceptPlayer(player.getName(), boardAltered);
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        NetworkManager.TurnEnded turnEnded = new NetworkManager.TurnEnded();
        turnEnded.TURN_ACTION = action.toString();
        sendToAllExceptPlayer(player.getName(), turnEnded);
    }

    @Override
    public void gameStarted(long bagSeed) {
        server.removeListener(lobbyListener);
        server.addListener(gameListener);
        NetworkManager.GameStarted gameStarted = new NetworkManager.GameStarted();
        gameStarted.BAG_SEED = bagSeed;
        server.sendToAllTCP(gameStarted);
    }

    @Override
    public void playerLeft() {
        server.stop();
    }

    void sendToAllExceptPlayer(String playerName, Object object) {
        Connection[] connections = server.getConnections();
        Connection excludedConn = null;
        for (Connection c : connections) {
            if (((GameConnection) c).PLAYER_NAME.equals(playerName)) {
                excludedConn = c;
                break;
            }
        }
        if (excludedConn != null) {
            server.sendToAllExceptTCP(excludedConn.getID(), object);
        } else {
            server.sendToAllTCP(object);
        }
    }
}
