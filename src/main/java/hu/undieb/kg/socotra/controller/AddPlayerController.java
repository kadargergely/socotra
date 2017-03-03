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

import hu.undieb.kg.socotra.util.AlertCreator;
import hu.undieb.kg.socotra.util.StringConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Gergely Kadar
 */
public class AddPlayerController {

    @FXML
    private ChoiceBox playerTypeChoiceBox;
    @FXML
    private TextField playerNameField;
    @FXML
    private Button playerOKButton;
    @FXML
    private Button playerCancelButton;

    private NewGameController mainCtr;

    private ObservableList<String> choiceBoxItems;

    public AddPlayerController(NewGameController ctr, boolean multiplayer, boolean localHuman) {
        this.mainCtr = ctr;
        choiceBoxItems = FXCollections.observableArrayList();
        if (!localHuman) {
            choiceBoxItems.add(StringConstants.LOCAL_PLAYER);
        }
        choiceBoxItems.add(StringConstants.COMPUTER);
        if (multiplayer) {
            choiceBoxItems.add(StringConstants.REMOTE_PLAYER);
        }
    }

    @FXML
    private void initialize() {
        playerTypeChoiceBox.setItems(choiceBoxItems);
        playerTypeChoiceBox.getSelectionModel().selectFirst();
        playerTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (((String) newVal).equals(StringConstants.REMOTE_PLAYER)) {
                playerNameField.setText("<" + StringConstants.REMOTE_PLAYER + " " + String.valueOf(mainCtr.getRemotePlayerCount() + 1) + ">");
                playerNameField.setDisable(true);
            } else {
                playerNameField.setText("");
                playerNameField.setDisable(false);
            }
        });
    }

    @FXML
    private void cancelPressed() {
        Stage stage = (Stage) playerCancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void okPressed() {
        if (playerNameField.getText().trim().equals("")) {
            AlertCreator.showErrorMessage(StringConstants.INVALID_NAME, StringConstants.ENTER_VALID_NAME);
            return;
        }
        try {
            mainCtr.addPlayer((String) playerTypeChoiceBox.getValue(), playerNameField.getText());
            Stage stage = (Stage) playerOKButton.getScene().getWindow();
            stage.close();
        } catch (NewGameController.AddPlayerException ex) {
            AlertCreator.showErrorMessage(StringConstants.EXISTING_NAME, ex.getMessage());
        }
    }
    
    @FXML
    private void nameFieldKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            okPressed();
        }
    }
}