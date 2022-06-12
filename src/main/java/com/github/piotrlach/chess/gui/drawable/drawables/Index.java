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
import lombok.ToString;

import java.awt.*;
import java.util.List;
import java.util.Map;

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

    private enum Location {
        TOP_ROW,
        BOTTOM_ROW,
        LEFT_COLUMN,
        RIGHT_COLUMN,
        INVALID
    }

    public Index(int x, int y, int size, int index) {
        rectangle = new Rectangle(x, y, size, size);
        this.index = index;
        this.symbol = chooseSymbol();
    }

    private String chooseSymbol() {
        var characters = Map.ofEntries(
                Map.entry(Location.BOTTOM_ROW, (char) (index + 64)), /* A, B, C, ... */
                Map.entry(Location.TOP_ROW, (char) (index + 38)), /* A, B, C, ... */
                Map.entry(Location.LEFT_COLUMN, (char) (index / 2 + 44)), /* 1, 2, 3, ... */
                Map.entry(Location.RIGHT_COLUMN, (char) ((index - 1) / 2 + 44)), /* 1, 2, 3, ... */
                Map.entry(Location.INVALID, (char) (0))
        );

        return characters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(inferLocation()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(Object::toString)
                .orElseThrow(() -> new IllegalStateException("No matching symbol found for Index!"));
    }

    private Location inferLocation() {
        if (isInBottomRow()) {
            return Location.BOTTOM_ROW;
        } else if (isInTopRow()) {
            return Location.TOP_ROW;
        } else if (isInLeftColumn()) {
            return Location.LEFT_COLUMN;
        } else if (isInRightColumn()) {
            return Location.RIGHT_COLUMN;
        } else {
            return Location.INVALID;
        }
    }

    private boolean isInBottomRow() {
        return index >= 0 && index <= 9;
    }

    private boolean isInTopRow() {
        return index >= 26 && index <= 35;
    }

    private boolean isInLeftColumn() {
        return index >= 10 && index <= 25 && index % 2 == 0;
    }

    private boolean isInRightColumn() {
        return index >= 10 && index <= 25 && index % 2 == 1;
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
