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
import java.util.Optional;
import java.util.stream.Collectors;
import my.chess.Board;
import my.chess.Coord;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class King extends Piece {

    public King(Color pieceColor, final Board board) {
        super(PieceName.King, pieceColor, imageLoader.getKING(pieceColor));
        this.board = board;        
    }
    
    @Override
    public void setImage() {
        image = imageLoader.getKING(color);
    }
    
    private void moveRook(Square source, Square target) {
        
        CastlingSide side = determineSide(source, target).get();        
        int rookCol, offset;
                
        switch (side) {
            case QUEEN:
                rookCol = 0; offset = 1;
                break;
            case KING:
                rookCol = 7; offset = -1;
                break;
            default: return;
        }
        
        var rookSquare = board.getSquares().get(source.coord.row * 8 + rookCol);            
        var rook = rookSquare.getPiece();

        int row = target.coord.row;
        int col = target.coord.col + offset;            
        var coord = new Coord(row, col);
        var square = board.getSquares().get(coord.index);

        rook.movePiece(rookSquare, square); 
    }
    
    @Override
    public void movePiece(Square source, Square target) {
        var piece = source.getPiece();
        if (piece == null || !(piece == this)) {
            return;
        }                                                
        
        if (isCastling(source, target)) {
            moveRook(source, target);
        }
        
        target.setPiece(this);        
        source.setPiece(null);
        source.setHighlighted(false);
        isOnStartPosition = false;
    }
    
    private Optional<Square> findSideRookSquare(List<Square> sideSquares, CastlingSide castlingSide) {
               
        var optionalRookSquare = sideSquares.stream()              
            .filter(square -> square.getPiece() != null)                    
            .filter(square -> square.getPiece().isOnStartPosition())
            .filter(square -> square.getPiece() instanceof Rook)                    
            .filter(square -> 
                switch (castlingSide) {
                    case KING -> square.coord.col == 7;
                    case QUEEN -> square.coord.col == 0;
                    default -> false;
                }
            ).findAny();
                        
        return optionalRookSquare;
    }
    
    private Optional<List<Square>> getSideSquares(CastlingSide castlingSide, Square source) {

        List<Square> sideSquares;  
        
        return switch (castlingSide) {
            case QUEEN -> Optional.of(
                    board.getSquares().stream()                    
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col < source.coord.col)
                    .collect(Collectors.toList())
            );
            case KING -> Optional.of(board.getSquares().stream()                    
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col > source.coord.col)
                    .collect(Collectors.toList())
            );
            default -> Optional.empty();
        };
    
    }
    
    private boolean isCastling(Square source, Square target) {
              
        var optionalCastlingSide = determineSide(source, target);                
        
        if (!this.isOnStartPosition() 
                || board.isAttacked(source)
                || optionalCastlingSide.isEmpty()) {
            return false;
        }                
        
        var castlingSide = optionalCastlingSide.get();                
        var optionalSideSquares = getSideSquares(castlingSide, source);
        
        if (optionalSideSquares.isEmpty()) {
            return false;
        }
        
        var sideSquares = optionalSideSquares.get();        
        var optionalRookSquare = findSideRookSquare(sideSquares, castlingSide);
        
        if (optionalRookSquare.isEmpty()) {
            return false;
        }
                        
        var rookSquare = optionalRookSquare.get();       
        sideSquares.removeIf(square -> square.equals(rookSquare));                
                       
        return isSidePathEmptyAndSafe(sideSquares, castlingSide);
    }
    
    private Optional<CastlingSide> determineSide(Square source, Square target) {
        
        if (!(source.coord.row == target.coord.row)) {
            return Optional.empty();
        }        
        
        if (target.coord.col == (source.coord.col + 2)) {
            return Optional.of(CastlingSide.KING);            
        } else if (target.coord.col == (source.coord.col - 2)) {
            return Optional.of(CastlingSide.QUEEN);
        }
        return Optional.empty();
    }
    
    private boolean isSidePathEmptyAndSafe(List<Square> sideSquares, CastlingSide side) {
        for (var square : sideSquares) {                        
            
            if (square.getPiece() != null) {
                return false;
            }                        
            if (side.equals(CastlingSide.KING)
                && square.coord.col <= 6
                && board.isAttacked(square)) {
                return false;
            }
            if (side.equals(CastlingSide.QUEEN)
                && square.coord.col >= 2
                && board.isAttacked(square)) {
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
        
        
        return isOneDiagonalMove 
                || isOneVerticalMove 
                || isOneHorizontalMove 
                || isCastling(source, target);        
    }    
    private final Board board;    
    private enum CastlingSide {
        KING,
        QUEEN
    }

}
