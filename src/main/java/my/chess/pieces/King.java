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
import java.util.stream.Collectors;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class King extends Piece {

    public King(Color pieceColor, final List<Square> squares) {
        super(PieceName.King, pieceColor, imageLoader.getKING(pieceColor));
        this.squares = squares;
    }
    
    @Override
    public void setImage() {
        image = imageLoader.getKING(color);
    }
       
    private boolean isCastling(Square source, Square target) {
        
        var isSameRow = source.coord.row == target.coord.row;        
        
        if (this.wasMoved /*|| isInCheck(source)*/ || !isSameRow) {
            return false;
        }                
        
        List<Square> sideSquares;
        var isQueenSide = target.coord.col == source.coord.col - 2;
        var isKingSide = target.coord.col == source.coord.col + 2;
                
        if (isQueenSide) {
            sideSquares = squares.stream()                    
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col < source.coord.col)
                    .collect(Collectors.toList());                                                
        } else if (isKingSide) {
            sideSquares = squares.stream()                    
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col > source.coord.col)
                    .collect(Collectors.toList());            
        } else {
            return false;
        }
        
        var optionalRookSquare = sideSquares.stream()              
            .filter(square -> square.getPiece() != null)                    
            .filter(square -> !square.getPiece().wasMoved)
            .filter(square -> square.getPiece() instanceof Rook)                    
            .filter(square -> {
                if (isKingSide) {
                    return square.coord.col == 7;
                } else if (isQueenSide) {
                    return square.coord.col == 0;
                } else {
                    return false;
                }
            }).findAny();
            
        if (optionalRookSquare.isEmpty()) {
            return false;
        }
                        
        var rookSquare = optionalRookSquare.get();       
        sideSquares.removeIf(square -> square.equals(rookSquare));
        
        for (var square : sideSquares) {                        
            
            if (square.getPiece() != null) {
                return false;
            }
            
            if (square.coord.col <= target.coord.col /*isInCheck(square)*/) {
                return false;
            }
        }
               
        return true;
    }

    @Override
    public boolean isCorrectMovement(Square source, Square target) {                
                        
        int verticalDiff, horizontalDiff; 
        
        verticalDiff = Math.abs(source.coord.row - target.coord.row);
        horizontalDiff = Math.abs(source.coord.col - target.coord.col);
        
        var isOneVerticalDiff = verticalDiff == 1;
        var isZeroVerticalDiff = verticalDiff == 0;        
        var isOneHorizontalDiff = horizontalDiff == 1;
        var isZeroHorizontalDiff = horizontalDiff == 0;
        
        var isOneDiagonalMove = isOneHorizontalDiff && isOneVerticalDiff;
        var isOneVerticalMove = isOneVerticalDiff && isZeroHorizontalDiff;
        var isOneHorizontalMove = isOneHorizontalDiff && isZeroVerticalDiff;
        
        
        if (isOneDiagonalMove || isOneVerticalMove || isOneHorizontalMove /*|| isCastling(source, target)*/) {
            wasMoved = true;
            return true;
        };
        return false;              
    }
    private final transient List<Square> squares;

}
