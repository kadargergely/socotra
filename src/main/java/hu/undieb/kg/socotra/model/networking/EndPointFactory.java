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
package hu.undieb.kg.socotra.model.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;

/**
 *
 * @author Gergely Kadar
 */
public class EndPointFactory {

    private static final int DEFAULT_PORT = 54555;

    public static EndPoint createServerEndPoint(int tcpPort) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(tcpPort);

        return server;
    }

    public static EndPoint createServerEndPoint() throws IOException {
        return createServerEndPoint(DEFAULT_PORT);
    }

    public static EndPoint createClientEndPoint(int timeout, String host, int tcpPort)
            throws IOException {
        Client client = new Client();
        client.start();
        client.connect(timeout, host, tcpPort);

        return client;
    }

    public static EndPoint createClientEndPoint(int timeout, String host)
            throws IOException {
        return createClientEndPoint(timeout, host, DEFAULT_PORT);
    }
}
