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
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class Queen extends Piece {

    public Queen(Color pieceColor) {        
        super(PieceName.Queen, pieceColor, imageLoader.getQUEEN(pieceColor));
    }

    @Override
    public void setImage() {
        image = imageLoader.getQUEEN(color);
    }
    
    @Override
    public boolean isCorrectMovement(Square source, Square target) {
        
        int verticalDiff, horizontalDiff; 
        
        verticalDiff = Math.abs(source.coord.row - target.coord.row);
        horizontalDiff = Math.abs(source.coord.col - target.coord.col);
        
        var isVerticalMove = verticalDiff > 0;
        var isHorizontalMove = horizontalDiff > 0;
        var isDiagonalMove = verticalDiff == horizontalDiff;
        
        return isDiagonalMove || (isVerticalMove && !isHorizontalMove) || (isHorizontalMove && !isVerticalMove);
                              
    }

}
