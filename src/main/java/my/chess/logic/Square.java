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
package my.chess.logic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import my.chess.gui.Drawable;
import my.chess.logic.pieces.Empty;
import my.chess.logic.pieces.Piece;

import java.awt.*;

/**
 *
 * @author Piotr Lach
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Square implements Drawable {

    @Getter
    @Setter
    @ToString.Include
    @NonNull
    private Piece piece;
    @Getter
    @Setter
    private boolean selected = false;
    @EqualsAndHashCode.Include
    @ToString.Include
    public final Coord coord;
    private final Rectangle rectangle;

    private static final Color MY_WHITE = new Color(255, 255, 204);
    private static final Color MY_BROWN = new Color(153, 102, 0);

    public Square(int x, int y, int size, Coord coord) {
        rectangle = new Rectangle(x, y, size, size);
        this.coord = coord;
        this.piece = Empty.INSTANCE;
    }

    public Square(Coord coord) {
        rectangle = new Rectangle(0, 0, 0, 0);
        this.coord = coord;
        this.piece = Empty.INSTANCE;
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

    public boolean isInBorderRow() {
        return coord.row == 0 || coord.row == 7;
    }

    public boolean isInSameRow(Square square) {
        return coord.row == square.coord.row;
    }

    public boolean isOnLeft(Square square) {
        return coord.col < square.coord.col;
    }

    public boolean isOnRight(Square square) {
        return coord.col > square.coord.col;
    }

    public void movePiece(Square target) {
        piece.move(this, target);
    }

    public boolean isCorrectMovement(Square target) {
        return piece.isCorrectMovement(this, target);
    }
}
