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

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Gergely Kadar
 */
class MenuBarController {

    @FXML
    private MenuItem newGameMenuItem;
    @FXML
    private MenuItem saveGameMenuItem;
    @FXML
    private MenuItem loadGameMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem doneMenuItem;
    @FXML
    private MenuItem redrawMenuItem;
    @FXML
    private MenuItem passMenuItem;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private Menu turnMenu;

    private boolean inGame;

    private MenuBarMainController mainCtr;

    public MenuBarController(MenuBarMainController mainCtr, boolean inGame) {
        this.mainCtr = mainCtr;
        this.inGame = inGame;
    }

    @FXML
    private void initialize() {
        turnMenu.setVisible(inGame);
    }
    
    @FXML
    private void newGamePressed() {
        mainCtr.handleNewGameButton();
    }
    
    @FXML
    private void saveGamePressed() {
        
    }
    
    @FXML
    private void loadGamePressed() {
        
    }
    
    @FXML
    private void donePressed() {
        
    }
    
    @FXML
    private void redrawPressed() {
        
    }
    
    @FXML
    private void passPressed() {
        
    }
    
    @FXML
    private void undoPressed() {
        
    }
    
    @FXML
    private void helpPressed() {
        
    }

    @FXML
    private void exitPressed() {
        mainCtr.handleExitButton();
    }
    
    
}
