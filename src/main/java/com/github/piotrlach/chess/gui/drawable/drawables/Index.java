/*
 * Copyright (C) 2022 Piotr Lach
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
package com.github.piotrlach.chess.gui.drawable.drawables;

import com.github.piotrlach.chess.gui.drawable.Drawable;
import com.github.piotrlach.chess.gui.drawable.drawables.location.Location;
import com.github.piotrlach.chess.gui.drawable.drawables.location.locations.BottomRow;
import com.github.piotrlach.chess.gui.drawable.drawables.location.locations.LeftColumn;
import com.github.piotrlach.chess.gui.drawable.drawables.location.locations.RightColumn;
import com.github.piotrlach.chess.gui.drawable.drawables.location.locations.TopRow;
import lombok.ToString;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Piotr Lach
 */
@ToString
public class Index implements Drawable {

    private final int index;
    private final String symbol;
    private final Rectangle rectangle;

    private static final String FONT_NAME = "Liberation Mono";
    private static final Color FONT_COLOR = Color.LIGHT_GRAY;

    private static final Location[] LOCATIONS = {
        new BottomRow(),
        new LeftColumn(),
        new RightColumn(),
        new TopRow()
    };

    public Index(int x, int y, int size, int index) {
        rectangle = new Rectangle(x, y, size, size);
        this.index = index;
        this.symbol = chooseSymbol(index);
    }

    private String chooseSymbol(int index) {
        return Arrays.stream(LOCATIONS)
                .filter(location -> location.includes(index))
                .findAny()
                .map(location -> location.getSymbol(index))
                .map(Object::toString)
                .orElseThrow(() -> new IllegalStateException("No matching symbol found for index!"));
    }

    @Override
    public void draw(Graphics graphics) {
        if (isInCorner()) {
            return;
        }

        var font = new Font(FONT_NAME, Font.BOLD, rectangle.width * 3 / 4);

        graphics.setColor(FONT_COLOR);
        graphics.setFont(font);

        int a = rectangle.x + rectangle.height * 2 / 7;
        int b = rectangle.y + rectangle.height * 3 / 4;
        graphics.drawString(symbol, a, b);
    }

    private boolean isInCorner() {
        return List.of(0, 9, 26, 35).contains(index);
    }

    @Override
    public void setDimension(int size) {
        rectangle.setSize(size, size);
    }

    @Override
    public void setPosition(int x, int y) {
        rectangle.setLocation(x, y);
    }
}
