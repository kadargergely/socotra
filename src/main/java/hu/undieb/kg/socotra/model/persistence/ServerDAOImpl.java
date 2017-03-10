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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gergely Kadar
 */
public class ServerDAOImpl implements ServerDAO {

    @Override
    public List<ServerEntity> getAvailableServers() throws DBConnectionException {
        List<ServerEntity> servers;
        EntityManager entityManager = null;

        try {
            entityManager = DBConnection.getEntityManager();
            TypedQuery<ServerEntity> query = entityManager.createQuery("SELECT s FROM ServerEntity s WHERE s.serverState = :serverState", ServerEntity.class);
            servers = query.setParameter("serverState", ServerEntity.ServerState.LOBBY).getResultList();
            if (servers == null) {
                servers = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new DBConnectionException();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }

        return servers;
    }

    @Override
    public void createServer(ServerEntity server) throws DBConnectionException {
        EntityManager entityManager = null;
        
        try {
            entityManager = DBConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(server);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new DBConnectionException();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
