/*
 * Copyright (C) 2022 Piotr Lach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package my.chess.logic;

import lombok.Getter;
import my.chess.gui.Board;
import my.chess.gui.Message;
import my.chess.logic.pieces.*;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Piotr Lach
 */
public class Logic {

    private final Board board;
    @Getter
    private final List<Square> squares;
    private final PieceFactory pieceFactory;
    private final Deque<Move> moves;
    @Getter
    private Color currentColor = Color.WHITE;


    public Logic(Board board, List<Square> squares, Deque<Move> moves) {
        this.board = board;
        this.squares = squares;
        pieceFactory = new PieceFactory(this);
        this.moves = moves;
    }

    public void setLayout(String[] layout) {

        clearBoard();

        if (layout.length != 64) {
            throw new IllegalArgumentException("Layout lacks squares!");
        }

        for (int row1 = 7, row2 = 0; row1 >= 0; row1--, row2++) {
            for (int col = 0; col < 8; col++) {

                var layoutCoord = new Coord(row1, col);

                var string = layout[layoutCoord.index];
                var piece = pieceFactory.getPiece(string);

                var squareCoord = new Coord(row2, col);

                var square = squares.get(squareCoord.index);
                square.setPiece(piece);
            }
        }

        board.repaint();
    }

    private void clearBoard() {
        currentColor = Color.WHITE;
        board.setOptionalSourceEmpty();

        for (var square : squares) {
            square.setPiece(Empty.INSTANCE);
            square.setSelected(false);
        }

        clearMoves();
    }

    public boolean isValidMove(Square source, Square target) {

        if (isMate()) {
            board.displayMessage(Message.isMate);
            return false;
        }

        var sPiece = source.getPiece();
        var tPiece = target.getPiece();

        if (!sPiece.isFoe(tPiece)) {
            return false;
        }

        if (!sPiece.isCorrectMovement(source, target)) {
            board.displayMessage(Message.wrongMove);
            return false;
        }

        if (!isPathFree(source, target)) {
            board.displayMessage(Message.pathBlocked);
            return false;
        }

        if (isCheck() && !isCheckBlock(source, target) && !isKingEscape(source, target)) {
            board.displayMessage(Message.getOutOfCheck);
            return false;
        }

        if (isSelfMadeCheck(source, target)) {
            board.displayMessage(Message.selfMadeCheck);
            return false;
        }

        return true;
    }

    public boolean isValidMove(int row1, int col1, int row2, int col2) {
        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        var source = squares.get(from.index);
        var target = squares.get(to.index);

        return isValidMove(source, target);
    }

    public boolean isAttacked(Square square) {
        return getAttackingSquares(square).size() > 0;
    }

    private boolean isCheck() {
        var kingSquare = getKing();
        return getAttackingSquares(kingSquare).size() > 0;
    }

    private boolean isMate() {

        if (!isCheck()) {
            return false;
        }

        var escapeSquares = getEscapeSquares();

        return !isCheckBlockPossible() && escapeSquares.isEmpty();
    }

    private Square getKing() throws IllegalStateException {

        for (var square : squares) {

            var piece = square.getPiece();

            var isKing = piece instanceof King;

            if (isKing && !piece.isFoe(currentColor)) {
                return square;
            }
        }
        throw new IllegalStateException("King has not been found.");
    }

    /**
     * Retrieves a list of squares for each enemy piece that attacks the square
     * passed as the parameter. Each list consists of the square containing the
     * enemy piece and the path that leads to the target square.
     */
    private List<List<Square>> getAttackingSquares(Square target) {
        var allSquaresLists = new ArrayList<List<Square>>();

        for (var source : squares) {

            if (isAttack(source, target)) {
                var singleSquaresList = new ArrayList<Square>();
                var path = getPath(source, target);

                singleSquaresList.add(source);
                singleSquaresList.addAll(path);

                allSquaresLists.add(singleSquaresList);
            }
        }
        return allSquaresLists;
    }

    private boolean isAttack(Square source, Square target) {
        var piece = source.getPiece();

        return !source.equals(target)
                && piece.isFoe(currentColor)
                && piece.isCorrectMovement(source, target)
                && isPathFree(source, target);
    }

    /**
     * Finds squares for king to escape, in order to get out of check.
     */
    private List<Square> getEscapeSquares() {

        var kingSquare = getKing();
        var king = kingSquare.getPiece();

        return squares.stream()
            .filter(square -> king.isCorrectMovement(kingSquare, square))
            .filter(square -> square.getPiece().isFoe(currentColor))
            .filter(square -> !isAttacked(square))
            .collect(Collectors.toList());
    }

