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
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;

/**
 *
 * @author Piotr Lach
 */
public class Queen extends Piece {

    public Queen(Color figureColor) {        
        super(PieceName.Queen, figureColor, Images.getQUEEN(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        boolean movement = (Math.abs(source.row - target.row) == Math.abs(source.col - target.col))
                || (Math.abs(source.row - target.row) > 0 && Math.abs(source.col - target.col) == 0)
                || (Math.abs(source.row - target.row) == 0 && Math.abs(source.col - target.col) > 0);
        return movement;
    }

}
