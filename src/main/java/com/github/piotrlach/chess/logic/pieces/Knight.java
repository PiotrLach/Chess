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
package com.github.piotrlach.chess.logic.pieces;

import com.github.piotrlach.chess.logic.Logic;
import com.github.piotrlach.chess.logic.square.Square;

/**
 *
 * @author Piotr Lach
 */
public class Knight extends Piece {

    public Knight(String color, Logic logic) {
        super("N", color, logic);
    }

    @Override
    public boolean isCorrectMovement(Square source, Square target) {

        int verticalDiff, horizontalDiff;

        verticalDiff = Math.abs(source.coord.row - target.coord.row);
        horizontalDiff = Math.abs(source.coord.col - target.coord.col);

        var isTwoVerticalMoves = verticalDiff == 2;
        var isOneVerticalMove = verticalDiff == 1;

        var isTwoHorizontalMoves = horizontalDiff == 2;
        var isOneHorizontalMove = horizontalDiff == 1;

        return (isTwoHorizontalMoves && isOneVerticalMove) || (isOneHorizontalMove && isTwoVerticalMoves);

    }

}
