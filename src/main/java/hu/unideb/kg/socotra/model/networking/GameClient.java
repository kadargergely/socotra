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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hu.unideb.kg.socotra.controller.LobbyController;
import hu.unideb.kg.socotra.model.GameManager;
import hu.unideb.kg.socotra.model.GameObserver;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.Players;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class GameClient implements GameObserver, GameEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);

    private Client client;
    private GameManager gameManager;
    private LobbyController lobbyController;
    private LobbyListener lobbyListener;
    private String playerName;
    private Players players;

    private static final int TIMEOUT = 5000;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    class LobbyListener extends Listener {

        AtomicInteger reconnectCount = new AtomicInteger();

        @Override
        public void connected(Connection connection) {
            NetworkManager.RegisterPlayer registerPlayer = new NetworkManager.RegisterPlayer();
            registerPlayer.PLAYER_NAME = playerName;
            client.sendTCP(registerPlayer);
        }

        @Override
        public void received(Connection connection, Object object) {
            Player player = players.getPlayerByName(playerName);

            if (object instanceof NetworkManager.RegisterPlayer) {
                String newPlayerName = ((NetworkManager.RegisterPlayer) object).PLAYER_NAME;
                lobbyController.addRemotePlayerToGame(newPlayerName);
            }
            if (object instanceof NetworkManager.PlayerLeft) {
                lobbyController.removeRemotePlayerFromGame(((NetworkManager.PlayerLeft) object).PLAYER_NAME);
            }
        }

        @Override
        public void disconnected(Connection connection) {
            if (reconnectCount.getAndIncrement() == MAX_RECONNECT_ATTEMPTS) {
                LOGGER.warn("Client failed to reconnect after the maximum number of attempts.");
                new Thread(() -> {
                    client.stop();
                }).start();
                Platform.runLater(() -> lobbyController.serverUnreachable());
            } else {
                new Thread(() -> {
                    try {
                        LOGGER.info("Disconnected from server, trying to reconnect: " + reconnectCount.get());
                        client.reconnect();
                        LOGGER.info("Successfully reconnected to server.");
                        reconnectCount.set(0);
                    } catch (IOException e) {
                        try {
                            LOGGER.info("Failed to reconnect.", e);
                            Thread.sleep(3000);
                            disconnected(connection);
                        } catch (InterruptedException ex) {
                            LOGGER.error("interrupted", ex);
                        }
                    }
                }).start();
            }
        }
    }

    public GameClient(String host, int port, LobbyController lobbyController, GameManager gameManager, Players players) throws IOException {
        client = new Client();        
        this.gameManager = gameManager;
        this.players = players;

        this.lobbyController = lobbyController;
        lobbyController.setEndPoint(this);

        for (Player player : players.getPlayersList()) {
            if (player.getPlayerType() == Player.PlayerType.HUMAN) {
                playerName = player.getName();
                break;
            }
        }

        NetworkManager.register(client);

        this.lobbyListener = new LobbyListener();
        client.addListener(lobbyListener);

        new Thread(() -> {
            try {
                client.start();
                client.connect(TIMEOUT, host, port);
            } catch (IOException e) {
                LOGGER.error("Failed to connect to server at ip address " + host + ":" + port + ".", e);
//                throw new IOException(e);
            }
        }).start();

    }

    @Override
    public void boardAltered(int row, int col, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStarted(long bagSeed) {

    }

    @Override
    public void trayAltered(int index, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playerLeft() {
        client.stop();
    }

}
