/*
 * Copyright (C) 2016 Gergely Kadar
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
package hu.unideb.kg.socotra.view;

import hu.unideb.kg.socotra.SocotraApp;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A version of AnchorPane, which keeps an aspect ratio when resized.
 *
 * @author Gergely Kadar
 */
public class ResizableAnchorPane extends AnchorPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResizableAnchorPane.class);
    
    public ResizableAnchorPane() {
    }
    
    @Override
    public void setWidth(double width) {
        super.setWidth(width);
    }    
    
    @Override
    public void setHeight(double height) {
        super.setHeight(height);
    }
}
