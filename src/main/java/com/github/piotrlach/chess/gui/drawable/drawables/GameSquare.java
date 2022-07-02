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
import com.github.piotrlach.chess.gui.PieceImageLoader;
import com.github.piotrlach.chess.logic.square.Square;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.awt.*;

/**
 *
 * @author Piotr Lach
 */
@EqualsAndHashCode(callSuper = true)
public class GameSquare extends Square implements Drawable, Comparable<GameSquare> {

    private static final PieceImageLoader imageLoader = PieceImageLoader.INSTANCE;
    private final Rectangle rectangle;
    @Getter
    @Setter
    private boolean selectedSource = false;
    @Getter
    @Setter
    private boolean selectedTarget = false;

    private static final Color MY_WHITE = new Color(255, 255, 204);
    private static final Color MY_BROWN = new Color(153, 102, 0);

    public GameSquare(int x, int y, int size, int index) {
        super(index);
        rectangle = new Rectangle(x, y, size, size);
    }

    @Override
    public void setDimension(int size) {
        rectangle.setSize(size, size);
    }

    @Override
    public void setPosition(int x, int y) {
        rectangle.setLocation(x, y);
    }

    public boolean contains(Point point) {
        return rectangle.contains(point);
    }

    @Override
    public void draw(Graphics graphics) {
        if (isWhite()) {
            graphics.setColor(MY_WHITE);
        } else {
            graphics.setColor(MY_BROWN);
        }

        if (isSelectedSource()) {
            graphics.setColor(Color.RED);
        } else if (isSelectedTarget()) {
            graphics.setColor(Color.BLUE);
        }

        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        imageLoader.getImage(piece.getClass(), piece.color)
            .ifPresent(image -> graphics.drawImage(image, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null));
    }

    private boolean isWhite() {
        var isRowOdd = coord.row % 2 == 1;
        var isColOdd = coord.col % 2 == 1;

        return (isRowOdd && !isColOdd) || (!isRowOdd && isColOdd);
    }

    @Override
    public int compareTo(GameSquare other) {
        return Integer.compare(this.coord.index, other.coord.index);
    }
}
