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
package hu.unideb.kg.socotra;

import hu.unideb.kg.socotra.controller.AddPlayerController;
import hu.unideb.kg.socotra.controller.ChoosePlayerController;
import hu.unideb.kg.socotra.controller.GameWindowController;
import hu.unideb.kg.socotra.controller.JoinServerController;
import hu.unideb.kg.socotra.controller.LobbyController;
import hu.unideb.kg.socotra.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import hu.unideb.kg.socotra.controller.LoginRegisterController;
import hu.unideb.kg.socotra.controller.MiddleGridPaneController;
import hu.unideb.kg.socotra.controller.RegisterController;
import hu.unideb.kg.socotra.controller.TestWindowController;
import hu.unideb.kg.socotra.controller.NewGameController;
import hu.unideb.kg.socotra.model.persistence.ServerDAOImpl;
import hu.unideb.kg.socotra.view.ResizableCanvas;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class SocotraApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocotraApp.class);
    private static int counter = 0;

    private Stage primaryStage;
    private Pane loginRegisterWindow;
    private Pane loginPane;
    private Pane registerPane;

    private LoginRegisterController loginRegisterCtr;
    private GameWindowController gameWindowCtr;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        String dictFile = "/hu_HU.dic";
        InputStreamReader inputStream = new InputStreamReader(SocotraApp.class.getResourceAsStream(dictFile), "UTF-8");
        showJoinServerWindow(new JoinServerController(this, inputStream, new ServerDAOImpl()));
