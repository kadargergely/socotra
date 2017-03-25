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

import hu.undieb.kg.socotra.SocotraApp;
import hu.undieb.kg.socotra.model.networking.NetworkManager;
import hu.undieb.kg.socotra.model.networking.NetworkUtils;
import hu.undieb.kg.socotra.model.persistence.DBConnectionException;
import hu.undieb.kg.socotra.model.persistence.ExistingServerNameException;
import hu.undieb.kg.socotra.model.persistence.PlayerEntity;
import hu.undieb.kg.socotra.model.persistence.ServerDAO;
import hu.undieb.kg.socotra.model.persistence.ServerDAOImpl;
import hu.undieb.kg.socotra.model.persistence.ServerEntity;
import hu.undieb.kg.socotra.util.AlertCreator;
import hu.undieb.kg.socotra.util.StringConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Gergely Kadar
 */
public class NewGameController {

    @FXML
    private TextField serverNameField;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField timeExtensionsField;
    @FXML
    private CheckBox privateServerCheckBox;
    @FXML
    private CheckBox limitedTimeCheckBox;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button addPlayerButton;
    @FXML
    private Button removePlayerButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView playersTable;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private GridPane serverSettingsPane;

    public class AddedPlayer {

        private final SimpleStringProperty NAME;
        private final SimpleStringProperty TYPE;
        private final String PASSWORD;
        private final boolean DEFINED;

        public AddedPlayer(String name, String type, String password, boolean defined) {
            this.NAME = new SimpleStringProperty(name);
            this.TYPE = new SimpleStringProperty(type);
            this.PASSWORD = password;
            this.DEFINED = defined;
        }

        public AddedPlayer(String name, String type, boolean defined) {
            this(name, type, null, defined);
        }

        public AddedPlayer(String name, String type, String password) {
            this(name, type, password, true);
        }

        public AddedPlayer(String name, String type) {
            this(name, type, null, true);
        }

        public String getName() {
            return NAME.get();
        }

        public String getType() {
            return TYPE.get();
        }

        public String getPassword() {
            return PASSWORD;
        }

        public boolean isDefined() {
            return DEFINED;
        }

    }

    public class AddPlayerException extends Exception {

        public AddPlayerException() {
            super();
        }

        public AddPlayerException(String message) {
            super(message);
        }
    }

    private static final int MAX_NUM_OF_PLAYERS = 4;
    private static final int MIN_NUM_OF_PLAYERS = 2;

    private ObservableList<AddedPlayer> tableData;
    private int remotePlayers;
    private boolean multiplayer;

    private SocotraApp mainApp;
    private GameInitializer gameInitializer;
    private ServerDAO serverDAO;

    public NewGameController(SocotraApp mainApp, boolean multiplayer) {
        this.mainApp = mainApp;
        if (multiplayer == false) {
            serverDAO = null;
        } else {
            serverDAO = new ServerDAOImpl();
        }
        gameInitializer = new GameInitializer();
        remotePlayers = 0;
        this.multiplayer = multiplayer;
    }

