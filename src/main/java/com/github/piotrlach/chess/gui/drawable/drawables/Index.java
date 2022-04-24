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

/**
 *
 * @author Piotr Lach
 */
@ToString
public class Index implements Drawable {

    private final int idx;
    private final String symbol;
    private static final Color FONT_COLOR = Color.LIGHT_GRAY;
    private final Rectangle rectangle;

    public Index(int x, int y, int size, int idx) {
        rectangle = new Rectangle(x, y, size, size);
        this.idx = idx;
        this.symbol = chooseSymbol();
    }

    private String chooseSymbol() {
        var values = List.of(
                isInBottomRow(),
                isInTopRow(),
                isInLeftColumn(),
                isInRightColumn()
        );

        var value = values.indexOf(true);

        var chars = List.of(
                (char) (idx + 64), /* A, B, C, ... */
                (char) (idx + 38),
                (char) (idx / 2 + 44), /* 1, 2, 3, ... */
                (char) ((idx - 1) / 2 + 44)
        );

        return chars.get(value).toString();
    }

    private boolean isInBottomRow() {
        return idx >= 0 && idx <= 9;
    }

    private boolean isInTopRow() {
        return idx >= 26 && idx <= 35;
    }

    private boolean isInLeftColumn() {
        return idx >= 10 && idx <= 25 && idx % 2 == 0;
    }

    private boolean isInRightColumn() {
        return idx >= 10 && idx <= 25 && idx % 2 == 1;
    }

    @Override
    public void draw(Graphics graphics) {

        if (isInCorner()) {
            return;
        }

        var font = new Font("Liberation Mono", Font.BOLD, rectangle.width * 3 / 4);

        graphics.setColor(FONT_COLOR);
        graphics.setFont(font);

        int a = rectangle.x + rectangle.height * 2 / 7;
        int b = rectangle.y + rectangle.height * 3 / 4;
        graphics.drawString(symbol, a, b);
    }

    private boolean isInCorner() {
        return List.of(0, 9, 26, 35).contains(idx);
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
