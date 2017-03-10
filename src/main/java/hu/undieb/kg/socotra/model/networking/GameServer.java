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
package hu.undieb.kg.socotra.model.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import hu.undieb.kg.socotra.controller.LobbyController;
import hu.undieb.kg.socotra.model.GameManager;
import hu.undieb.kg.socotra.model.GameObserver;
import hu.undieb.kg.socotra.model.Player;
import hu.undieb.kg.socotra.model.Players;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Gergely Kadar
 */
public class GameServer implements GameObserver, GameEndPoint {

    private Server server;
    private GameManager gameManager;
    private LobbyController lobbyController;
    private Players players;

    static class GameConnection extends Connection {

        public String PLAYER_NAME;
    }

    public GameServer(int port, LobbyController lobbyController) throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        server = new Server();
//        this.gameManager = gameManager;
//        this.players = players;

        NetworkManager.register(server);

        server.addListener(new Listener() {
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
                if (object instanceof NetworkManager.GameStarted) {
                    // Only the server sends this message, so if it comes from a client, we ignore it.
                    return;
                }
                if (object instanceof NetworkManager.RegisterPlayer) {
                    connection.PLAYER_NAME = ((NetworkManager.RegisterPlayer) object).PLAYER_NAME;
                    lobbyController.playerConnected(connection.PLAYER_NAME);
                }
            }
        });
        
        server.bind(port);
        server.start();
        
        this.lobbyController = lobbyController;
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
        NetworkManager.GameStarted gameStarted = new NetworkManager.GameStarted();
        gameStarted.BAG_SEED = bagSeed;
        server.sendToAllTCP(gameStarted);
    }
    
    @Override
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private void sendToAllExceptPlayer(String playerName, Object object) {
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
        }
    }
}
