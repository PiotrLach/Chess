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
import java.util.List;
import my.chess.Square;
import my.chess.Board;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    public Pawn(Color color, PieceName pieceName) {
        super(pieceName, color, Images.getPAWN(color));

        startingRow = pieceName == PieceName.Pawn1 ? 1 : 6;

        isOnBottomRow = startingRow == 1;
    }
    
    private boolean enPassant(Coord source, Coord target) {
        if ((source.row - target.row) == (isOnBottomRow ? -1 : 1) && Math.abs(source.col - target.col) == 1) {
            System.out.println("---");
            System.out.format("%d %d %d %d", source.row, source.col, target.row, target.col);
            int temp = source.col - (source.col - target.col);
            System.out.println(source.row + " " + temp);
            Square square = Board.getSquare(source.row, source.col - (source.col - target.col));
            Piece neighbor = square.getPiece();
            if (neighbor instanceof Pawn) {
                Pawn p = (Pawn) neighbor;
                square.setPiece(null);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        Square square = Board.getSquare(target.row, target.col);
        
        boolean isOneForwardMovement = Math.abs(source.row - target.row) == 1, 
                isTwoForwardMovements = Math.abs(source.row - target.row) == 2,
                isVertical = (isOnBottomRow ? target.row > source.row : target.row < source.row),
                isHorizontal = Math.abs(source.col - target.col) == 1,           
                isNotHorizontal = Math.abs(source.col - target.col) == 0,           
                isNullAhead = square.getPiece() == null,
                isFoeDiagonal = !isNullAhead && this.isFoe(square.getPiece());
        
        var availableMovements = List.of(
            isVertical && isOneForwardMovement && isNotHorizontal && isNullAhead,
            isVertical && source.row == startingRow && isTwoForwardMovements && isNotHorizontal && isNullAhead,
            isVertical && isOneForwardMovement && isHorizontal && isFoeDiagonal
        );
        return availableMovements.contains(true);      
    }
    private final boolean isOnBottomRow;
    private final int startingRow;

}
