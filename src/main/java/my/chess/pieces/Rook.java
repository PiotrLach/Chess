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
public class Rook extends Piece {

    public Rook(Color figureColor) {
        super(PieceName.Rook, figureColor, imageLoader.getROOK(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        
        int vDiff, hDiff; // vertical and horizontal difference;
        vDiff = Math.abs(source.row - target.row);
        hDiff = Math.abs(source.col - target.col);
        
        var isVerticalMov = vDiff > 0;
        var isHorizontalMov = hDiff > 0;        
        
        return (isVerticalMov && !isHorizontalMov) || (isHorizontalMov && !isVerticalMov);
    }

}
