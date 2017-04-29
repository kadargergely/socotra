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

import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Gergely Kadar
 */
public class ScoreTableController {

    @FXML
    private TableView scoreTable;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn scoreColumn;

    public class PlayerScore {

        private SimpleStringProperty name;
        private SimpleIntegerProperty score;

        public PlayerScore(String name, int score) {
            this.name = new SimpleStringProperty(name);
            this.score = new SimpleIntegerProperty(score);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getScore() {
            return score.get();
        }

        public void setScore(int score) {
            this.score.set(score);
        }
        
        public SimpleStringProperty nameProperty() {
            return this.name;
        }
        
        public SimpleIntegerProperty scoreProperty() {
            return this.score;
        }
    }

    private GameWindowController mainCtr;
    private ObservableList<PlayerScore> tableData;

    public ScoreTableController(GameWindowController ctr) {
        this.mainCtr = ctr;
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        tableData = FXCollections.observableArrayList();
        
        
        
        scoreTable.setItems(tableData);
    }

    public void update(List<String> playerNames, List<Integer> playerScores) {
        tableData.clear();       
        for (int i = 0; i < playerNames.size(); i++) {
            tableData.add(new PlayerScore(playerNames.get(i), playerScores.get(i)));
        }
    }
}
