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
public class Rook extends Piece {

    public Rook(String color, Logic logic) {
        super("R", color, logic);
    }

    @Override
    public boolean isCorrectMovement(Square source, Square target) {

        int vDiff, hDiff; // vertical and horizontal difference;
        vDiff = Math.abs(source.coord.row - target.coord.row);
        hDiff = Math.abs(source.coord.col - target.coord.col);

        var isVerticalMov = vDiff > 0;
        var isHorizontalMov = hDiff > 0;

        return (isVerticalMov && !isHorizontalMov) || (isHorizontalMov && !isVerticalMov);
    }

}
