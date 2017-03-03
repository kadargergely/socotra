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
    private Label timerLabel;
    
    private final GameWindowController mainCtr;
    
    public ButtonsController(GameWindowController ctr) {
        this.mainCtr = ctr;
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
}
