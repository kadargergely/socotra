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

/**
 *
 * @author gkadar
 */
public class InitialWindowController implements MenuBarMainController {
    
    private MenuBarController menuBarCtr;
    private CanvasController canvasCtr;

    public InitialWindowController(SocotraApp mainApp) {
        this.menuBarCtr = new MenuBarController(this, false);
        this.canvasCtr = new CanvasController();
    }

    @Override
    public void handleExitButton() {
        
    }

    @Override
    public void handleNewGameButton() {
        
    }

    @Override
    public void handleLoadGameButton() {
        
    }

    @Override
    public void handleSaveGameButton() {
        
    }

    @Override
    public void handleDoneButton() {
        
    }

    @Override
    public void handleRedrawButton() {
        
    }

    @Override
    public void handlePassButton() {
        
    }

    @Override
    public void handleUndoButton() {
        
    }

    @Override
    public void handleHelpButton() {
        
    }

    MenuBarController getMenuBarController() {
        return this.menuBarCtr;
    }
    
    CanvasController getCanvasController() {
        return this.canvasCtr;
    }
}
