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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Gergely Kadar
 */
public class JoinServerController {
    
    @FXML
    private Button addServerButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableColumn playerStatusColumn;
    @FXML
    private TableColumn playerTypeColumn;
    @FXML
    private TableColumn serverNameColumn;
    @FXML
    private TableColumn playersColumn;
    @FXML
    private TableColumn playerNameColumn;
    @FXML
    private TableView availableServersTable;
    @FXML
    private TableView playersTable;
    @FXML
    private TextField extendTimeField;
    @FXML
    private TextField serverNameField;
    @FXML
    private TextField timeField;
    
    public class AvailableServer  {
        
        private SimpleStringProperty name;
        private SimpleStringProperty players;
        
        public AvailableServer(String name, String players) {
            this.name = new SimpleStringProperty(name);
            this.players = new SimpleStringProperty(players);
        }
        
        public String getName() {
            return name.get();
        }
        
        public String getPlayers() {
            return players.get();
        }
        
        public void setName(String name) {
            this.name.set(name);
        }
        
        public void setPlayers(String players) {
            this.players.set(players);
        }
    }
    
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
    
    private ObservableList<AvailableServer> availableServers;
    private ObservableList<PlayerInGame> playersInGame;
    
    private SocotraApp mainApp;
    
    public JoinServerController(SocotraApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void initialize() {
        serverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playersColumn.setCellValueFactory(new PropertyValueFactory<>("players"));
        
        availableServers = FXCollections.observableArrayList();
        availableServersTable.setItems(availableServers);
        
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        playerStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        playersInGame = FXCollections.observableArrayList();
        playersTable.setItems(playersInGame);
    }
}
