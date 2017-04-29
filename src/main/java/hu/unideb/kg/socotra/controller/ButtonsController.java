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

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author Gergely Kadar
 */
public class ButtonsController {

    @FXML
    private Button doneButton;
    @FXML
    private Button redrawButton;
    @FXML
    private Button passButton;
    @FXML
    private Button extendButton;
    @FXML
    private Label timerLabel;

    private final GameWindowController mainCtr;

    private boolean timeLimitExists;

    public ButtonsController(GameWindowController ctr, boolean timeLimitExists) {
        this.mainCtr = ctr;
        this.timeLimitExists = timeLimitExists;
    }

    @FXML
    private void initialize() {
        timerLabel.setVisible(timeLimitExists);
        extendButton.setVisible(timeLimitExists);
    }

    @FXML
    private void doneButtonClicked() {
        mainCtr.handleDoneButton();
    }

    @FXML
    private void redrawButtonClicked() {
        mainCtr.handleRedrawButton();
    }

    @FXML
    private void passButtonClicked() {
        mainCtr.handlePassButton();
    }

    @FXML
    private void extendButtonClicked() {
        mainCtr.handleExtendButton();
    }

    public void updateTimer(int remainingTime) {
        if (timerLabel != null) {
            String minutes = String.valueOf(remainingTime / 60);
            String seconds = remainingTime % 60 >= 10 ? String.valueOf(remainingTime % 60) : "0" + String.valueOf(remainingTime % 60);
            timerLabel.setText(minutes + ":" + seconds);
        }
    }

    public void disableButtonBasedOnTurn(boolean playersTurn) {
        doneButton.setDisable(!playersTurn);
        redrawButton.setDisable(!playersTurn);
        passButton.setDisable(!playersTurn);
        extendButton.setDisable(!playersTurn);
    }   

    void updateButtons(boolean playerCanPlay, boolean playerCanSwap, boolean playerCanPass, boolean playerCanExtendTime) {
        doneButton.setDisable(!playerCanPlay);
        redrawButton.setDisable(!playerCanSwap);
        passButton.setDisable(!playerCanPass);
        extendButton.setDisable(!playerCanExtendTime);
    }
}
