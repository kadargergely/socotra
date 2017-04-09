/*
 * Copyright (C) 2017 gkadar
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
 * @author gkadar
 */
public class SocotraAI implements GameObserver {

    public SocotraAI(GameManager gameManager, Player player) {

    }

    @Override
    public void trayAltered(int index, Player player) {
        
    }

    @Override
    public void boardAltered(int row, int col, Player player) {
        
    }

    @Override
    public void turnEnded(GameManager.TurnAction action, Player player) {
        
    }

    @Override
    public void localPlayerLeft(Player player) {
        
    }

    @Override
    public void remotePlayerLeft(Player player) {
        
    }

    @Override
    public void serverLeft() {
        
    }

}
