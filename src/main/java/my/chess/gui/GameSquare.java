/*
 * Java chess game implementation
 * Copyright (C) 2021 Piotr Lach
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package my.chess.gui;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import my.chess.logic.Coord;
import my.chess.logic.Square;

import java.awt.*;

/**
 *
 * @author Piotr Lach
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
public class GameSquare extends Square implements Drawable {

    private final Rectangle rectangle;

    private static final Color MY_WHITE = new Color(255, 255, 204);
    private static final Color MY_BROWN = new Color(153, 102, 0);

    public GameSquare(int x, int y, int size, Coord coord) {
        super(coord);
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

        if (isSelected()) {
            graphics.setColor(Color.RED);
        }

        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        piece.drawImage(graphics, rectangle.x, rectangle.y, rectangle.width);
    }

    private boolean isWhite() {

        var isRowOdd = coord.row % 2 == 1;
        var isColOdd = coord.col % 2 == 1;

        return (isRowOdd && !isColOdd) || (!isRowOdd && isColOdd);
    }

}
