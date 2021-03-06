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
package hu.unideb.kg.socotra.model;

/**
 *
 * @author Gergely Kadar
 */
public interface GameObserver {
    
    public void trayAltered(int index, Player player);
    
    public void boardAltered(int row, int col, Player player);
    
    public void turnEnded(GameManager.TurnAction action, Player nextPlayer);
    
    public void localPlayerLeft(Player player);
    
    public void remotePlayerLeft(Player player);
    
    public void serverLeft();
    
    public void gameStarted(Player firstPlayer); 
    
    public void updateTimer(int remainingTime);
    
    public void thinkingTimeExtended(Player player);
    
    public void thinkingTimeOver(Player player);
    
    public String jokerLetterRequested(Player player);
    
    public void jokerLetterChosen(String letter, Player player);
    
//    public void gameEnded();
}
