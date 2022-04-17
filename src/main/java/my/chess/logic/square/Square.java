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
package my.chess.logic.square;

import lombok.*;
import my.chess.logic.pieces.Empty;
import my.chess.logic.pieces.King;
import my.chess.logic.pieces.Piece;

import java.awt.*;

@EqualsAndHashCode
public class Square {
    @EqualsAndHashCode.Include
    @ToString.Include
    public final Coord coord;
    @Getter
    @Setter
    @ToString.Include
    @NonNull
    protected Piece piece;
    @Getter
    @Setter
    private boolean selected = false;

    public Square(Coord coord) {
        this.coord = coord;
        this.piece = Empty.INSTANCE;
    }

    public Square(int index) {
        this.coord = new Coord(index);
        this.piece = Empty.INSTANCE;
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

    public boolean hasFoe(final String color) {
        return piece.isFoe(color);
    }

    public boolean hasKing() {
        return piece instanceof King;
    }
}
