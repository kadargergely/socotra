/*
 * Copyright (C) 2016 kadar
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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author Gergely Kadar
 */
public class JavaVersionMessageController {

    @FXML
    Button okButton;
    @FXML
    Label javaVersionMessageLabel;

    public JavaVersionMessageController() {
    }

    @FXML
    private void initialize() {
        javaVersionMessageLabel.setText("A játékhoz 1.8.0_40 vagy újabb Java verzió szükséges. Telepíts újabb Java verziót!");
    }

    @FXML
    private void okButtonClicked() {
        Platform.exit();
    }

    public void setMessageText(String message) {
        javaVersionMessageLabel.setText(message);
    }
}
