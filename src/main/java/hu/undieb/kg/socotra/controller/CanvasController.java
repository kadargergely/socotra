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

import hu.undieb.kg.socotra.SocotraApp;
import hu.undieb.kg.socotra.model.Tile;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class CanvasController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocotraApp.class);
    private Image backgroundImage;
    private Tile[][] boardTiles;

    private final double TILE_SIZE = 0.0563636363636364;
    private final double FIELD_SIZE = 0.0581818181818182;
    private final double BOARD_TOP_LEFT_X = 0.0636363636363636;
    private final double BOARD_TOP_LEFT_Y = 0.0181818181818182;
    private final double TRAY_BACK_TOP_LEFT_X = 0.2781818181818182;

    private Canvas canvas;
    private double mouseX;
    private double mouseY;

    private GraphicsContext gc;
    private Tile tileInHand;
    private Font tileLetterFont;

    private double tileSize;
    private Image tileSprite;
    private Font tileValueFont;
    private Tile[] trayTiles;

    private GameWindowController mainCtr;

    public CanvasController(GameWindowController mainCtr) {
        this.mainCtr = mainCtr;
        tileLetterFont = Font.loadFont(this.getClass().getResource("/fonts/FrancoisOne.ttf").toString(), tileSize * 0.7);
        tileValueFont = Font.loadFont(this.getClass().getResource("/fonts/FrancoisOne.ttf").toString(), tileSize * 0.3);
        backgroundImage = new Image(CanvasController.class.getResource("/images/scrabble_board.png").toString());
    }

    private void drawBoard() {

    }

    private void drawTile(Tile tile, double x, double y) {
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(x, y, tileSize, tileSize);
        gc.strokeRect(x, y, tileSize, tileSize);
        if (!"_".equals(tile.getLetter())) {
            gc.setFill(Color.DARKSLATEGREY);
            gc.setFont(tileLetterFont);
            gc.fillText(tile.getLetter(), (int) (x + tileSize * 0.5), (int) (y + tileSize * 0.4));
            if (tile.getValue() > 0) {
                gc.setFont(tileValueFont);
                gc.fillText(String.valueOf(tile.getValue()), x + tileSize * 0.8, y + tileSize * 0.8);
            }
        }
    }

    public void drawTileOnBoard(Tile tile, int row, int col) {

    }

//    private void drawTile(Tile tile, double x, double y) {
//        gc.setFill(Color.DARKSLATEGREY);
//        gc.setFont(tileLetterFont);
//        gc.drawImage(tileSprite, x, y, tileSize, tileSize);
//        //tileLetterFont.
//        gc.fillText(tile.getLetter(), x + tileSize * 0.5, y + tileSize * 0.35);
//        gc.setFont(tileValueFont);
//        gc.fillText(String.valueOf(tile.getValue()), x + tileSize, y);
//    }
    private void fillCanvas() {
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private void onResize() {
        tileSize = canvas.getWidth() * (31.0 / 550.0);
        tileLetterFont = Font.loadFont(this.getClass().getResource("/fonts/FrancoisOne.ttf").toString(), Math.round(tileSize * 0.7));
        tileValueFont = Font.loadFont(this.getClass().getResource("/fonts/FrancoisOne.ttf").toString(), Math.round(tileSize * 0.3));
        repaint();
    }

    private void onMousePressed(MouseEvent e) {
        if (e.getX() > canvas.getWidth() * (35.0 / 550.0) && e.getX() < canvas.getWidth() * (515.0 / 550.0)
                && e.getY() > canvas.getHeight() * (10.0 / 550.0) && e.getY() < canvas.getHeight() * (490.0 / 550.0)) {
            int row = (int) (e.getY() - canvas.getHeight() * (10.0 / 550.0)) / 32;
            int col = (int) (e.getX() - canvas.getWidth() * (35.0 / 550.0)) / 32;
            mainCtr.mousePressedOnBoard(row, col);
        } else if (e.getX() > canvas.getWidth() * (163.0 / 550.0) && e.getX() < canvas.getWidth() * (387.0 / 550.0)
                && e.getY() > canvas.getHeight() * (504.0 / 550.0) && e.getY() < canvas.getHeight() * (536.0 / 550.0)) {
            int index = (int) (e.getX() - canvas.getWidth() * (163.0 / 550.0)) / 32;
            mainCtr.mousePressedOnTray(index);
        }
    }

    private void onMouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    private void repaint() {
        // draw board
        gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(canvas.getWidth() * (153.0 / 550.0), canvas.getHeight() * (494.0 / 550.0),
                canvas.getWidth() * (244.0 / 550.0), canvas.getHeight() * (52.0 / 550.0));
        // draw tiles on board
        for (int i = 0; i < boardTiles.length; i++) {
            for (int j = 0; j < boardTiles[i].length; j++) {
                drawTile(boardTiles[i][j], canvas.getWidth() * (35.0 / 550.0) + j * canvas.getWidth() * (32.0 / 550.0),
                        canvas.getHeight() * (10.0 / 550.0) + i * canvas.getHeight() * (32.0 / 550));
            }
        }
        // draw tiles on tray
        for (int i = 0; i < trayTiles.length; i++) {
            drawTile(trayTiles[i], canvas.getWidth() * (163.0 / 550.0) + i * canvas.getWidth() * (32.0 / 550.0),
                    canvas.getHeight() * (504.0 / 550.0));
        }
        // draw tile in hand
        drawTile(tileInHand, mouseX, mouseY);
//        drawTile(new Tile("TY", 10), canvas.getWidth() * (35.0 / 550.0), canvas.getHeight() * (10.0 / 550.0));
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        // specify event handlers
        canvas.setOnMouseMoved(e -> onMouseMoved(e));
        canvas.setOnMousePressed(e -> onMousePressed(e));
        canvas.widthProperty().addListener(e -> onResize());
        canvas.heightProperty().addListener(e -> onResize());
        // set up graphics context
        gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
    }

    public void updateBoardTile(int row, int col, Tile tile) {
        boardTiles[row][col] = tile;
        repaint();
    }

    public void updateTileInHand(Tile tile) {
        tileInHand = tile;
        repaint();
    }

    public void updateTrayTile(int index, Tile tile) {
        trayTiles[index] = tile;
        repaint();
    }
}
