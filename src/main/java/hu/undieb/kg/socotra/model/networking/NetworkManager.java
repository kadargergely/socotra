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
package hu.undieb.kg.socotra.model.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 *
 * @author Gergely Kadar
 */
public class NetworkManager {

    public static final int DEFAULT_PORT = 54555;

    private NetworkManager() {
    }

    public static class BoardAltered {

        public int ROW;
        public int COL;
        public String PLAYER_NAME;
    }

    public static class TrayAltered {

        public int INDEX;
        public String PLAYER_NAME;
    }

    public static class TurnEnded {

        public String TURN_ACTION;
        public String PLAYER_NAME;
    }

    public static class GameStarted {

        public long BAG_SEED;
        public String PLAYER_NAME;
    }

    public static class RegisterPlayer {

        public String PLAYER_NAME;
    }

    public static class PlayerJoined {

    }

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(BoardAltered.class);
        kryo.register(TrayAltered.class);
        kryo.register(TurnEnded.class);
        kryo.register(GameStarted.class);
        kryo.register(RegisterPlayer.class);
    }
}
