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
package hu.unideb.kg.socotra.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Gergely Kadar
 */
public class TestWindowController {
    
    @FXML
    private ImageView field0_0;
    @FXML
    private ImageView field0_1;
    @FXML
    private AnchorPane anchorPane0_0;
    
    private ImageView[][] fields;
    
    @FXML
    private void initialize() {
        fields = new ImageView[3][3];
        fields[0][0] = field0_0;
        fields[0][1] = field0_1;        
        
        Image img = new Image(this.getClass().getResource("/images/tile.png").toString());
        fields[0][0].setImage(img);
        
        fields[0][0].fitWidthProperty().bind(anchorPane0_0.widthProperty());
        fields[0][0].fitHeightProperty().bind(anchorPane0_0.heightProperty());
    }
}
