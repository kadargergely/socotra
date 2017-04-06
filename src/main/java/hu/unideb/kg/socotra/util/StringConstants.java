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
package hu.unideb.kg.socotra.util;

/**
 *
 * @author Gergely Kadar
 */
public class StringConstants {
    
    private StringConstants() {
    }

    public static final String LOCAL_PLAYER = "Helyi játékos";
    public static final String COMPUTER = "Számítógép";
    public static final String HUMAN = "Ember";
    public static final String REMOTE_PLAYER = "Távoli játékos";
    public static final String INVALID_NAME = "Hibás név";
    public static final String ENTER_VALID_NAME = "Kérlek, adj meg egy megfelelő nevet!";
    public static final String EXISTING_NAME = "Már létező név";
    public static final String ENTER_NEW_NAME = "Ez a név már létezik. Kérlek, adj meg másikat!";
    public static final String CONNECTED = "Csatlakozva";
    public static final String WAITING_FOR_PLAYER = "Várakozás a játékosra...";
    public static final String SERVER_CREATION_FAILED_TITLE = "Szerver létrehozási hiba";
    public static final String SERVER_CREATION_FAILED_MSG = "Szerver létrehozása nem sikerült. Próbálkozz másik porttal!";
    public static final String DB_UNREACHABLE_MSG = "Nem sikerült elérni az adatbázist.";
    public static final String INVALID_SERVER_NAME_TITLE = "Nem megfelelő szerver név";
    public static final String INVALID_SERVER_NAME_MSG = "Kérlek, adj meg egy megfelelő szerver nevet!";
    public static final String EXISTING_SERVER_NAME_MSG = "Már létezik ilyen nevű szerver. Kérlek, adj meg egy másik nevet!";
    public static final String FAILED_TO_GET_EXTERNAL_IP = "Nem sikerült lekérdezni a külső IP címet. Próbálkozz később!";
    public static final String INVALID_TIMER_PROP_TITLE = "Nem megfelelő időkorlát beállítások";
    public static final String INVALID_TIMER_PROP_MSG = "Kérlek, adj meg megfelelő értékeket a gondolkodási idővel kapcsolatos beállításokhoz!";
    public static final String INVALID_SERVER_PORT_TITLE = "Nem megfelelő szerver port";
    public static final String INVALID_SERVER_PORT_MSG = "Kérlek, adj meg megfelelő értéket a szerver portnak!";
    public static final String FAILED_TO_GET_SERVERS_TITLE = "A szerverek listája nem elérhető";
    public static final String FAILED_TO_GET_SERVERS_MSG = "Nem sikerült lekérdezni a szerverek listáját. Az adatbázis nem elérhető.";
    public static final String SERVER_PLAYER_STATUS_CONNECTED = "Csatlakozva";
    public static final String SERVER_PLAYER_STATUS_FREE = "Szabad";
    public static final String ADD_SERVER_TITLE = "Szerver hozzáadása";
    public static final String ADD_SERVER_TEXT = "Szerver IP címe";
    public static final String NO_SERVERS_AT_IP_TITLE = "Nem található szerver";
    public static final String NO_SERVERS_AT_IP_MSG = "Nem található elérhető szerver a megadott IP címen.";
    public static final String NEW_PLAYER = "Új játékos";
    public static final String PASSWORD = "jelszó";
    public static final String FAILED_TO_JOIN_SERVER_TITLE = "Szerver csatlakozási hiba";
    public static final String FAILED_TO_JOIN_SERVER_MSG = "Nem sikerült csatlakozni a szerverhez. A szerver nem elérhető.";
    public static final String SERVER_UNREACHABLE_TITLE = "Kapcsolódási hiba";
    public static final String SERVER_UNREACHABLE_MSG = "Megszakadt a kapcsolat a játékszerverrel. A játék ki fog lépni.";
    public static final String PLAYER_PASSWD_INCORRECT_TITLE = "Hibás jelszó";
    public static final String PLAYER_PASSWD_INCORRECT_MSG = "A csatlakozáshoz meg kell adni a kiválasztott játékoshoz tartozó jelszót.";
    public static final String CANT_JOIN_SERVER_TITLE = "Nem lehet csatlakozni a szerverhez"; 
    public static final String CANT_JOIN_SERVER_MSG = "Sajnos már nem lehet csatlakozni ehhez a szerverhez. Már elindult a játék, vagy nincs több hely.";
}
