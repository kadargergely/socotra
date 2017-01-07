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
package hu.undieb.kg.socotra.controller;

import hu.undieb.kg.socotra.model.GameState;
import hu.undieb.kg.socotra.model.LocalHumanPlayer;
import hu.undieb.kg.socotra.model.Tile;
import hu.undieb.kg.socotra.view.GameView;

/**
 *
 * @author Gergely Kadar
 */
public class GameWindowController implements GameView {

    private ScoreTableController scoreTableCtr;
    private ChatController chatCtr;
    private HistoryController historyCtr;
    private MenuBarController menuBarCtr;
    private CanvasController canvasCtr;

    private LocalHumanPlayer player;

    public GameWindowController() {
        scoreTableCtr = new ScoreTableController();
        chatCtr = new ChatController();
        historyCtr = new HistoryController();
        menuBarCtr = new MenuBarController();
        canvasCtr = new CanvasController(this);
    }

    void mousePressedOnBoard(int row, int col) {
        player.mousePressedOnBoard(row, col);
    }
    
    void mousePressedOnTray(int index) {
        player.mousePressedOnTray(index);
    }

//    @Override
//    public void updateTileInHand(Tile tile) {
//        canvasCtr.updateTileInHand(tile);
//    }
//
//    @Override
//    public void updateBoardTile(int row, int col, Tile tile) {
//        canvasCtr.updateBoardTile(row, col, tile);
//    }
//
//    @Override
//    public void updateTrayTiles(int index, Tile tile) {
//        canvasCtr.updateTrayTile(index, tile);
//    }

    public enum WindowType {
        SINGLE_PLAYER,
        MULTIPLAYER
    }

    public ScoreTableController getScoreTableCtr() {
        return scoreTableCtr;
    }

    public ChatController getChatCtr() {
        return chatCtr;
    }

    public HistoryController getHistoryCtr() {
        return historyCtr;
    }

    public MenuBarController getMenuBarCtr() {
        return menuBarCtr;
    }

    public CanvasController getCanvasCtr() {
        return canvasCtr;
    }

	@Override
	public void update(GameState gameState) {		
		
	}

	@Override
	public void drawTileInHand(Tile tileInHand) {
		// TODO Auto-generated method stub
		
	}

}
