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
    private GameListener gameListener;
    private Players players;

    private static final int TIMEOUT = 5000;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;

    class LobbyListener extends Listener {

        AtomicInteger reconnectCount = new AtomicInteger();

        @Override
        public void connected(Connection connection) {
            NetworkManager.RegisterPlayer registerPlayer = new NetworkManager.RegisterPlayer();
            registerPlayer.PLAYER_NAME = players.getLocalPlayerName();
            client.sendTCP(registerPlayer);
        }

        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof NetworkManager.RegisterPlayer) {
                String newPlayerName = ((NetworkManager.RegisterPlayer) object).PLAYER_NAME;
                lobbyController.addRemotePlayerToGame(newPlayerName);
                return;
            }
            if (object instanceof NetworkManager.PlayerLeft) {
                lobbyController.removeRemotePlayerFromGame(((NetworkManager.PlayerLeft) object).PLAYER_NAME);
                return;
            }
            if (object instanceof NetworkManager.GameStarted) {
                lobbyController.startGame(((NetworkManager.GameStarted) object).SHUFFLE_SEED);
                return;
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

    class GameListener extends Listener {

        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof NetworkManager.PlayerLeft) {
                gameManager.remotePlayerLeft(GameClient.this.players.getPlayerByName(((NetworkManager.PlayerLeft) object).PLAYER_NAME));
                return;
            }
            if (object instanceof NetworkManager.ThinkingTimeExtended) {
                NetworkManager.ThinkingTimeExtended thinkingTimeExtended = (NetworkManager.ThinkingTimeExtended) object;
                gameManager.extendThinkingTime(players.getPlayerByName(thinkingTimeExtended.PLAYER_NAME));
                return;
            }
            if (object instanceof NetworkManager.BoardAltered) {
                NetworkManager.BoardAltered boardAltered = (NetworkManager.BoardAltered) object;
                gameManager.alterBoard(boardAltered.ROW, boardAltered.COL, players.getPlayerByName(boardAltered.PLAYER_NAME));
                return;
            }
            if (object instanceof NetworkManager.TrayAltered) {
                NetworkManager.TrayAltered trayAltered = (NetworkManager.TrayAltered) object;
                gameManager.alterTray(trayAltered.INDEX, players.getPlayerByName(trayAltered.PLAYER_NAME));
                return;
            }
            if (object instanceof NetworkManager.TurnEnded) {
                NetworkManager.TurnEnded turnEnded = (NetworkManager.TurnEnded) object;
                gameManager.endTurn(GameManager.TurnAction.valueOf(turnEnded.TURN_ACTION), players.getPlayerByName(turnEnded.PLAYER_NAME));
                return;
            }
            if (object instanceof NetworkManager.JokerLetterChosen) {
                NetworkManager.JokerLetterChosen jokerLetterChosen = (NetworkManager.JokerLetterChosen) object;
                players.getPlayerByName(jokerLetterChosen.PLAYER_NAME).getTileInHand().setJokerLetter(jokerLetterChosen.LETTER);
                return;
            }
        }

        @Override
        public void disconnected(Connection connection) {
            LOGGER.error("Lost connection to server.");
            new Thread(() -> {
                client.stop();
            }).start();
            gameManager.serverLeft();
        }
    }

    public GameClient(String host, int port, LobbyController lobbyController, GameManager gameManager, Players players) throws IOException {
        client = new Client();
        this.gameManager = gameManager;
        this.players = players;
        this.lobbyController = lobbyController;
        this.lobbyListener = new LobbyListener();
        this.gameListener = new GameListener();

        NetworkManager.register(client);

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
        if (player.getName().equals(players.getLocalPlayerName())) {
            NetworkManager.BoardAltered boardAltered = new NetworkManager.BoardAltered();
            boardAltered.PLAYER_NAME = player.getName();
            boardAltered.ROW = row;
            boardAltered.COL = col;
            client.sendTCP(boardAltered);
        }
    }

    @Override
    public void trayAltered(int index, Player player) {
        if (player.getName().equals(players.getLocalPlayerName())) {
            NetworkManager.TrayAltered trayAltered = new NetworkManager.TrayAltered();
            trayAltered.PLAYER_NAME = player.getName();
            trayAltered.INDEX = index;
            client.sendTCP(trayAltered);
        }
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        if (player.getName().equals(players.getLocalPlayerName())) {
            NetworkManager.TurnEnded turnEnded = new NetworkManager.TurnEnded();
            turnEnded.PLAYER_NAME = player.getName();
            turnEnded.TURN_ACTION = action.toString();
            client.sendTCP(turnEnded);
        }
    }

    @Override
    public void thinkingTimeOver(Player player) {
        if (player.getName().equals(players.getLocalPlayerName())) {
            NetworkManager.TurnEnded turnEnded = new NetworkManager.TurnEnded();
            turnEnded.PLAYER_NAME = player.getName();
            turnEnded.TURN_ACTION = GameManager.TurnAction.PASS.toString();
            client.sendTCP(turnEnded);
            gameManager.endTurn(GameManager.TurnAction.PASS, player);
        }
    }

    @Override
    public String jokerLetterRequested(Player player) {
        return null;
    }

    @Override
    public void jokerLetterChosen(String letter, Player player) {
        NetworkManager.JokerLetterChosen jokerLetterChosen = new NetworkManager.JokerLetterChosen();
        jokerLetterChosen.PLAYER_NAME = player.getName();
        jokerLetterChosen.LETTER = letter;
        client.sendTCP(jokerLetterChosen);
    }

    @Override
    public void localPlayerLeft(Player player) {
        client.stop();
    }

    @Override
    public void remotePlayerLeft(Player player) {

    }

    @Override
    public void serverLeft() {

    }

    @Override
    public void playerLeft() {
        client.stop();
    }

    @Override
    public void gameStarted(Player firstPlayer) {

    }

    @Override
    public void updateTimer(int remainingTime) {

    }

    @Override
    public void thinkingTimeExtended(Player player) {
        NetworkManager.ThinkingTimeExtended thinkingTimeExtended = new NetworkManager.ThinkingTimeExtended();
        thinkingTimeExtended.PLAYER_NAME = player.getName();
        client.sendTCP(thinkingTimeExtended);
    }

    @Override
    public void switchToGameListener() {
        client.removeListener(lobbyListener);
        client.addListener(gameListener);
    }

}
