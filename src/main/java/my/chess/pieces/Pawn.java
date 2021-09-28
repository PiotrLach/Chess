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
import my.chess.LastMove;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    public Pawn(Color color, PieceName pieceName, LastMove lastMove) {
        super(pieceName, color, imageLoader.getPAWN(color));
        this.lastMove = lastMove;

        startRow = pieceName == PieceName.Pawn1 ? 1 : 6;

        isMovingDown = startRow == 1;        
    }

    @Override
    public void setImage() {
        image = imageLoader.getPAWN(color);
    }
    
    private boolean isEnPassant(Square source, Square target) {       
        if (!lastMove.isTwoSquaresAdvancedEnemyPawn(this.color)) {
            return false;
        }
        
        var lastMoveTarget = lastMove.getTarget();
        
        var isSourceOnSameRow = source.coord.row == lastMoveTarget.coord.row;
        var isSourceOnLeft = lastMoveTarget.coord.col == source.coord.col - 1;
        var isSourceOnRight = lastMoveTarget.coord.col == source.coord.col + 1;
        
        var isSourceNeighbor = isSourceOnSameRow && (isSourceOnLeft || isSourceOnRight); 
        
        var isTargetAbove = lastMoveTarget.coord.row - 1 == target.coord.row;        
        var isTargetSameCol = lastMoveTarget.coord.col == target.coord.col; 
        var isTargetBelow = lastMoveTarget.coord.row + 1 == target.coord.row;                
        
        var opt1 = isSourceNeighbor && isMovingDown && isTargetBelow && isTargetSameCol;
        var opt2 = isSourceNeighbor && !isMovingDown && isTargetAbove && isTargetSameCol;                
        
        if (opt1 || opt2) {
            lastMoveTarget.setPiece(null);
            return true;
        } 
        return false;
    }
        
    @Override
    public boolean isCorrectMovement(Square source, Square target) { 
        
        int verticalDiff, horizontalDiff; 
        
        verticalDiff = Math.abs(source.coord.row - target.coord.row);
        horizontalDiff = Math.abs(source.coord.col - target.coord.col);
        
        var isOneVerticalMove = verticalDiff == 1;
        var isTwoVerticalMoves = verticalDiff == 2;
        
        var isTargetRowHigher = target.coord.row > source.coord.row;
        var isTargetRowLower = target.coord.row < source.coord.row; 
        
        var isForwardMove = (isMovingDown ? isTargetRowHigher : isTargetRowLower);
        
        var isHorizontal = horizontalDiff == 1;                   
        var isNotHorizontal = horizontalDiff == 0;        
        
        var isOnStartRow = source.coord.row == startRow;        
        
        var isNullOnTarget = target.getPiece() == null;
        var isFoeOnTarget = !isNullOnTarget && isFoe(target.getPiece());
        
        var possibleMovements = List.of(
            isForwardMove && isOneVerticalMove && isNotHorizontal && isNullOnTarget,
            isForwardMove && isOnStartRow && isTwoVerticalMoves && isNotHorizontal && isNullOnTarget,
            isForwardMove && isOneVerticalMove && isHorizontal && isFoeOnTarget,
            isEnPassant(source, target)
        );
        
        return possibleMovements.contains(true);      
    }
    private final LastMove lastMove;
    private final boolean isMovingDown;
    private final int startRow;

}
