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
import javafx.scene.control.MenuItem;

/**
 *
 * @author Gergely Kadar
 */
public class MenuBarController {

    @FXML
    private MenuItem exitMenuItem;
    
    private GameWindowController mainCtr;

    public MenuBarController(GameWindowController mainCtr) {
        this.mainCtr = mainCtr;
    }

    @FXML
    private void exitPressed() {
        mainCtr.handleExitButton();
    }
}
