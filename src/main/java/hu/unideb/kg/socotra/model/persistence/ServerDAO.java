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
package hu.unideb.kg.socotra.model.persistence;

import java.util.List;

/**
 *
 * @author Gergely Kadar
 */
public interface ServerDAO {

    public List<ServerEntity> getAvailableServers() throws DBConnectionException;

    public void createServer(ServerEntity server) throws DBConnectionException, ExistingServerNameException;

    public List<ServerEntity> getServersByIp(String ipAddress) throws DBConnectionException;

    public ServerEntity getServerByName(String name) throws DBConnectionException;

    public void incrementAvailablePlaces(ServerEntity server) throws DBConnectionException;

    public void decrementAvailablePlaces(ServerEntity server) throws DBConnectionException;

    public void addPlayer(PlayerEntity player) throws DBConnectionException;
    
    public void removePlayer(ServerEntity server, PlayerEntity player) throws DBConnectionException;

    public void connectPlayer(PlayerEntity player) throws DBConnectionException;

    public void disconnectPlayer(PlayerEntity player) throws DBConnectionException;
}
