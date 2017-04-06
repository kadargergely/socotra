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

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Gergely Kadar
 */
@Entity
@Table(name = "socotra_servers")
public class ServerEntity implements Serializable {

    public enum ServerState {
        LOBBY,
        STARTED,
        CLOSED
    }

    @Id
    @Column(name = "server_id")
    @SequenceGenerator(name = "IdGenerator", sequenceName = "socotra_server_id_s", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdGenerator")
    private int serverId;

    @Column(name = "server_name")
    private String name;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private Integer port;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ServerState serverState;

    @Column(name = "thinking_time")
    private Integer thinkingTime;

    @Column(name = "time_extensions")
    private Integer timeExtensions;

    @Column(name = "private_flag")
    private Boolean privateServer;

    @Column(name = "available_places")
    private int availablePlaces;

    @OneToMany(mappedBy = "server", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerEntity> players;

    public ServerEntity() {
    }

    public ServerEntity(int serverId, String name, String ipAddress, Integer port, ServerState state, Integer thinkingTime,
            Integer timeExtensions, Boolean privateServer, int availablePlaces, List<PlayerEntity> players) {
        this.serverId = serverId;
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverState = state;
        this.thinkingTime = thinkingTime;
        this.timeExtensions = timeExtensions;
        this.privateServer = privateServer;
        this.availablePlaces = availablePlaces;
        this.players = players;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public Integer getThinkingTime() {
        return thinkingTime;
    }

    public void setThinkingTime(Integer thinkingTime) {
        this.thinkingTime = thinkingTime;
    }

    public Integer getTimeExtensions() {
        return timeExtensions;
    }

    public void setTimeExtensions(Integer timeExtensions) {
        this.timeExtensions = timeExtensions;
    }

    public Boolean getPrivateServer() {
        return privateServer;
    }

    public void setPrivateServer(Boolean privateServer) {
        this.privateServer = privateServer;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerEntity> players) {
        this.players = players;
    }

}
