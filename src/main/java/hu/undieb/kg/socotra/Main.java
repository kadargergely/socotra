/*
 * Copyright (C) 2016 kadar
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
package hu.undieb.kg.socotra;

import hu.undieb.kg.socotra.model.persistence.DBConnection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gergely Kadar
 */
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Application started.");
        String javaVersion = System.getProperty("java.runtime.version");
        LOGGER.info("Using java version " + javaVersion);
        int majorVersion = Integer.parseInt(String.valueOf(javaVersion.charAt(2)));
        String versionAfterUnderline = javaVersion.substring(javaVersion.indexOf("_") + 1);
        StringBuilder minorVersionString = new StringBuilder();
        for (int i = 0; i < versionAfterUnderline.length(); i++) {
            if (Character.isDigit(versionAfterUnderline.charAt(i))) {
                minorVersionString.append(versionAfterUnderline.charAt(i));
            } else {
                break;
            }
        }
        if (majorVersion < 8 || (majorVersion == 8 && Integer.parseInt(minorVersionString.toString()) < 40)) {
            LOGGER.error("Java version 1.8.0_40 or later required, please update Java.");
            JavaVersionMessage.showMessage(args);
        } else {
            LOGGER.info("Starting GUI...");
            SocotraApp.runApp(args);
            LOGGER.info("GUI exited.");
//            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//            threadSet.forEach(t -> System.out.println(t.getName()));
            DBConnection.close();
        }
    }
}
