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
import hu.undieb.kg.socotra.util.StringConstants;
import javafx.beans.property.SimpleStringProperty;
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

        public AddedPlayer(String name, String type) {
            this.NAME = new SimpleStringProperty(name);
            this.TYPE = new SimpleStringProperty(type);
        }

        public String getName() {
            return NAME.get();
        }

        public String getType() {
            return TYPE.get();
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

    private SocotraApp mainApp;
    private GameInitializer gameInitializer;

    public NewGameController(SocotraApp mainApp, boolean multiplayer) {
        this.mainApp = mainApp;
        gameInitializer = new GameInitializer();
        remotePlayers = 0;
        if (!multiplayer) {
            serverSettingsPane.setDisable(true);
        }
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));

        tableData = FXCollections.observableArrayList();
        playersTable.setItems(tableData);

        removePlayerButton.setDisable(true);
        playersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                removePlayerButton.setDisable(false);
            } else {
                removePlayerButton.setDisable(true);
            }
        });
        
        forwardButton.setDisable(true);
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
        mainApp.showLobbyWindow(new LobbyController(gameInitializer));
    }

    public void addPlayer(String playerType, String playerName) throws AddPlayerException {
        boolean nameExists = tableData.stream()
                .map(p -> p.getName())
                .filter(n -> n.toLowerCase().equals(playerName.trim().toLowerCase()))
                .findAny().isPresent();
        if (nameExists) {
            throw new AddPlayerException(StringConstants.ENTER_NEW_NAME);
        } else {
            tableData.add(new AddedPlayer(playerName.trim(), playerType));
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

    public int getRemotePlayerCount() {
        return remotePlayers;
    }
}