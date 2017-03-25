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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hu.undieb.kg.socotra.controller.LobbyController;
import hu.undieb.kg.socotra.model.GameManager;
import hu.undieb.kg.socotra.model.GameObserver;
import hu.undieb.kg.socotra.model.Player;
import hu.undieb.kg.socotra.model.Players;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class GameClient implements GameObserver {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GameClient.class);

    private Client client;
    private GameManager gameManager;
    private LobbyController lobbyController;
    private String playerName;
    private Players players;

    private static final int TIMEOUT = 5000;

    public GameClient(String host, int port, GameManager gameManager, Players players) throws IOException {
        client = new Client();
        client.start();
        this.gameManager = gameManager;
        this.players = players;
        for (Player player : players.getPlayers()) {
            if (player.getPlayerType() == Player.PlayerType.HUMAN) {
                playerName = player.getName();
                break;
            }
        }

        NetworkManager.register(client);

        client.addListener(new Listener() {
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
                    
                }
            }
        });

        try {
            client.connect(TIMEOUT, host, port);
        } catch (IOException e) {
            LOGGER.error("Failed to connect to server at ip address " + host + ":" + port + ".", e);
            throw new IOException(e);
        }
    }

    @Override
    public void boardAltered(int row, int col, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStarted(long bagSeed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void trayAltered(int index, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
