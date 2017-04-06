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
package hu.unideb.kg.socotra.view;

import hu.unideb.kg.socotra.model.GameState;
import hu.unideb.kg.socotra.model.Tile;

/**
 * This interface contains methods that the controller of a view should implement. These methods are
 * called to update the view to reflect the current state of the game.
 *
 * @author Gergely Kadar
 */
public interface GameView {

//	public void updateBoard();
//	
//    public void updateBoardTile(int row, int col, Tile tile);
//
//    public void updateTrayTiles(int index, Tile tile);
//
//    public void updateTileInHand(Tile tile);
    
//    public void update(GameState gameState);
    
    public void update();
    
    public void drawTileInHand(Tile tileInHand);
}
