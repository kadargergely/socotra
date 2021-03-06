/*
 * Copyright (C) 2016 Gergely Kadar
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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author Gergely Kadar
 */
public class LoginController {
    
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button exitButton;
    
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    
    private SocotraApp mainApp;
    
    @FXML
    private void handleRegisterButton() {
        mainApp.showRegister();
    }
    
    @FXML
    private void handleLoginButton() {
        
    }
    
    @FXML
    private void handleExitButton() {
        Platform.exit();
    }
    
    public void setMainApp(SocotraApp mainApp) {
        this.mainApp = mainApp;
    }
}
