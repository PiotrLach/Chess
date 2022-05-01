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
package com.github.piotrlach.chess.logic.square;

import com.github.piotrlach.chess.logic.pieces.Empty;
import com.github.piotrlach.chess.logic.pieces.King;
import com.github.piotrlach.chess.logic.pieces.Piece;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@EqualsAndHashCode
public class Square {
    @EqualsAndHashCode.Include
    public final Coord coord;
    @Getter
    @Setter
    @NonNull
    protected Piece piece;

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
   public boolean isInSameCol(Square square) {
        return coord.col == square.coord.col;
    }

    public boolean isOnLeft(Square square) {
        return coord.col < square.coord.col;
    }

    public boolean isOnRight(Square square) {
        return coord.col > square.coord.col;
    }

    public boolean movePieceTo(Square target) {
        return piece.move(this, target);
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
