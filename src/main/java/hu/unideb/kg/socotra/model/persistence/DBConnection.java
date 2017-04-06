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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class DBConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
    
    private static EntityManagerFactory entityManagerFactory;
    private static List<EntityManager> entityManagers = new ArrayList<>();

    static EntityManager getEntityManager() throws PersistenceException {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("db");
            LOGGER.info("Database connection opened.");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();        
        entityManagers.add(entityManager);
        return entityManager;
    }

    public static void close() {
        try {
            entityManagers.stream().filter(em -> em.isOpen()).forEach(em -> em.close());
        } catch (Exception e) {
            LOGGER.warn("Failed to close open entityManagers.", e);
        }
        try {
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
                LOGGER.info("Database connection closed.");
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to close database connection.", e);
        }
    }
}