    private boolean isCheckBlockPossible() {

        var kingSquare = getKing();
        var allSquaresLists = getAttackingSquares(kingSquare);

        if (allSquaresLists.size() > 1) {
            return false; /* This means an unblockable double check. */
        }

        var singleSquaresList = allSquaresLists.get(0);

        for (var target : singleSquaresList) {
            for (var source : squares) {

                if (isBlock(source, target))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBlock(Square source, Square target) {
        var piece = source.getPiece();

        return !(piece instanceof King)
                && !piece.isFoe(currentColor)
                && piece.isCorrectMovement(source, target)
                && isPathFree(source, target);
    }

    private boolean isSelfMadeCheck(Square source, Square target) {

        var sourcePiece = source.getPiece();

        source.setPiece(Empty.INSTANCE);
        target.setPiece(sourcePiece);

        var isSelfMadeCheck = isCheck();

        source.setPiece(sourcePiece);
        target.setPiece(Empty.INSTANCE);

        return isSelfMadeCheck;
    }

    private boolean isKingEscape(Square source, Square target) {
        var escapeSquares = getEscapeSquares();

        return source.getPiece() instanceof King && escapeSquares.contains(target);
    }

    private boolean isCheckBlock(Square source, Square target) {
        var kingSquare = getKing();
        var allSquaresLists = getAttackingSquares(kingSquare);

        if (allSquaresLists.size() > 1) {
            return false; /* This means an unblockable double check. */
        }

        var singleSquaresList = allSquaresLists.get(0);

        var isKing = source.getPiece() instanceof King;

        return !isKing && singleSquaresList.contains(target);
    }

    /**
     * Checks if there are pieces on the path between source and target squares
     */
    private boolean isPathFree(Square source, Square target) {

        var path = getPath(source, target);

        return path.stream()
            .filter(this::isNotEmptySquare)
            .count() == 0;
    }

    private boolean isNotEmptySquare(Square square) {
        return !(square.getPiece() instanceof Empty);
    }

    /**
     * Traverses the board in particular direction, inferred from differences
     * between source and target squares. Retrieves a list of squares in
     * straight line between source and target squares.
     */
    private List<Square> getPath(Square source, Square target) {

        var piece = source.getPiece();

        if (piece instanceof Knight) {
            return Collections.emptyList();
        }

        int verticalDiff = calcVerticalDiff(source, target);
        int horizontalDiff = calcHorizontalDiff(source, target);

        int row = source.coord.row + verticalDiff;
        int col = source.coord.col + horizontalDiff;
        var coord = new Coord(row, col);

        List<Square> path = new ArrayList<>();

        while (!coord.equals(target.coord)) {

            path.add(squares.get(coord.index));

            row += verticalDiff;
            col += horizontalDiff;

            coord = new Coord(row, col);
        }

        return path;
    }

    private int calcVerticalDiff(Square source, Square target) {

        var isTargetSameRow = source.coord.row == target.coord.row;
        var isTargetRowLower = source.coord.row < target.coord.row;

        return isTargetSameRow ? 0 : (isTargetRowLower ? 1 : -1);
    }

    private int calcHorizontalDiff(Square source, Square target) {

        var isTargetSameCol = source.coord.col == target.coord.col;
        var isTargetColLower = source.coord.col < target.coord.col;

        return isTargetSameCol ? 0 : (isTargetColLower ? 1 : -1);
    }

    public void changeCurrentColor() {
        var isWhite = currentColor.equals(Color.WHITE);
        currentColor = isWhite ? Color.BLACK : Color.WHITE;
    }

    public void movePiece(final Coord from, final Coord to) {

        var source = squares.get(from.index);
        var target = squares.get(to.index);

        source.movePiece(target);
    }

    public void movePiece(int row1, int col1, int row2, int col2) {

        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        movePiece(from, to);
    }

    public void movePiece(int rank1, char file1, int rank2, char file2) {

        var from = new Coord(rank1, file1);
        var to = new Coord(rank2, file2);

        movePiece(from, to);
    }

    public boolean isCorrectMovement(int row1, int col1, int row2, int col2) {

        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        return isCorrectMovement(from, to);
    }

    public boolean isCorrectMovement(Coord from, Coord to) {

        var source = squares.get(from.index);
        var target = squares.get(to.index);

        return source.isCorrectMovement(target);
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void addMove(Square source, Square target) {
        var move = new Move(source.coord, target.coord);
        moves.add(move);
    }

    public void addMove(Square source, Square target, Piece piece) {
        var move = new Move(source.coord, target.coord, piece);
        moves.add(move);
    }

    public Optional<Move> getLastMove() {
        return Optional.ofNullable(moves.peekLast());
    }

    public Square getSquare(Coord coord) {
        return squares.get(coord.index);
    }

    public Square getSquare(int row, int col) {
        var coord = new Coord(row, col);
        return squares.get(coord.index);
    }

    public void setPiece(Coord coord, Piece piece) {
        var square = squares.get(coord.index);
        square.setPiece(piece);
    }

    public Piece getPiece(int rank, char file) {
        var coord = new Coord(rank, file);
        var square = squares.get(coord.index);
        return square.getPiece();
    }

    public void clearMoves() {
        moves.clear();
    }

    public int getPromotionChoice() {
        return board.getPromotionChoice();
    }

    public void setOptionalSourceEmpty() {
        board.setOptionalSourceEmpty();
    }
}
