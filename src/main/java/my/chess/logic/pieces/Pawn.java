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
package my.chess.logic.pieces;

import my.chess.logic.Logic;
import my.chess.logic.Move;
import my.chess.logic.square.Square;

import java.util.List;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    private final boolean isMovingDown;

    private final int startRow;

    public Pawn(final String color, final String symbol, final Logic logic) {
        super(symbol, color, logic);

        startRow = symbol.equals("L") ? 1 : 6;

        isMovingDown = startRow == 1;
    }

    @Override
    public void move(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        if (!logic.isValidMove(source, target)) {
            return;
        }

        if (isEnPassant(source, target)) {
            var optional = logic.getLastMove();
            var coord = optional.get().target;

            logic.setPiece(coord, Empty.INSTANCE);
        }

        Piece piece = this;

        if (target.isInBorderRow()) {
            piece = promote(piece);
            logic.addMove(source, target, piece);
        } else {
            logic.addMove(source, target);
        }

        logic.changeCurrentColor();
        logic.setOptionalSourceEmpty();

        target.setPiece(piece);
        source.setPiece(Empty.INSTANCE);
        source.setSelected(false);
        isOnStartPosition = false;
    }

    private Piece promote(Piece piece) {

        int choice = logic.getPromotionChoice();

        return switch(choice) {
            default -> new Queen(piece.color, logic);
            case 1 -> new Knight(piece.color, logic);
            case 2 -> new Rook(piece.color, logic);
            case 3 -> new Bishop(piece.color, logic);
        };
    }

    private boolean isEnPassant(Square source, Square target) {

        var optional = logic.getLastMove();

        if (optional.isEmpty()) {
            return false;
        }

        var lastMove = optional.get();

        if (!isTwoSquaresAdvancedEnemyPawn(lastMove)) {
            return false;
        }
        var coord = lastMove.target;

        var lastMoveTarget = logic.getSquare(coord);

        var isSourceOnSameRow = source.coord.row == lastMoveTarget.coord.row;
        var isLastMoveTargetLeft = lastMoveTarget.coord.col == source.coord.col - 1;
        var isLastMoveTargetRight = lastMoveTarget.coord.col == source.coord.col + 1;

        var isSourceNeighbor = isSourceOnSameRow && (isLastMoveTargetLeft || isLastMoveTargetRight);

        var isTargetAbove = lastMoveTarget.coord.row - 1 == target.coord.row;
        var isTargetSameCol = lastMoveTarget.coord.col == target.coord.col;
        var isTargetBelow = lastMoveTarget.coord.row + 1 == target.coord.row;

        var opt1 = isSourceNeighbor && isMovingDown && isTargetBelow && isTargetSameCol;
        var opt2 = isSourceNeighbor && !isMovingDown && isTargetAbove && isTargetSameCol;

        return opt1 || opt2;
    }

    private boolean isTwoSquaresAdvancedEnemyPawn(Move lastMove) {

        int vDiff; // vertical difference
        vDiff = Math.abs(lastMove.source.row - lastMove.target.row);
        var lastMoveTargetSquare = logic.getSquare(lastMove.target);
        var piece = lastMoveTargetSquare.getPiece();

        return piece instanceof Pawn && vDiff == 2 && piece.isFoe(this.color);
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

        var isTargetEmpty = target.getPiece() instanceof Empty;
        var isFoeOnTarget = !isTargetEmpty && isFoe(target.getPiece());

        var possibleMovements = List.of(
            isForwardMove && isOneVerticalMove && isNotHorizontal && isTargetEmpty,
            isForwardMove && isOnStartRow && isTwoVerticalMoves && isNotHorizontal && isTargetEmpty,
            isForwardMove && isOneVerticalMove && isHorizontal && isFoeOnTarget,
            isEnPassant(source, target)
        );

        return possibleMovements.contains(true);
    }

}