//        showNewGameWindow(new NewGameController(this, inputStream, true, new ServerDAOImpl()));;
//         showGameWindow(GameWindowController.WindowType.MULTIPLAYER);
        // showTestWindow();

        LOGGER.info("GUI started.");
    }

    private Pane getLoginRegisterWindow() throws IOException {
        try {
            if (loginRegisterWindow == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxmls/LoginRegisterRoot.fxml"));
                loginRegisterCtr = new LoginRegisterController();
                loader.setController(loginRegisterCtr);
                loginRegisterWindow = loader.load();
                LOGGER.info("/fxmls/LoginRegisterRoot.fxml loaded.");
            }
        } catch (IOException e) {
            LOGGER.error("/fxmls/LoginRegisterRoot.fxml couldn't be loaded.");
            throw new IOException("/fxmls/LoginRegisterRoot.fxml couldn't be loaded.", e);
        }
        return loginRegisterWindow;
    }

    private Pane getLoginPane() throws IOException {
        try {
            if (loginPane == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxmls/Login.fxml"));
                LoginController loginCtr = new LoginController();
                loginCtr.setMainApp(this);
                loader.setController(loginCtr);
                loginPane = loader.load();
                LOGGER.info("/fxmls/Login.fxml loaded.");
            }
        } catch (IOException e) {
            LOGGER.error("/fxmls/Login.fxml couldn't be loaded.");
            throw new IOException("/fxmls/Login.fxml couldn't be loaded.", e);
        }
        return loginPane;
    }

    private Pane getRegisterPane() throws IOException {
        try {
            if (registerPane == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxmls/Register.fxml"));
                RegisterController registerCtr = new RegisterController();
                registerCtr.setMainApp(this);
                loader.setController(registerCtr);
                registerPane = loader.load();
                LOGGER.info("/fxmls/Register.fxml loaded.");
            }
        } catch (IOException e) {
            LOGGER.error("/fxmls/Register.fxml couldn't be loaded.");
            LOGGER.error(e.getMessage());
            throw new IOException("/fxmls/Register.fxml couldn't be loaded.", e);
        }
        return registerPane;
    }

    public void showLoginRegisterWindow() {
        try {
            Scene scene = new Scene(getLoginRegisterWindow());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bejelentkezés");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.error("Can't show Login/Register window: " + e.getMessage());
        }
    }

    public void showLogin() {
        try {
            if (loginRegisterCtr != null) {
                loginRegisterCtr.getLoginRegisterRootPane().setCenter(getLoginPane());
                primaryStage.setTitle("Bejelentkezés");
            }
        } catch (IOException e) {
            LOGGER.error("Can't switch to Login: " + e.getMessage());
        }
    }

    public void showRegister() {
        try {
            if (loginRegisterCtr != null) {
                loginRegisterCtr.getLoginRegisterRootPane().setCenter(getRegisterPane());
                primaryStage.setTitle("Regisztráció");
            }
        } catch (IOException e) {
            LOGGER.error("Can't switch to Register: " + e.getMessage());
        }
    }

    public void showGameWindow(GameWindowController.WindowType windowType) {
        try {
            BorderPane gameWindowBorderPane = new BorderPane();
            GridPane gameWindowGridPane = new GridPane();

            InputStreamReader inputStream = new InputStreamReader(SocotraApp.class.getResourceAsStream("/hu_HU.dic"), "UTF-8");
            List<String> playerNames = Arrays.asList("Játékos", "Számítógép");
//            List<GameInitializer.PlayerType> playerTypes = Arrays.asList(GameManager.PlayerType.HUMAN, GameManager.PlayerType.COMPUTER);
//            gameWindowCtr = GameInitializer.initGame(inputStream, playerNames, playerTypes);

            gameWindowBorderPane.setTop(loadNode("/fxmls/MenuBar.fxml", gameWindowCtr.getMenuBarCtr()));

            if (windowType == GameWindowController.WindowType.MULTIPLAYER) {
                // Creating left pane with score table and chat
                AnchorPane leftRootPane = new AnchorPane();
                leftRootPane.setPrefSize(350, 575);
                leftRootPane.setMinWidth(200);
                Node scoreTable = loadNode("/fxmls/ScoreTable.fxml", gameWindowCtr.getScoreTableCtr());
                AnchorPane.setTopAnchor(scoreTable, 25.0);
                AnchorPane.setLeftAnchor(scoreTable, 12.0);
                AnchorPane.setRightAnchor(scoreTable, 12.0);
                Node chat = loadNode("/fxmls/Chat.fxml", gameWindowCtr.getChatCtr());
                AnchorPane.setBottomAnchor(chat, 25.0);
                AnchorPane.setTopAnchor(chat, 210.0);
                AnchorPane.setLeftAnchor(chat, 12.0);
                AnchorPane.setRightAnchor(chat, 12.0);
                leftRootPane.getChildren().addAll(scoreTable, chat);
                GridPane.setHgrow(leftRootPane, Priority.SOMETIMES);
                GridPane.setVgrow(leftRootPane, Priority.SOMETIMES);
                GridPane.setConstraints(leftRootPane, 1, 1);

                // Creating middle pane with canvas
                MiddleGridPaneController middleGridPaneCtr = new MiddleGridPaneController();
                AnchorPane middleGridPane = (AnchorPane) loadNode("/fxmls/MiddleGridPane.fxml", middleGridPaneCtr);
                Canvas canvas = new ResizableCanvas();
                gameWindowCtr.getCanvasCtr().setCanvas(canvas);
                canvas.setWidth(550);
                canvas.setHeight(550);
                AnchorPane.setTopAnchor(canvas, 12.5);
                AnchorPane.setBottomAnchor(canvas, 12.5);
                AnchorPane.setLeftAnchor(canvas, 12.5);
                AnchorPane.setRightAnchor(canvas, 12.5);
                middleGridPaneCtr.getCanvasRootPane().getChildren().add(canvas);
                GridPane.setHgrow(middleGridPane, Priority.ALWAYS);
                GridPane.setVgrow(middleGridPane, Priority.SOMETIMES);
                GridPane.setConstraints(middleGridPane, 2, 1);
                // Binding the width and height of the canvas to keep its square aspect ratio 
                middleGridPaneCtr.getGridPane().getRowConstraints().get(1).prefHeightProperty().bind(middleGridPaneCtr.getGridPane().widthProperty());
                middleGridPaneCtr.getGridPane().getColumnConstraints().get(1).prefWidthProperty().bind(middleGridPaneCtr.getGridPane().heightProperty());

                // Creating right pane with game history table and buttons
                AnchorPane rightRootPane = new AnchorPane();
                rightRootPane.setPrefSize(350, 275);
                rightRootPane.setMinWidth(150);
                Node history = loadNode("/fxmls/History.fxml", gameWindowCtr.getHistoryCtr());
                AnchorPane.setTopAnchor(history, 25.0);
                AnchorPane.setBottomAnchor(history, 175.0);
                AnchorPane.setLeftAnchor(history, 12.0);
                AnchorPane.setRightAnchor(history, 12.0);
                Node buttons = loadNode("/fxmls/EndTurnButtons.fxml", gameWindowCtr.getButtonsCtr());
                AnchorPane.setBottomAnchor(buttons, 0.0);
                AnchorPane.setLeftAnchor(buttons, 12.0);
                AnchorPane.setRightAnchor(buttons, 12.0);
                rightRootPane.getChildren().addAll(history, buttons);
                GridPane.setHgrow(rightRootPane, Priority.SOMETIMES);
                GridPane.setVgrow(rightRootPane, Priority.SOMETIMES);
                GridPane.setConstraints(rightRootPane, 3, 1);

                // Adding the panes to the parent grid pane                
                gameWindowGridPane.getChildren().addAll(leftRootPane, middleGridPane, rightRootPane);
            } else if (windowType == GameWindowController.WindowType.SINGLE_PLAYER) {

            }

            gameWindowBorderPane.setCenter(gameWindowGridPane);

            Scene scene = new Scene(gameWindowBorderPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Socotra");
            primaryStage.show();

            primaryStage.setMinHeight(primaryStage.getHeight() - scene.getHeight() + 600);
            switch (windowType) {
                case SINGLE_PLAYER:
                    gameWindowBorderPane.setPrefSize(900, 600);
                    primaryStage.setMinWidth(primaryStage.getWidth() - scene.getWidth() + 800);
                case MULTIPLAYER:
                    gameWindowBorderPane.setPrefSize(1175, 600);
                    primaryStage.setMinWidth(primaryStage.getWidth() - scene.getWidth() + 975);
            }

            LOGGER.debug("primaryStage.getHeight() = " + String.valueOf(primaryStage.getHeight()));
            LOGGER.debug("scene.getHeight() = " + String.valueOf(scene.getHeight()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void showNewGameWindow(NewGameController ctr) {
        try {
            
            AnchorPane rootPane = (AnchorPane) loadNode("/fxmls/NewGame.fxml", ctr);
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Socotra");
            primaryStage.setMinWidth(640);
            primaryStage.setMinHeight(480);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void showNewPlayerWindow(AddPlayerController addPlayerController) {
        try {
            AnchorPane pane = (AnchorPane) loadNode("/fxmls/AddPlayer.fxml", addPlayerController);
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void showChoosePlayerWindow(ChoosePlayerController choosePlayerController) {
        try {
            AnchorPane pane = (AnchorPane) loadNode("/fxmls/ChoosePlayer.fxml", choosePlayerController);
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void showLobbyWindow(LobbyController ctr) {
        try {
            AnchorPane pane = (AnchorPane) loadNode("/fxmls/Lobby.fxml", ctr);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Socotra");
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(350);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void showJoinServerWindow(JoinServerController ctr) {
        try {
            AnchorPane pane = (AnchorPane) loadNode("/fxmls/JoinServer.fxml", ctr);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Socotra");
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void showTestWindow() {
        try {
            GridPane rootPane = (GridPane) loadNode("/fxmls/GameWindow.fxml", new TestWindowController());
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Socotra");
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private Node loadNode(String fxmlPath, Object controller) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource(fxmlPath));
            loader.setController(controller);
            Node node = loader.load();
            LOGGER.info(fxmlPath + " loaded.");
            return node;
        } catch (IOException e) {
            throw new IOException("Failed to load " + fxmlPath, e);
        }
    }
    
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public static void runApp(String[] args) {
        launch(args);
    }
}
