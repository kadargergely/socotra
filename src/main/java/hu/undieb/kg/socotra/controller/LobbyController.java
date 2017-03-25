/*
 * Copyright (C) 2017 Gergely Kadar
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
import hu.undieb.kg.socotra.util.StringConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Gergely Kadar
 */
public class LobbyController {

    @FXML
    private TableView playersTable;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private TextArea playerDataTextArea;
    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;

    public class PlayerInGame {

        private SimpleStringProperty name;
        private SimpleStringProperty type;
        private SimpleStringProperty status;

        public PlayerInGame(String name, String type, String status) {
            this.name = new SimpleStringProperty(name);
            this.status = new SimpleStringProperty(status);
            this.type = new SimpleStringProperty(type);
        }

        public String getName() {
            return name.get();
        }

        public String getType() {
            return type.get();
        }

        public String getStatus() {
            return status.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public void setStatus(String status) {
            this.status.set(status);
        }
    }

    private SocotraApp mainApp;
    private GameInitializer gameInitializer;

    private ObservableList<PlayerInGame> tableData;

    public LobbyController(GameInitializer gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableData = FXCollections.observableArrayList();
        playersTable.setItems(tableData);

        for (GameInitializer.PlayerSlot ps : gameInitializer.getPlayerSlots()) {
            String name = ps.player.getName();
            String type = "";
            switch (ps.playerType) {
                case HUMAN:
                    type = StringConstants.LOCAL_PLAYER;
                    break;
                case COMPUTER:
                    type = StringConstants.COMPUTER;
                    break;
                case REMOTE:
                    type = StringConstants.REMOTE_PLAYER;
                    break;
            }
            String status = ps.isFree() ? StringConstants.WAITING_FOR_PLAYER : StringConstants.CONNECTED;
            tableData.add(new PlayerInGame(name, type, status));
        }
    }

    public void playerConnected(String playerName) {
        for (PlayerInGame p : tableData) {
            if (p.getType().equals(StringConstants.REMOTE_PLAYER) && p.getName().equals(StringConstants.WAITING_FOR_PLAYER)) {
                p.setName(playerName);
                p.setStatus(StringConstants.CONNECTED);
                gameInitializer.remotePlayerJoined(playerName);
                break;
            }
        }
    }
}
