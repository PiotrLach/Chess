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
import java.util.List;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    public Pawn(Color color, PieceName pieceName) {
        super(pieceName, color, imageLoader.getPAWN(color));

        startingRow = pieceName == PieceName.Pawn1 ? 1 : 6;

        isOnBottomRow = startingRow == 1;
    }
    
    
    @Override
    public boolean isCorrectMovement(Square source, Square target) {        
        
        boolean isOneForwardMovement = Math.abs(source.coord.row - target.coord.row) == 1, 
                isTwoForwardMovements = Math.abs(source.coord.row - target.coord.row) == 2,
                isVertical = (isOnBottomRow ? target.coord.row > source.coord.row : target.coord.row < source.coord.row),
                isHorizontal = Math.abs(source.coord.col - target.coord.col) == 1,           
                isNotHorizontal = Math.abs(source.coord.col - target.coord.col) == 0,           
                isNullAhead = target.getPiece() == null,
                isFoeDiagonal = !isNullAhead && this.isFoe(target.getPiece());
        
        var availableMovements = List.of(
            isVertical && isOneForwardMovement && isNotHorizontal && isNullAhead,
            isVertical && source.coord.row == startingRow && isTwoForwardMovements && isNotHorizontal && isNullAhead,
            isVertical && isOneForwardMovement && isHorizontal && isFoeDiagonal
        );
        return availableMovements.contains(true);      
    }
    private final boolean isOnBottomRow;
    private final int startingRow;

}
