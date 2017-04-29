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
import hu.unideb.kg.socotra.util.StringConstants;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Gergely Kadar
 */
public class HistoryController {
    
    @FXML
    private TableView historyTable;
    @FXML
    private TableColumn turnColumn;
    @FXML
    private TableColumn playerColumn;
    @FXML
    private TableColumn wordColumn;
    @FXML
    private TableColumn scoreColumn;
    
    public class PlayedWord {
        private SimpleIntegerProperty turn;
        private SimpleStringProperty playerName;
        private SimpleStringProperty word;
        private SimpleIntegerProperty score;

        public PlayedWord(int turn, String playerName, String word, int score) {
            this.turn = new SimpleIntegerProperty(turn);
            this.playerName = new SimpleStringProperty(playerName);
            this.word = new SimpleStringProperty(word);
            this.score = new SimpleIntegerProperty(score);
        }
        
        public PlayedWord(GameManager.PlayedWord playedWord) {
            this.turn = new SimpleIntegerProperty(playedWord.getTurn());
            this.playerName = new SimpleStringProperty(playedWord.getPlayerName());
            this.word = new SimpleStringProperty(playedWord.getWord());
            this.score = new SimpleIntegerProperty(playedWord.getScore());
        }

        public int getTurn() {
            return turn.get();
        }
        
        public void setTurn(int turn) {
            this.turn.set(turn);
        }

        public String getPlayerName() {
            return playerName.get();
        }
        
        public void setPlayerName(String name) {
            this.playerName.set(name);
        }

        public String getWord() {
            return word.get();
        }
        
        public void setWord(String word) {
            this.word.set(word);
        }

        public int getScore() {
            return score.get();
        }
        
        public void setScore(int score) {
            this.score.set(score);
        }
        
        public SimpleIntegerProperty turnProperty() {
            return this.turn;
        }
        
        public SimpleStringProperty playerNameProperty() {
            return this.playerName;
        }
        
        public SimpleStringProperty wordProperty() {
            return this.word;
        }
        
        public SimpleIntegerProperty scoreProperty() {
            return this.score;
        }
    }
    
    private GameWindowController mainCtr;
    private ObservableList<PlayedWord> tableData;
    
    public HistoryController(GameWindowController ctr) {
        this.mainCtr = ctr;
    }
    
    @FXML
    private void initialize() {
        turnColumn.setCellValueFactory(new PropertyValueFactory<>("turn"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        tableData = FXCollections.observableArrayList();
        historyTable.setItems(tableData);
        historyTable.setPlaceholder(new Label(StringConstants.NO_WORDS_PLAYED_YET));
    }
    
    public void update(List<GameManager.PlayedWord> currentPlayedWords) {
        if (currentPlayedWords.size() < tableData.size()) {
            for (int i = currentPlayedWords.size(); i < tableData.size(); i++) {
                tableData.remove(i);
            }
        } else if (currentPlayedWords.size() == tableData.size() && currentPlayedWords.size() > 0) {
            tableData.set(tableData.size() - 1, new PlayedWord(currentPlayedWords.get(currentPlayedWords.size() - 1)));
        } else {
            for (int i = tableData.size(); i < currentPlayedWords.size(); i++) {
                tableData.add(new PlayedWord(currentPlayedWords.get(i)));
            }
        }
    }
}
