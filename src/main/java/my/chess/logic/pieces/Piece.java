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
package my.chess.logic.pieces;

import lombok.*;
import my.chess.logic.Logic;
import my.chess.logic.square.Square;

/**
 * @author Piotr Lach
 */
@EqualsAndHashCode
abstract public class Piece {

    private static final String symbolFormat = "%s;%s";
    protected final Logic logic;
    public final String symbol;
    public final String color;
    @Getter
    protected boolean isOnStartPosition = true;

    public Piece(final String symbol, final String color, final Logic logic) {
        this.symbol = symbol;
        this.color = color;
        this.logic = logic;
    }

    public void move(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        if (!logic.isValidMove(source, target)) {
            return;
        }

        target.setPiece(this);
        source.setPiece(Empty.INSTANCE);
        source.setSelected(false);
        isOnStartPosition = false;

        logic.addMove(source, target);

        logic.changeCurrentColor();
        logic.setOptionalSourceEmpty();
    }

    public boolean isFoe(Piece piece) {
        return !piece.color.equals(this.color);
    }

    public boolean isFoe(String color) {
        return !this.color.equals(color);
    }

    abstract public boolean isCorrectMovement(Square source, Square target);

    @Override
    public String toString() {
        return String.format(symbolFormat, symbol, color);
    }
}
