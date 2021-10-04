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
import java.util.Collections;
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

    private enum CastlingSide {
        KING,
        QUEEN,
        WRONG
    }

    private transient final Board board;

    public King(Color pieceColor, final Board board) {
        super(PieceName.King, pieceColor, imageLoader.getKING(pieceColor));
        this.board = board;
    }

    @Override
    public void movePiece(Square source, Square target) {
        var piece = source.getPiece();
        if (!(piece == this)) {
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

    private boolean isCastling(Square source, Square target) {

        var castlingSide = determineSide(source, target);
        var sideSquares = getSideSquares(castlingSide, source);
        var optionalRookSquare = findSideRookSquare(sideSquares, castlingSide);

        if (!this.isOnStartPosition()
                || board.isAttacked(source)
                || castlingSide.equals(CastlingSide.WRONG)
                || sideSquares.isEmpty()
                || optionalRookSquare.isEmpty()) {
            return false;
        }

        var rookSquare = optionalRookSquare.get();
        sideSquares.removeIf(square -> square.equals(rookSquare));

        return isSidePathEmptyAndSafe(sideSquares, castlingSide);
    }

    private void moveRook(Square source, Square target) {

        CastlingSide side = determineSide(source, target);
        int rookCol, offset;

        switch (side) {
            case QUEEN -> {
                rookCol = 0; offset = 1;
            }
            case KING -> {
                rookCol = 7; offset = -1;
            }
            default -> {
                return;
            }
        }

        var rookSquare = board.getSquares().get(source.coord.row * 8 + rookCol);
        var rook = rookSquare.getPiece();

        int row = target.coord.row;
        int col = target.coord.col + offset;
        var coord = new Coord(row, col);
        var square = board.getSquares().get(coord.index);

        rook.movePiece(rookSquare, square);
    }

    private CastlingSide determineSide(Square source, Square target) {

        if (!(source.coord.row == target.coord.row)) {
            return CastlingSide.WRONG;
        }
        if (target.coord.col == (source.coord.col + 2)) {
            return CastlingSide.KING;
        } else if (target.coord.col == (source.coord.col - 2)) {
            return CastlingSide.QUEEN;
        }
        return CastlingSide.WRONG;
    }

    private List<Square> getSideSquares(CastlingSide castlingSide, Square source) {

        return switch (castlingSide) {
            case QUEEN -> board.getSquares().stream()
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col < source.coord.col)
                    .collect(Collectors.toList());
            case KING -> board.getSquares().stream()
                    .filter(square -> square.coord.row == source.coord.row)
                    .filter(square -> square.coord.col > source.coord.col)
                    .collect(Collectors.toList());
            default -> Collections.emptyList();
        };

    }

    private Optional<Square> findSideRookSquare(List<Square> sideSquares, CastlingSide castlingSide) {

        return sideSquares.stream()
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
    public void setImage() {
        image = imageLoader.getKING(color);
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

}
