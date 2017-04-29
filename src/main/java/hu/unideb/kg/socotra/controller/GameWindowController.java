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
import hu.unideb.kg.socotra.model.Letters;
import hu.unideb.kg.socotra.model.Player;
import hu.unideb.kg.socotra.util.AlertCreator;
import hu.unideb.kg.socotra.util.StringConstants;
import javafx.application.Platform;

/**
 *
 * @author Gergely Kadar
 */
public class GameWindowController implements GameObserver, MenuBarMainController {

    public enum WindowType {
        SINGLE_PLAYER,
        MULTIPLAYER
    }

    private ScoreTableController scoreTableCtr;
    private ChatController chatCtr;
    private HistoryController historyCtr;
    private MenuBarController menuBarCtr;
    private CanvasController canvasCtr;
    private ButtonsController buttonsCtr;

    private SocotraApp mainApp;
    private GameManager gameManager;
    private Player player;

    public GameWindowController(SocotraApp mainApp, GameManager gameManager, Player player) {
        this.mainApp = mainApp;
        this.gameManager = gameManager;
        this.player = player;

        scoreTableCtr = new ScoreTableController(this);
        chatCtr = new ChatController();
        historyCtr = new HistoryController(this);
        menuBarCtr = new MenuBarController(this, true);
        canvasCtr = new CanvasController(this);
        buttonsCtr = new ButtonsController(this, gameManager.thinkingTimeLimitExists());
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
        update();
    }

    @Override
    public void thinkingTimeOver(Player player) {

    }

    @Override
    public void localPlayerLeft(Player player) {
        update();
    }

    @Override
    public void remotePlayerLeft(Player player) {
        update();
    }

    @Override
    public void serverLeft() {
        Platform.runLater(() -> {
            AlertCreator.showErrorMessage(StringConstants.SERVER_UNREACHABLE_TITLE, StringConstants.SERVER_UNREACHABLE_MSG);
            mainApp.getPrimaryStage().close();
        });
    }

    @Override
    public void gameStarted(Player firstPlayer) {
        update();
    }

    @Override
    public void updateTimer(int remainingTime) {
        Platform.runLater(() -> buttonsCtr.updateTimer(remainingTime));
    }

    @Override
    public void thinkingTimeExtended(Player player) {

    }

    @Override
    public String jokerLetterRequested(Player player) {
        if (player == this.player) {
            return AlertCreator.showChoiceDialog(StringConstants.CHOOSE_JOKER_LETTER_TITLE,
                    StringConstants.CHOOSE_JOKER_LETTER_MSG, Letters.getLetters());
        }
        return null;
    }

    @Override
    public void jokerLetterChosen(String letter, Player player) {
        
    }

    void handleMousePressedOnBoard(int row, int col) {
        gameManager.alterBoard(row, col, player);
    }

    void handleMousePressedOnTray(int index) {
        gameManager.alterTray(index, player);
    }

    void handleWindowResized() {
        canvasCtr.update(gameManager.getGameBoardTiles(), player.getTrayTiles(), player.getTileInHand());
    }

    void handleMouseMoved() {
        canvasCtr.update(gameManager.getGameBoardTiles(), player.getTrayTiles(), player.getTileInHand());
    }

    @Override
    public void handleExitButton() {
        gameManager.localPlayerLeft(player);
        mainApp.getPrimaryStage().close();
    }

    @Override
    public void handleNewGameButton() {

    }

    @Override
    public void handleJoinButton() {

    }

    @Override
    public void handleLoadGameButton() {

    }

    @Override
    public void handleSaveGameButton() {

    }

    @Override
    public void handleDoneButton() {
        gameManager.endTurn(GameManager.TurnAction.PLAY, player);
    }

    @Override
    public void handleRedrawButton() {
        gameManager.endTurn(GameManager.TurnAction.SWAP, player);
    }

    @Override
    public void handlePassButton() {
        gameManager.endTurn(GameManager.TurnAction.PASS, player);
    }

    @Override
    public void handleExtendButton() {
        gameManager.extendThinkingTime(player);
    }

    @Override
    public void handleUndoButton() {

    }

    @Override
    public void handleHelpButton() {

    }

    void componentsLoaded() {
        gameManager.gameWindowReady();
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

    MenuBarController getMenuBarCtr() {
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

    private void update() {
        canvasCtr.update(gameManager.getGameBoardTiles(), player.getTrayTiles(), player.getTileInHand());
        scoreTableCtr.update(gameManager.getPlayerNames(), gameManager.getPlayerScores());
        historyCtr.update(gameManager.getPlayedWords());
        buttonsCtr.updateButtons(
                gameManager.playerCanEndTurn(player, GameManager.TurnAction.PLAY),
                gameManager.playerCanEndTurn(player, GameManager.TurnAction.SWAP),
                gameManager.playerCanEndTurn(player, GameManager.TurnAction.PASS),
                gameManager.playerCanExtendTime(player));
    }

}
