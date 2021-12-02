/*
 * Copyright (C) 2021 Piotr Lach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package my.chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Piotr Lach
 */
public class Index extends Rectangle implements Drawable {

    public Index(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void setDimension(int size) {
        setSize(size, size);
    }

    @Override
    public void setPosition(int x, int y) {
        setLocation(x, y);
    }

    @Override
    public void drawHighlighted(Graphics graphics) {

    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public boolean isHighlighted() {
        return false;
    }

    @Override
    public void setHighlighted(boolean highlighted) {
    }

}
