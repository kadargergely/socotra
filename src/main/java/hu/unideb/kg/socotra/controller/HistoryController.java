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
import java.util.List;
import java.util.stream.Collectors;
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
    
    private class PlayedWord {
        private final SimpleIntegerProperty TURN;
        private final SimpleStringProperty PLAYER_NAME;
        private final SimpleStringProperty WORD;
        private final SimpleIntegerProperty SCORE;

        public PlayedWord(int turn, String playerName, String word, int score) {
            this.TURN = new SimpleIntegerProperty(turn);
            this.PLAYER_NAME = new SimpleStringProperty(playerName);
            this.WORD = new SimpleStringProperty(word);
            this.SCORE = new SimpleIntegerProperty(score);
        }
        
        public PlayedWord(GameManager.PlayedWord playedWord) {
            this.TURN = new SimpleIntegerProperty(playedWord.TURN);
            this.PLAYER_NAME = new SimpleStringProperty(playedWord.PLAYER_NAME);
            this.WORD = new SimpleStringProperty(playedWord.WORD);
            this.SCORE = new SimpleIntegerProperty(playedWord.SCORE);
        }

        public int getTurn() {
            return TURN.get();
        }

        public String getPlayerName() {
            return PLAYER_NAME.get();
        }

        public String getWord() {
            return WORD.get();
        }

        public int getScore() {
            return SCORE.get();
        }        
    }
    
    private GameWindowController mainCtr;
    private ObservableList<PlayedWord> tableData;
    
    public HistoryController(GameWindowController ctr) {
        this.mainCtr = ctr;
    }
    
    @FXML
    private void initialize() {
        turnColumn.setCellValueFactory(new PropertyValueFactory<>("round"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        tableData = FXCollections.observableArrayList();
        historyTable.setItems(tableData);
    }
    
    public void update() {
        List<GameManager.PlayedWord> currentPlayedWords = mainCtr.getGameManager().getPlayedWords();
        int lastAddedRound = tableData.size();
        currentPlayedWords.stream().filter(pw -> pw.TURN > lastAddedRound).forEach(pw -> {
            tableData.add(new PlayedWord(pw));
        });
    }
}
