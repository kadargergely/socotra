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
package hu.unideb.kg.socotra.model.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import hu.unideb.kg.socotra.controller.LobbyController;
import hu.unideb.kg.socotra.model.Players;

/**
 *
 * @author gkadar
 */
public class GameServerLobbyListener extends Listener {

    LobbyController lobbyController;
    private Players players;
    
    public GameServerLobbyListener(GameServer server, LobbyController lobbyController, Players players) {
        super();
        
    }

    @Override
    public void received(Connection c, Object object) {
        GameServer.GameConnection connection = (GameServer.GameConnection) c;
        if (object instanceof NetworkManager.RegisterPlayer) {
            connection.PLAYER_NAME = ((NetworkManager.RegisterPlayer) object).PLAYER_NAME;
            lobbyController.addRemotePlayerToGame(connection.PLAYER_NAME);
            
        }
    }
}