    public NewGameController(SocotraApp mainApp, boolean multiplayer, ServerDAO serverDAO) {
        this.mainApp = mainApp;
        this.serverDAO = serverDAO;
        gameInitializer = new GameInitializer();
        remotePlayers = 0;
        this.multiplayer = multiplayer;
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));

        tableData = FXCollections.observableArrayList();
        playersTable.setItems(tableData);

        if (!multiplayer) {
            serverSettingsPane.setDisable(true);
        } else {
            serverPortField.setText(String.valueOf(NetworkManager.DEFAULT_PORT));
        }

        removePlayerButton.setDisable(true);
        playersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                removePlayerButton.setDisable(false);
            } else {
                removePlayerButton.setDisable(true);
            }
        });

        timeField.setDisable(!limitedTimeCheckBox.isSelected());
        timeExtensionsField.setDisable(!limitedTimeCheckBox.isSelected());

        // enforce numeric values in certain fields
        ChangeListener<String> enforceNumericValues = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                serverPortField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        };
        serverPortField.textProperty().addListener(enforceNumericValues);
        timeField.textProperty().addListener(enforceNumericValues);
        timeExtensionsField.textProperty().addListener(enforceNumericValues);

        forwardButton.setDisable(true);
    }

    @FXML
    private void limitedTimePressed() {
        timeField.setDisable(!limitedTimeCheckBox.isSelected());
        timeExtensionsField.setDisable(!limitedTimeCheckBox.isSelected());
        if (!limitedTimeCheckBox.isSelected()) {
            timeField.clear();
            timeExtensionsField.clear();
        } else {
            timeField.setText(String.valueOf(GameInitializer.DEFAULT_THINKING_TIME));
            timeExtensionsField.setText(String.valueOf(GameInitializer.DEFAULT_TIME_EXTENSIONS));
        }
    }

    @FXML
    private void addPlayerPressed() {
        boolean localHuman = tableData.stream()
                .map(p -> p.getType())
                .filter(t -> t.equals(StringConstants.LOCAL_PLAYER))
                .findAny().isPresent();
        mainApp.showNewPlayerWindow(new AddPlayerController(this, true, localHuman));
    }

    @FXML
    private void removePlayerPressed() {
        AddedPlayer selectedPlayer = (AddedPlayer) playersTable.getSelectionModel().getSelectedItem();
        int selectedPlayerIndex = playersTable.getSelectionModel().getSelectedIndex();
        playersTable.getItems().remove(selectedPlayer);
        gameInitializer.removePlayerSlot(selectedPlayerIndex);
        if (selectedPlayer != null && selectedPlayer.getType().equals(StringConstants.REMOTE_PLAYER)) {
            remotePlayers--;
        }
        forwardButton.setDisable(tableData.size() < MIN_NUM_OF_PLAYERS);
        addPlayerButton.setDisable(false);
    }

    @FXML
    private void forwardPressed() {
        if (multiplayer) {
            try {
                // validate server name
                String serverName = serverNameField.getText();
                if (serverName == null || serverName.trim().equals("")) {
                    AlertCreator.showErrorMessage(StringConstants.INVALID_SERVER_NAME_TITLE,
                            StringConstants.INVALID_SERVER_NAME_MSG);
                    return;
                }
                // validate server port
                String portFieldText = serverPortField.getText();
                int port;
                if (!(portFieldText == null || portFieldText.trim().equals(""))) {
                    port = Integer.valueOf(portFieldText);
                } else {
                    AlertCreator.showErrorMessage(StringConstants.INVALID_SERVER_PORT_TITLE,
                            StringConstants.INVALID_SERVER_PORT_MSG);
                    return;
                }
                // validate thinking time
                Integer thinkingTime = null;
                Integer timeExtensions = null;
                String timeFieldText = timeField.getText();
                String timeExtensionsFieldText = timeExtensionsField.getText();
                if (limitedTimeCheckBox.isSelected()) {
                    if (timeFieldText == null || timeExtensionsFieldText == null
                            || timeFieldText.trim().equals("") || timeExtensionsFieldText.trim().equals("")) {
                        AlertCreator.showErrorMessage(StringConstants.INVALID_TIMER_PROP_TITLE,
                                StringConstants.INVALID_TIMER_PROP_MSG);
                        return;
                    } else {
                        thinkingTime = Integer.valueOf(timeFieldText);
                        timeExtensions = Integer.valueOf(timeExtensionsFieldText);
                    }
                }
                // get external IP address
                String externalIp = NetworkUtils.getExternalIpAddress();
                // create server on database
                List<PlayerEntity> players = new ArrayList<>();
                for (AddedPlayer p : tableData) {
                    PlayerEntity.PlayerType playerType;
                    switch (p.getType()) {
                        case StringConstants.COMPUTER:
                            playerType = PlayerEntity.PlayerType.COMPUTER;
                            break;
                        default:
                            playerType = PlayerEntity.PlayerType.HUMAN;
                    }
                    if (p.isDefined()) {
                        players.add(new PlayerEntity(0, null, p.getName(), playerType, 
                                !p.getType().equals(StringConstants.REMOTE_PLAYER), p.getPassword()));
                    }
                }
                int availablePlaces = (int) tableData.stream().filter(p -> !p.isDefined()).count();
                ServerEntity serverEntity = new ServerEntity(0, serverName, externalIp, port,
                        ServerEntity.ServerState.LOBBY, thinkingTime, timeExtensions, 
                        privateServerCheckBox.isSelected(), availablePlaces, players);
                players.forEach(p -> p.setServer(serverEntity));
                serverDAO.createServer(serverEntity);
                // create the server endpoint and jump to the lobby
                LobbyController lobbyController = new LobbyController(gameInitializer);
                gameInitializer.createServer(NetworkManager.DEFAULT_PORT, lobbyController);
                mainApp.showLobbyWindow(lobbyController);
            } catch (IOException ex) {
                AlertCreator.showErrorMessage(StringConstants.SERVER_CREATION_FAILED_TITLE, ex.getMessage());
            } catch (DBConnectionException ex) {
                AlertCreator.showErrorMessage(StringConstants.SERVER_CREATION_FAILED_TITLE, StringConstants.DB_UNREACHABLE_MSG);
            } catch (ExistingServerNameException ex) {
                AlertCreator.showErrorMessage(StringConstants.SERVER_CREATION_FAILED_TITLE, StringConstants.EXISTING_SERVER_NAME_MSG);
            }
        }
    }

    public void addPlayer(String playerType, String playerName, String password, boolean defined) throws AddPlayerException {
        boolean nameExists = tableData.stream()
                .map(p -> p.getName())
                .filter(n -> n.toLowerCase().equals(playerName.trim().toLowerCase()))
                .findAny().isPresent();
        if (nameExists) {
            throw new AddPlayerException(StringConstants.ENTER_NEW_NAME);
        } else {
            tableData.add(new AddedPlayer(playerName.trim(), playerType, password, defined));
            switch (playerType) {
                case StringConstants.LOCAL_PLAYER:
                    gameInitializer.addPlayerSlot(playerName, GameInitializer.PlayerType.HUMAN);
                    break;
                case StringConstants.REMOTE_PLAYER:
                    gameInitializer.addPlayerSlot(playerName, GameInitializer.PlayerType.REMOTE);
                    break;
                case StringConstants.COMPUTER:
                    gameInitializer.addPlayerSlot(playerName, GameInitializer.PlayerType.COMPUTER);
                    break;
            }
        }

        addPlayerButton.setDisable(tableData.size() == MAX_NUM_OF_PLAYERS);
        forwardButton.setDisable(tableData.size() < MIN_NUM_OF_PLAYERS);

        if (playerType.equals(StringConstants.REMOTE_PLAYER)) {
            remotePlayers++;
        }
    }

    public void addPlayer(String playerType, String playerName, String password) throws AddPlayerException {
        addPlayer(playerType, playerName, password, true);
    }

    public void addPlayer(String playerType, String playerName, boolean defined) throws AddPlayerException {
        addPlayer(playerType, playerName, null, defined);
    }

    public void addPlayer(String playerType, String playerName) throws AddPlayerException {
        addPlayer(playerType, playerName, null);
    }

    public int getRemotePlayerCount() {
        return remotePlayers;
    }
}
