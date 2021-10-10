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
import javax.swing.Icon;
import javax.swing.JOptionPane;
import my.chess.Board;
import my.chess.Move;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    private final boolean isMovingDown;

    private final int startRow;

    public Pawn(Color color, PieceName pieceName, Board board) {
        super(pieceName, color, imageLoader.getPAWN(color), board);

        startRow = pieceName == PieceName.Pawn1 ? 1 : 6;

        isMovingDown = startRow == 1;
    }

    @Override
    public void movePiece(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        if (isEnPassant(source, target)) {
            var moves = board.getMoves();
            var coord = moves.getLast().target;
            var square = board.getSquares().get(coord.index);
            square.setPiece(Empty.INSTANCE);
        }

        Piece piece = this;
        Move move;

        if (target.coord.row == 0 || target.coord.row == 7) {
            piece = promote(piece);
            move = new Move(source.coord, target.coord, piece);
        } else {
            move = new Move(source.coord, target.coord);
        }

        board.getMoves().add(move);
        board.changeCurrentColor();

        target.setPiece(piece);
        source.setPiece(Empty.INSTANCE);
        source.setHighlighted(false);
        isOnStartPosition = false;
    }

    private Piece promote(Piece piece) {

        int choice = showPromoteDialog();

        return switch(choice) {
            default -> new Queen(piece.color, board);
            case 1 -> new Knight(piece.color, board);
            case 2 -> new Rook(piece.color, board);
            case 3 -> new Bishop(piece.color, board);
        };
    }

    private int showPromoteDialog() {

        var parentComponent = board;
        var message = board.getResourceBundle().getString("Board.PromoteMessage");
        var title = board.getResourceBundle().getString("Board.PromoteMessageTitle");
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType = JOptionPane.INFORMATION_MESSAGE;
        Icon icon = null;
        String[] options = {
                board.getResourceBundle().getString("Board.QueenName"),
                board.getResourceBundle().getString("Board.KnightName"),
                board.getResourceBundle().getString("Board.RookName"),
                board.getResourceBundle().getString("Board.BishopName")
        };
        var initialValue = board.getResourceBundle().getString("Board.QueenName");

        int choice = JOptionPane.showOptionDialog(parentComponent,
                message,
                title,
                optionType,
                messageType,
                icon,
                options,
                initialValue
        );
        return choice;
    }

    private boolean isEnPassant(Square source, Square target) {

        var moves = board.getMoves();

        if (moves.isEmpty()) {
            return false;
        }

        var lastMove = moves.getLast();

        if (!isTwoSquaresAdvancedEnemyPawn(lastMove)) {
            return false;
        }
        var coord = lastMove.target;

        var lastMoveTarget = board.getSquares().get(coord.index);

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
        var lastMoveTargetSquare = board.getSquares().get(lastMove.target.index);
        var piece = lastMoveTargetSquare.getPiece();

        return piece instanceof Pawn && vDiff == 2 && piece.isFoe(this.color);
    }

    @Override
    public void setImage() {
        image = imageLoader.getPAWN(color);
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
