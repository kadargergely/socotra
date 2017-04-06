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

import hu.unideb.kg.socotra.controller.JavaVersionMessageController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class JavaVersionMessage extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaVersionMessage.class);
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Java verzió");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxmls/JavaVersionMessage.fxml"));
            loader.setController(new JavaVersionMessageController());
            AnchorPane pane = (AnchorPane) loader.load();
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            LOGGER.info("/fxmls/updateJavaMessage.fxml file loaded.");
        } catch (IOException e) {
            LOGGER.error("/fxmls/updateJavaMessage.fxml couldn't be loaded.");
        }
    }

    public static void showMessage(String[] args) {
        launch(args);
    }
}
