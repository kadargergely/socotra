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
package hu.undieb.kg.socotra.model.persistence;

import hu.undieb.kg.socotra.controller.GameInitializer;
import hu.undieb.kg.socotra.model.GameManager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class ServerDAOImpl implements ServerDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerDAOImpl.class);

    @Override
    public List<ServerEntity> getAvailableServers() throws DBConnectionException {
        List<ServerEntity> servers = new ArrayList<>();
        EntityManager entityManager = null;

        try {
            entityManager = DBConnection.getEntityManager();
            TypedQuery<ServerEntity> query = entityManager.createQuery(
                    "SELECT s FROM ServerEntity s "
                    + "WHERE s.serverState = :serverState AND s.privateServer = :privateServer", ServerEntity.class);
            query.setParameter("serverState", ServerEntity.ServerState.LOBBY);
            query.setParameter("privateServer", false);
            servers = query.getResultList();
            LOGGER.info("List of available servers successfully fetched from database.");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to fetch available servers from database.", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }

        return servers;
    }

    @Override
    public void createServer(ServerEntity server) throws DBConnectionException, ExistingServerNameException {
        EntityManager entityManager = null;

        try {
            entityManager = DBConnection.getEntityManager();
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT count(s.serverId) FROM ServerEntity s WHERE s.name = :name", Long.class);
            query.setParameter("name", server.getName());
            if (query.getSingleResult() != 0) {
                throw new ExistingServerNameException();
            }
            entityManager.getTransaction().begin();
            entityManager.persist(server);
            entityManager.getTransaction().commit();
            LOGGER.info("Server successfully created on database");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to create server on database.", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }
    }

    @Override
    public List<ServerEntity> getServersByIp(String ipAddress) throws DBConnectionException {
        EntityManager entityManager = null;
        List<ServerEntity> servers = new ArrayList<>();

        try {
            entityManager = DBConnection.getEntityManager();
            TypedQuery<ServerEntity> query = entityManager.createQuery(
                    "SELECT s FROM ServerEntity s WHERE s.ipAddress = :ipAddress", ServerEntity.class);
            servers = query.setParameter("ipAddress", ipAddress).getResultList();
            LOGGER.info("Server(s) with ipAddress " + ipAddress + " successfully fetched from the database.");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to fetch servers with ipAddress " + ipAddress + " from database.", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }

        return servers;
    }

    @Override
    public ServerEntity getServerByName(String name) throws DBConnectionException {
        EntityManager entityManager = null;
        ServerEntity server = null;

        try {
            entityManager = DBConnection.getEntityManager();
            TypedQuery<ServerEntity> query = entityManager.createQuery(
                    "SELECT s FROM ServerEntity s WHERE s.name = :name", ServerEntity.class);
            server = query.setParameter("name", name).getSingleResult();
            LOGGER.info("Server with name " + name + " successfully fetched from the database.");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to fetch server with name " + name + " from database.", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }

        return server;
    }

    @Override
    public void incrementAvailablePlaces(ServerEntity server) throws DBConnectionException {
        if (server.getAvailablePlaces() >= GameInitializer.MAX_PLAYERS) {
            return;
        }

        EntityManager entityManager = null;
        try {
            entityManager = DBConnection.getEntityManager();
            entityManager.getTransaction().begin();
            server.setAvailablePlaces(server.getAvailablePlaces() + 1);
            entityManager.getTransaction().commit();
            LOGGER.info("Number of available places successfully incremented on server named " + server.getName() + ".");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to increment number of available places on server named " + server.getName() + ".", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }
    }

    @Override
    public void decrementAvailablePlaces(ServerEntity server) throws DBConnectionException {
        if (server.getAvailablePlaces() <= 0) {
            return;
        }

        EntityManager entityManager = null;
        try {
            entityManager = DBConnection.getEntityManager();
            entityManager.getTransaction().begin();
            server.setAvailablePlaces(server.getAvailablePlaces() - 1);
            entityManager.getTransaction().commit();
            LOGGER.info("Number of available places successfully decremented on server named " + server.getName() + ".");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to decrement number of available places on server named " + server.getName() + ".", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }
    }

    @Override
    public void addPlayer(ServerEntity server, PlayerEntity player) throws DBConnectionException {
        EntityManager entityManager = null;
        try {
            entityManager = DBConnection.getEntityManager();
            entityManager.getTransaction().begin();
            server.getPlayers().add(player);
            entityManager.getTransaction().commit();
            LOGGER.info("Player named " + player.getName() + " successfully added to server named " + server.getName() + ".");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to add player named " + player.getName() + " to server named " + server.getName() + ".", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }
    }

    @Override
    public void connectPlayer(PlayerEntity player) throws DBConnectionException {
        EntityManager entityManager = null;
        try {
            entityManager = DBConnection.getEntityManager();
            entityManager.getTransaction().begin();
            player.setConnected(true);
            entityManager.getTransaction().commit();
            LOGGER.info("Player named " + player.getName() + " successfully set to connected status.");
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to set player named " + player.getName() + " to connected status.", e);
            throw new DBConnectionException();
        } finally {
            closeEntityManager(entityManager);
        }
    }

    private void closeEntityManager(EntityManager entityManager) throws DBConnectionException {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        } catch (PersistenceException e) {
            LOGGER.warn("Failed to close entityManager.", e);
            throw new DBConnectionException();
        }
    }

}
