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
package hu.undieb.kg.socotra.controller;

import hu.undieb.kg.socotra.model.ComputerPlayer;
import hu.undieb.kg.socotra.model.GameManager;
import hu.undieb.kg.socotra.model.GameObserver;
import hu.undieb.kg.socotra.model.LocalHumanPlayer;
import hu.undieb.kg.socotra.model.Player;
import hu.undieb.kg.socotra.model.Players;
import hu.undieb.kg.socotra.model.RemotePlayer;
import hu.undieb.kg.socotra.model.networking.GameClient;
import hu.undieb.kg.socotra.model.networking.GameEndPoint;
import hu.undieb.kg.socotra.model.networking.GameServer;
import hu.undieb.kg.socotra.model.persistence.ServerDAO;
import hu.undieb.kg.socotra.util.StringConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gergely Kadar
 */
public class GameInitializer {

    public static final int MAX_PLAYERS = 4;
    public static final int DEFAULT_THINKING_TIME = 180;
    public static final int DEFAULT_TIME_EXTENSIONS = 3;

    public enum PlayerType {
        HUMAN,
        COMPUTER,
        REMOTE
    }

    public class PlayerSlot {

        public final PlayerType playerType;
        public final Player player;
        private boolean free;

        public PlayerSlot(PlayerType playerType, Player player, boolean free) {
            this.playerType = playerType;
            this.player = player;
            this.free = free;
        }

        public boolean isFree() {
            return free;
        }
    }

    private List<PlayerSlot> playerSlots;
    private List<GameObserver> observers;
    private GameEndPoint gameEndPoint;

    public GameInitializer() {
        playerSlots = new ArrayList<>();
        observers = new ArrayList<>();
        gameEndPoint = null;
    }

//    public static GameWindowController initGame(InputStreamReader inputStream, List<String> playerNames, List<PlayerType> playerTypes)
//            throws FileNotFoundException {
//        
//        GameWindowController ctr = new GameWindowController();
//
//        List<Player> players = new ArrayList<>();
//        LocalHumanPlayer localHumanPlayer = null;
//        for (int i = 0; i < playerNames.size(); i++) {
//            switch (playerTypes.get(i)) {
//                case HUMAN:
////                    localHumanPlayer = new LocalHumanPlayer(playerNames.get(i), ctr);
////                    players.add(localHumanPlayer);
//                    break;
//                case COMPUTER:
//                    players.add(new ComputerPlayer(playerNames.get(i)));
//                    break;
//                case REMOTE:
//                    if (networkManager == null) {
//                        //networkManager = new NetworkManager(true);
//                    }
//                    RemotePlayer remotePlayer = new RemotePlayer(playerNames.get(i));
//                    players.add(remotePlayer);
//                    //networkManager.addRemotePlayer(remotePlayer);
//                    break;
//            }
//        }
//
//        // GameManager gameManager = new GameManager(inputStream, players);
//        // ctr.setGameManager(gameManager);
//        ctr.setPlayer(localHumanPlayer);
//
//        return ctr;
//    }
    public GameWindowController createGameWindowController(InputStreamReader inputStream) throws FileNotFoundException {
        List<Player> players = playerSlots.stream().map(s -> s.player).filter(p -> p != null).collect(Collectors.toList());
        GameManager gameManager = new GameManager(inputStream, new Players(players));
        LocalHumanPlayer humanPlayer = null;
        for (Player p : players) {
            if (p instanceof LocalHumanPlayer) {
                humanPlayer = (LocalHumanPlayer) p;
                break;
            }
        }
        if (humanPlayer == null) {
            return null;
        }
        gameEndPoint.setGameManager(gameManager);
        GameWindowController gameWindowController = new GameWindowController(gameManager, humanPlayer);
        observers.add(gameWindowController);
        gameManager.addObservers(observers);

        return gameWindowController;
    }

    public void addPlayerSlot(String name, PlayerType type) {
        PlayerSlot slot = new PlayerSlot(type, createPlayer(name, type), (type == PlayerType.REMOTE));
        playerSlots.add(slot);
    }

    public void removePlayerSlot(int index) {
        playerSlots.remove(index);
    }

    public void remotePlayerJoined(String name) {
        for (PlayerSlot ps : playerSlots) {
            if (ps.playerType == PlayerType.REMOTE && ps.free) {
                ps.player.setName(name);
                ps.free = false;
                break;
            }
        }
    }

    private Player createPlayer(String name, PlayerType type) {
        Player player = null;
        switch (type) {
            case HUMAN:
                player = new LocalHumanPlayer(name);
                break;
            case COMPUTER:
                player = new ComputerPlayer(name);
                break;
            case REMOTE:
                player = new RemotePlayer(name);
                break;
            default:
                break;
        }
        return player;
    }

    public void createServer(int port, LobbyController lobbyController) throws IOException {
        try {
            GameServer gameServer = new GameServer(port, lobbyController);
            observers.add(gameServer);
            gameEndPoint = gameServer;
        } catch (IOException e) {
            throw new IOException(StringConstants.SERVER_CREATION_FAILED_MSG, e.getCause());
        }
    }

    public void createClient(String host, int port) throws IOException {
        GameClient gameClient = new GameClient(host, port);
        observers.add(gameClient);
        gameEndPoint = gameClient;
    }

    public List<PlayerSlot> getPlayerSlots() {
        return playerSlots;
    }
}
