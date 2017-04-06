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

import hu.unideb.kg.socotra.model.GameManager;
import hu.unideb.kg.socotra.model.GameObserver;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.model.Tile;
import hu.unideb.kg.socotra.view.GameView;

/**
 *
 * @author Gergely Kadar
 */
public class GameWindowController implements GameView, GameObserver {

    private ScoreTableController scoreTableCtr;
    private ChatController chatCtr;
    private HistoryController historyCtr;
    private MenuBarController menuBarCtr;
    private CanvasController canvasCtr;
    private ButtonsController buttonsCtr;

    private GameManager gameManager;
    private Player player;

    public GameWindowController(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        
        scoreTableCtr = new ScoreTableController(this);
        chatCtr = new ChatController();
        historyCtr = new HistoryController(this);
        menuBarCtr = new MenuBarController();
        canvasCtr = new CanvasController(this);
        buttonsCtr = new ButtonsController(this);
    }

    @Override
    public void trayAltered(int index, Player player) {
        update();
    }

    @Override
    public void boardAltered(int row, int col, Player player) {
        update();
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStarted(long bagSeed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void mousePressedOnBoard(int row, int col) {
        gameManager.alterBoard(row, col, player);        
    }

    void mousePressedOnTray(int index) {
        player.alterTray(index);
    }

    void handleDoneButton() {
        if (gameManager.endTurn(GameManager.TurnAction.PLAY, player)) {
            update();
        }
    }

    void handleRedrawButton() {
        if (gameManager.endTurn(GameManager.TurnAction.SWAP, player)) {
            update();
        }
    }

    void handlePassButton() {
        if (gameManager.endTurn(GameManager.TurnAction.PASS, player)) {
            update();
        }
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

    public ButtonsController getButtonsCtr() {
        return buttonsCtr;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Player getPlayer() {
        return player;
    }

//    public void setPlayer(LocalHumanPlayer player) {
//        this.player = player;
//    }

    @Override
    public void update() {
        canvasCtr.repaint();
        scoreTableCtr.update();
        historyCtr.update();
    }

    @Override
    public void drawTileInHand(Tile tileInHand) {
        // TODO Auto-generated method stub

    }

}
