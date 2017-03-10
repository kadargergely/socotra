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
package hu.undieb.kg.socotra.util;

/**
 *
 * @author Gergely Kadar
 */
public class StringConstants {
    
    private StringConstants() {
    }

    public static final String LOCAL_PLAYER = "Helyi játékos";
    public static final String COMPUTER = "Számítógép";
    public static final String REMOTE_PLAYER = "Távoli játékos";
    public static final String INVALID_NAME = "Hibás név";
    public static final String ENTER_VALID_NAME = "Kérlek, adj meg egy megfelelő nevet!";
    public static final String EXISTING_NAME = "Már létező név";
    public static final String ENTER_NEW_NAME = "Ez a név már létezik. Kérlek, adj meg másikat!";
    public static final String CONNECTED = "Csatlakozva";
    public static final String WAITING_FOR_PLAYER = "Várakozás a játékosra...";
    public static final String SERVER_CREATION_FAILED_TITLE = "Szerver létrehozási hiba";
    public static final String SERVER_CREATION_FAILED_MSG = "Szerver létrehozása nem sikerült. Próbálkozz másik porttal!";
    public static final String INVALID_SERVER_NAME_TITLE = "Nem megfelelő szerver név";
    public static final String INVALID_SERVER_NAME_MSG = "Kérlek, adj meg egy megfelelő szerver nevet!";
    public static final String FAILED_TO_GET_EXTERNAL_IP = "Nem sikerült lekérdezni a külső IP címet. Próbálkozz később!";
    public static final String INVALID_TIMER_PROP_TITLE = "Nem megfelelő időkorlát beállítások";
    public static final String INVALID_TIMER_PROP_MSG = "Kérlek, adj meg megfelelő értékeket a gondolkodási idővel kapcsolatos beállításokhoz!";
    public static final String INVALID_SERVER_PORT_TITLE = "Nem megfelelő szerver port";
    public static final String INVALID_SERVER_PORT_MSG = "Kérlek, adj meg megfelelő értéket a szerver portnak!";
}
