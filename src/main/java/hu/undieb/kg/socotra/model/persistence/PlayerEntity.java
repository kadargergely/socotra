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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Gergely Kadar
 */
@Entity
@Table(name = "socotra_players")
public class PlayerEntity {
    
    public enum PlayerType {
        HUMAN,
        COMPUTER
    }   
    
    @Id
    @Column(name = "player_id")
    @SequenceGenerator(name = "IdGenerator", sequenceName = "socotra_player_id_s", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdGenerator")
    private int playerId;
    
    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private ServerEntity server;
    
    @Column(name = "player_name")
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "player_type")
    private PlayerType type;    
    
    @Column(name = "connected_flag")
    private boolean connected;
    
    @Column(name = "password")
    private String password;

    public PlayerEntity() {
    }

    public PlayerEntity(int playerId, ServerEntity server, String name, PlayerType type, boolean connected, String password) {
        this.playerId = playerId;
        this.server = server;
        this.name = name;
        this.type = type;
        this.connected = connected;
        this.password = password;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    
    
}
