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

import hu.unideb.kg.socotra.model.networking.NetworkManager;
import hu.unideb.kg.socotra.model.persistence.DBConnectionException;
import hu.unideb.kg.socotra.model.persistence.PlayerEntity;
import hu.unideb.kg.socotra.model.persistence.ServerDAO;
import hu.unideb.kg.socotra.model.persistence.ServerEntity;
import hu.unideb.kg.socotra.util.AlertCreator;
import hu.unideb.kg.socotra.util.StringConstants;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Gergely Kadar
 */
public class JoinServerController {

    @FXML
    private Button addServerButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableColumn playerStatusColumn;
    @FXML
    private TableColumn playerTypeColumn;
    @FXML
    private TableColumn serverNameColumn;
    @FXML
    private TableColumn playersColumn;
    @FXML
    private TableColumn playerNameColumn;
    @FXML
    private TableView availableServersTable;
    @FXML
    private TableView playersTable;
    @FXML
    private TextField extendTimeField;
    @FXML
    private TextField serverNameField;
    @FXML
    private TextField timeField;

    public class AvailableServer {

        private SimpleStringProperty name;
        private SimpleStringProperty players;

        public AvailableServer(String name, String players) {
            this.name = new SimpleStringProperty(name);
            this.players = new SimpleStringProperty(players);
        }

        public String getName() {
            return name.get();
        }

        public String getPlayers() {
            return players.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setPlayers(String players) {
            this.players.set(players);
        }
    }

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
    }

    private ObservableList<AvailableServer> availableServers;
    private List<ServerEntity> serverEntities;
    private ObservableList<PlayerInGame> playersInGame;

    private SocotraApp mainApp;
    private ServerDAO serverDAO;
    private GameInitializer gameInitializer;

    public JoinServerController(SocotraApp mainApp, InputStreamReader inputStream, ServerDAO serverDAO)
            throws IOException {
        this.mainApp = mainApp;
        this.serverDAO = serverDAO;
        this.gameInitializer = new GameInitializer(inputStream);
    }

    @FXML
    private void initialize() {
        serverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playersColumn.setCellValueFactory(new PropertyValueFactory<>("players"));

        availableServers = FXCollections.observableArrayList();
        availableServersTable.setItems(availableServers);

        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        playerStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        playersInGame = FXCollections.observableArrayList();
        playersTable.setItems(playersInGame);

        availableServersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ServerEntity selectedServer = getServerEntityByName(((AvailableServer) newValue).getName());
                setServerData(selectedServer);
                connectButton.setDisable(false);
            }
        });

        connectButton.setDisable(true);

        refreshAvailableServersList();
    }

    private void refreshAvailableServersList() {
        try {
            availableServers.clear();
            serverEntities = serverDAO.getAvailableServers();
            serverEntities.forEach(server -> availableServers.add(serverEntityToAvailableServer(server)));
        } catch (DBConnectionException ex) {
            AlertCreator.showErrorMessage(StringConstants.FAILED_TO_GET_SERVERS_TITLE, StringConstants.FAILED_TO_GET_SERVERS_MSG);
        }
    }

    private void setServerData(ServerEntity server) {
        serverNameField.setText(server.getName());

        if (server.getThinkingTime() != null) {
            timeField.setDisable(false);
            extendTimeField.setDisable(false);
            timeField.setText(String.valueOf(server.getThinkingTime()));
            extendTimeField.setText(String.valueOf(server.getTimeExtensions()));
        } else {
            timeField.setDisable(true);
            extendTimeField.setDisable(true);
        }

        playersInGame.clear();
        for (PlayerEntity player : server.getPlayers()) {
            String playerType;
            switch (player.getType()) {
                case COMPUTER:
                    playerType = StringConstants.COMPUTER;
                    break;
                default:
                    playerType = StringConstants.HUMAN;
            }
            if (player.isConnected() || player.getPassword() != null) {
                playersInGame.add(new PlayerInGame(player.getName(), playerType,
                        player.isConnected() ? StringConstants.SERVER_PLAYER_STATUS_CONNECTED : StringConstants.SERVER_PLAYER_STATUS_FREE));
            }
        }
    }

    private ServerEntity getServerEntityByName(String name) {
        return serverEntities.stream().filter(s -> s.getName().equals(name)).findAny().get();
    }

    private AvailableServer serverEntityToAvailableServer(ServerEntity server) {
        int playerCount = server.getPlayers().size();
        int availablePlayerCount = server.getAvailablePlaces();
        int loadedCount = (int) server.getPlayers().stream().filter(p -> p.isLoaded()).count();
        String players = String.valueOf(availablePlayerCount) + "/" + String.valueOf(playerCount + availablePlayerCount - loadedCount);
        return new AvailableServer(server.getName(), players);
    }

    @FXML
    private void refreshButtonPressed() {
        refreshAvailableServersList();
    }

    @FXML
    private void addServerButtonPressed() {
        String serverName = AlertCreator.showInputDialog(StringConstants.ADD_SERVER_TITLE, StringConstants.ADD_SERVER_TEXT);        
        if (serverName != null) {
            try {
                ServerEntity server = serverDAO.getServerByName(serverName);
                if (server != null && server.getPrivateServer()) {
                    serverEntities.add(server);
                    availableServers.add(serverEntityToAvailableServer(server));
                } else {
                    AlertCreator.showErrorMessage(StringConstants.PRIVATE_SERVER_DOESNT_EXIST_TITLE, StringConstants.PRIVATE_SERVER_DOESNT_EXIST_MSG);
                }
            } catch (DBConnectionException e) {
                AlertCreator.showErrorMessage(StringConstants.FAILED_TO_GET_SERVERS_TITLE, StringConstants.FAILED_TO_GET_SERVERS_MSG);
            }
        }
    }

    @FXML
    private void connectButtonPressed() {
        try {
            int selectedServerIndex = availableServersTable.getSelectionModel().getSelectedIndex();
            ServerEntity selectedServerRefreshed = serverDAO.getServerByName(availableServers.get(selectedServerIndex).getName());
            availableServers.set(selectedServerIndex, serverEntityToAvailableServer(selectedServerRefreshed));
            setServerData(selectedServerRefreshed);
            if (selectedServerRefreshed != null && selectedServerRefreshed.getAvailablePlaces() > 0) {
                serverDAO.decrementAvailablePlaces(selectedServerRefreshed);
                selectedServerRefreshed = serverDAO.getServerByName(selectedServerRefreshed.getName());
                ChoosePlayerController choosePlayerCtr = new ChoosePlayerController(mainApp, serverDAO, selectedServerRefreshed, gameInitializer);
                mainApp.showChoosePlayerWindow(choosePlayerCtr);
            } else {
                AlertCreator.showErrorMessage(StringConstants.CANT_JOIN_SERVER_TITLE, StringConstants.CANT_JOIN_SERVER_MSG);
                availableServers.remove(selectedServerIndex);
            }
        } catch (DBConnectionException ex) {

        }
    }
}
