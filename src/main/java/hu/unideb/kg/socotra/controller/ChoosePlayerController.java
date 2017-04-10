/*
 * Copyright (C) 2017 gkadar
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

import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.networking.NetworkManager;
import hu.unideb.kg.socotra.model.persistence.DBConnectionException;
import hu.unideb.kg.socotra.model.persistence.PlayerEntity;
import hu.unideb.kg.socotra.model.persistence.ServerDAO;
import hu.unideb.kg.socotra.model.persistence.ServerEntity;
import hu.unideb.kg.socotra.util.AlertCreator;
import hu.unideb.kg.socotra.util.StringConstants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author gkadar
 */
public class ChoosePlayerController {

    @FXML
    private ChoiceBox playerChoiceBox;
    @FXML
    private TextField playerNameField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;

    private JoinServerController mainCtr;
    private SocotraApp mainApp;
    private GameInitializer gameInitializer;
    private ServerEntity serverToConnect;
    private ServerDAO serverDAO;

    private ObservableList<String> choiceBoxItems;    

    public ChoosePlayerController(SocotraApp mainApp, ServerDAO serverDAO, ServerEntity serverToConnect, GameInitializer gameInitializer) {
        this.mainApp = mainApp;
        this.serverDAO = serverDAO;
        this.serverToConnect = serverToConnect;
        this.gameInitializer = gameInitializer;
        choiceBoxItems = FXCollections.observableArrayList();
        choiceBoxItems.add(StringConstants.NEW_PLAYER);
        for (PlayerEntity player : serverToConnect.getPlayers()) {
            if (!player.isConnected() && player.getType() == PlayerEntity.PlayerType.HUMAN) {
                choiceBoxItems.add(player.getName());
            }
        }
    }

    @FXML
    private void initialize() {
        playerChoiceBox.setItems(choiceBoxItems);
        playerChoiceBox.getSelectionModel().selectFirst();
        playerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            String newValue = (String) newVal;
            if (!StringConstants.NEW_PLAYER.equals(newValue)) {
                playerNameField.setText(newValue);
            } else {
                playerNameField.clear();
            }
            playerNameField.setDisable(!StringConstants.NEW_PLAYER.equals(newValue));
        });
    }

    @FXML
    private void okPressed() {
        try {
            if (playerChoiceBox.getValue().equals(StringConstants.NEW_PLAYER)) {
                // validate player name
                if (playerNameField.getText().trim().equals("")) {
                    AlertCreator.showErrorMessage(StringConstants.INVALID_NAME, StringConstants.ENTER_VALID_NAME);
                    return;
                }
                // check if player name doesn't already exist on the server
                if (serverToConnect.getPlayers().stream().filter(p -> p.getName().equals(playerNameField.getText())).findAny().isPresent()) {
                    AlertCreator.showErrorMessage(StringConstants.EXISTING_NAME, StringConstants.ENTER_NEW_NAME);
                    return;
                }
                // validate password
                String password = null;
                if (!passwordField.getText().trim().equals("")) {
                    password = passwordField.getText();
                }
                // add players to the game
                gameInitializer.addPlayer(playerNameField.getText(), Player.PlayerType.HUMAN);
                serverToConnect.getPlayers().forEach(p -> {
                    gameInitializer.addPlayer(p.getName(),
                            p.getType() == PlayerEntity.PlayerType.HUMAN ? Player.PlayerType.REMOTE : Player.PlayerType.COMPUTER);
                });
            } else {
                // check if password is correct
                String correctPassword = getPlayerPassword(playerNameField.getText());
                if (correctPassword == null) {
                    if (!(passwordField.getText() == null || passwordField.getText().equals(""))) {
                        AlertCreator.showErrorMessage(StringConstants.PLAYER_PASSWD_INCORRECT_TITLE, StringConstants.PLAYER_PASSWD_INCORRECT_MSG);
                    }
                } else if (!(passwordField.getText() != null && passwordField.getText().equals(correctPassword))) {
                    AlertCreator.showErrorMessage(StringConstants.PLAYER_PASSWD_INCORRECT_TITLE, StringConstants.PLAYER_PASSWD_INCORRECT_MSG);
                }
                // add players to the game
                gameInitializer.addPlayer(playerNameField.getText(), Player.PlayerType.HUMAN);
                for (PlayerEntity player : serverToConnect.getPlayers()) {
                    if (!player.getName().equals(playerNameField.getText())) {
                        gameInitializer.addPlayer(player.getName(),
                                player.getType() == PlayerEntity.PlayerType.HUMAN ? Player.PlayerType.REMOTE : Player.PlayerType.COMPUTER);
                    }
                }
            }            
            // create client endpoint
            LobbyController lobbyController = new LobbyController(mainApp, gameInitializer, serverDAO, serverToConnect);            
            gameInitializer.createClient(serverToConnect.getIpAddress(),
                    serverToConnect.getPort() != null ? serverToConnect.getPort() : NetworkManager.DEFAULT_PORT, lobbyController);
            mainApp.showLobbyWindow(lobbyController);
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            AlertCreator.showErrorMessage(StringConstants.FAILED_TO_JOIN_SERVER_TITLE, StringConstants.FAILED_TO_JOIN_SERVER_MSG);
        }
    }
    
    @FXML
    private void cancelPressed() {
        try {
            serverDAO.incrementAvailablePlaces(serverToConnect);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (DBConnectionException ex) {
            Logger.getLogger(ChoosePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void nameFieldKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            okPressed();
        }
    }

    private String getPlayerPassword(String playerName) {
        for (PlayerEntity player : serverToConnect.getPlayers()) {
            if (player.getName().equals(playerName)) {
                return player.getPassword();
            }
        }

        return null;
    }

    private PlayerEntity getPlayerEntityByName(String name) {
        for (PlayerEntity player : serverToConnect.getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }
}
