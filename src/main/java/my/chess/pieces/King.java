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
import my.chess.Move;
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

    public King(Color pieceColor, Board board) {
        super(PieceName.King, pieceColor, imageLoader.getKING(pieceColor), board);
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
        source.setPiece(Empty.INSTANCE);
        source.setHighlighted(false);
        isOnStartPosition = false;

        var move = new Move(source.coord, target.coord);
        board.getMoves().add(move);

        board.changeCurrentColor();
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
        sideSquares.removeIf(rookSquare::equals);

        return isSidePathEmptyAndSafe(sideSquares, castlingSide);
    }

    private void moveRook(Square source, Square target) {

        var castlingSide = determineSide(source, target);
        int rookCol, offset;

        switch (castlingSide) {
            case QUEEN -> {
                rookCol = 0;
                offset = 1;
            }
            case KING -> {
                rookCol = 7;
                offset = -1;
            }
            default -> {
                return;
            }
        }

        var squares = board.getSquares();

        var rookSquare = squares.get(source.coord.row * 8 + rookCol);
        var rook = rookSquare.getPiece();

        int row = target.coord.row;
        int col = target.coord.col + offset;
        var coord = new Coord(row, col);
        var square = squares.get(coord.index);

        rook.movePiece(rookSquare, square);
        board.changeCurrentColor();
    }

    private CastlingSide determineSide(Square source, Square target) {

        if (!(source.isInSameRow(target))) {
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

        var squares = board.getSquares();

        return switch (castlingSide) {
            case QUEEN -> squares.stream()
                    .filter(source::isInSameRow)
                    .filter(source::isOnRight)
                    .collect(Collectors.toList());

            case KING -> squares.stream()
                    .filter(source::isInSameRow)
                    .filter(source::isOnLeft)
                    .collect(Collectors.toList());

            default -> Collections.emptyList();
        };

    }

    private Optional<Square> findSideRookSquare(List<Square> sideSquares, CastlingSide castlingSide) {

        return sideSquares.stream()
                .filter(square -> !(square.getPiece() instanceof Empty))
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
            if (!(square.getPiece() instanceof Empty)) {
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
