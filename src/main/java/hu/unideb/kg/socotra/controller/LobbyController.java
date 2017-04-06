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

import hu.unideb.kg.socotra.SocotraApp;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.networking.GameEndPoint;
import hu.unideb.kg.socotra.model.persistence.DBConnectionException;
import hu.unideb.kg.socotra.model.persistence.PlayerEntity;
import hu.unideb.kg.socotra.model.persistence.ServerDAO;
import hu.unideb.kg.socotra.model.persistence.ServerEntity;
import hu.unideb.kg.socotra.util.AlertCreator;
import hu.unideb.kg.socotra.util.StringConstants;
import java.util.logging.Level;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class LobbyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @FXML
    private TableView playersTable;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private TextArea playerDataTextArea;
    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;

    public class PlayerInGame {

        private SimpleStringProperty name;
        private SimpleStringProperty type;
        private SimpleStringProperty status;

        public PlayerInGame(String name, String type, String status) {
            this.name = new SimpleStringProperty(name);
            this.status = new SimpleStringProperty(status);
            this.type = new SimpleStringProperty(type);
        }

        public String getName() {
            return name.get();
        }

        public String getType() {
            return type.get();
        }

        public String getStatus() {
            return status.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    private SocotraApp mainApp;
    private GameEndPoint endPoint;
    private GameInitializer gameInitializer;
    private ServerDAO serverDAO;
    private ServerEntity serverToConnect;

    private ObservableList<PlayerInGame> tableData;

    public LobbyController(GameInitializer gameInitializer, ServerDAO serverDAO, ServerEntity serverToConnect) {
        this.gameInitializer = gameInitializer;
        this.serverDAO = serverDAO;
        this.serverToConnect = serverToConnect;
        this.endPoint = null;
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableData = FXCollections.observableArrayList();
        playersTable.setItems(tableData);

        for (Player player : gameInitializer.getPlayers().getPlayersList()) {
            String name = player.getName();
            String type = "";
            switch (player.getPlayerType()) {
                case HUMAN:
                    type = StringConstants.LOCAL_PLAYER;
                    break;
                case COMPUTER:
                    type = StringConstants.COMPUTER;
                    break;
                case REMOTE:
                    type = StringConstants.REMOTE_PLAYER;
                    break;
            }
            String status = player.getPlayerType() == Player.PlayerType.REMOTE && player.getName().equals("<" + StringConstants.REMOTE_PLAYER + ">")
                    ? StringConstants.WAITING_FOR_PLAYER : StringConstants.CONNECTED;
            tableData.add(new PlayerInGame(name, type, status));
        }
        
        int loadedPlayers = (int) serverToConnect.getPlayers().stream().filter(p -> p.isLoaded()).count();
        for (int i = 0; i < serverToConnect.getAvailablePlaces() - loadedPlayers; i++) {
            tableData.add(new PlayerInGame("<" + StringConstants.REMOTE_PLAYER + ">", StringConstants.REMOTE_PLAYER, StringConstants.WAITING_FOR_PLAYER));
        }        
//        mainApp.getPrimaryStage().setOnCloseRequest((WindowEvent event) -> {
//            exitPressed();
//        });
    }

    @FXML
    private void exitPressed() {
        endPoint.playerLeft();
        closeWindow();
    }

    public void addRemotePlayerToGame(String playerName) {
        for (PlayerInGame p : tableData) {
            if (p.getName().equals(playerName) && p.getStatus().equals(StringConstants.WAITING_FOR_PLAYER)) {
                p.setStatus(StringConstants.CONNECTED);
                return;
            }
        }
        for (PlayerInGame p : tableData) {
            if (p.getType().equals(StringConstants.REMOTE_PLAYER) && p.getStatus().equals(StringConstants.WAITING_FOR_PLAYER)) {
                p.setName(playerName);
                p.setStatus(StringConstants.CONNECTED);
                gameInitializer.remotePlayerJoined(playerName);
                break;
            }
        }
    }

    public void removeRemotePlayerFromGame(String playerName) {
        for (PlayerInGame playerInGame : tableData) {
            if (playerInGame.getName().equals(playerName)) {
                for (PlayerEntity playerEntity : serverToConnect.getPlayers()) {
                    if (playerEntity.getName().equals(playerName)) {
                        if (!playerEntity.isLoaded()) {
                            playerInGame.setName("<" + StringConstants.REMOTE_PLAYER + ">");
                        }
                        playerInGame.setStatus(StringConstants.WAITING_FOR_PLAYER);
                        gameInitializer.remotePlayerLeft(playerName);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void addRemotePlayerToDatabase(String playerName, String password) {
        try {
            PlayerEntity playerToAdd = null;
            for (PlayerEntity player : serverToConnect.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    playerToAdd = player;
                    break;
                }
            }
            if (playerToAdd != null) {
                serverDAO.connectPlayer(playerToAdd);
            } else {
                playerToAdd = new PlayerEntity(0, serverToConnect, playerName, PlayerEntity.PlayerType.HUMAN, true, password, false);
                serverDAO.addPlayer(playerToAdd);
            }
            serverToConnect = serverDAO.getServerByName(serverToConnect.getName());
        } catch (DBConnectionException e) {
            LOGGER.warn("db error", e);
        }
    }

    public void removeRemotePlayerFromDatabase(String playerName) {
        try {
            for (PlayerEntity player : serverToConnect.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    LOGGER.debug("player found, and should be removed from DB");
                    if (player.isLoaded()) {
                        serverDAO.disconnectPlayer(player);
                    } else {
                        serverDAO.removePlayer(serverToConnect, player);
                    }
                    LOGGER.info("players on the server after remove:");
                    serverToConnect.getPlayers().forEach(p -> LOGGER.info("(" + p.getPlayerId() + ") " + p.getName()));
                    serverDAO.incrementAvailablePlaces(serverToConnect);
                    break;
                }
            }
            serverToConnect = serverDAO.getServerByName(serverToConnect.getName());
        } catch (DBConnectionException e) {
            LOGGER.warn("Failed to remove player with name " + playerName + " from database.", e);
        }
    }

    public void serverUnreachable() {
        AlertCreator.showErrorMessage(StringConstants.SERVER_UNREACHABLE_TITLE, StringConstants.SERVER_UNREACHABLE_MSG);
        closeWindow();
    }

    public void setEndPoint(GameEndPoint endPoint) {
        this.endPoint = endPoint;
    }

    private void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
