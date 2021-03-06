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
package com.github.piotrlach.chess.logic;

import com.github.piotrlach.chess.logic.pieces.*;
import com.github.piotrlach.chess.logic.square.Coord;
import com.github.piotrlach.chess.logic.square.Square;
import lombok.Getter;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Piotr Lach
 */
public class Logic {

    private final Board board;
    @Getter
    private final List<? extends Square> squares;
    private final PieceFactory pieceFactory;
    private final Deque<Move> moves;
    @Getter
    private String currentColor = "W";

    public Logic(Board board, List<? extends Square> squares, Deque<Move> moves) {
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
    }

    private void clearBoard() {
        currentColor = "W";

        for (var square : squares) {
            square.setPiece(Empty.INSTANCE);
        }

        clearMoves();
    }

    public boolean isValidMove(Square source, Square target) {
        if (isMate()) {
            board.displayMessage(Message.isMate);
            return false;
        }

        var attacker = source.getPiece();
        var defender = target.getPiece();

        if (!attacker.isFoe(defender)) {
            return false;
        }

        if (!attacker.isCorrectMovement(source, target)) {
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
        return getAttackingSquares(square)
                .size() > 0;
    }

    private boolean isCheck() {
        return getAttackingSquares(getKingSquare())
                .size() > 0;
    }

    private boolean isMate() {
        if (!isCheck()) {
            return false;
        }

        return !isCheckBlockPossible() && getEscapeSquares().isEmpty();
    }

    private Square getKingSquare() throws IllegalStateException {
        return squares.stream()
                .filter(Square::hasKing)
                .filter(square -> !square.hasFoe(currentColor))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("King has not been found!"));
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

        var kingSquare = getKingSquare();
        var king = kingSquare.getPiece();

        return squares.stream()
            .filter(square -> king.isCorrectMovement(kingSquare, square))
            .filter(square -> square.getPiece().isFoe(currentColor))
            .filter(square -> !isAttacked(square))
            .collect(Collectors.toList());
    }

    private boolean isCheckBlockPossible() {
        var kingSquare = getKingSquare();
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
        return source.hasKing() && getEscapeSquares().contains(target);
    }

    private boolean isCheckBlock(Square source, Square target) {
        var kingSquare = getKingSquare();
        var allSquaresLists = getAttackingSquares(kingSquare);

        if (allSquaresLists.size() > 1) {
            return false; /* This means an unblockable double check. */
        }

        var singleSquaresList = allSquaresLists.get(0);

        return !source.hasKing() && singleSquaresList.contains(target);
    }

    /**
     * Checks if there are pieces on the path between source and target squares
     */
    private boolean isPathFree(Square source, Square target) {
        return getPath(source, target)
                .stream()
                .noneMatch(this::isNotEmptySquare);
    }

    private boolean isNotEmptySquare(Square square) {
        return !(square.getPiece() instanceof Empty);
    }

    /**
     * Traverses the board in particular direction, inferred from differences
     * between source and target squares. Retrieves a list of squares in
     * straight line between source and target squares.
     */
    private List<? extends Square> getPath(Square source, Square target) {
        var piece = source.getPiece();

        if (piece instanceof Knight) {
            return Collections.emptyList();
        }

        int verticalDiff = calcDiff(source.coord.row, target.coord.row);
        int horizontalDiff = calcDiff(source.coord.col, target.coord.col);
        val seed = source.coord.increment(verticalDiff, horizontalDiff);

        return Stream.iterate(seed, coord -> coord.increment(verticalDiff, horizontalDiff))
                .limit(seed.distance(target.coord))
                .map(coord -> squares.get(coord.index))
                .toList();
    }

    private int calcDiff(final int source, final int target) {
        var isTargetSame = source == target;
        var isTargetLower = source < target;

        return isTargetSame ? 0 : (isTargetLower ? 1 : -1);
    }

    public void changeCurrentColor() {
        var isWhite = currentColor.equals("W");
        currentColor = isWhite ? "B" : "W";
    }

    public boolean movePiece(final Coord from, final Coord to) {
        var source = squares.get(from.index);
        var target = squares.get(to.index);

        return source.movePieceTo(target);
    }

    public void movePiece(int row1, int col1, int row2, int col2) {
        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        movePiece(from, to);
    }

    public boolean movePiece(int rank1, char file1, int rank2, char file2) {
        var from = new Coord(rank1, file1);
        var to = new Coord(rank2, file2);

        return movePiece(from, to);
    }

    public boolean isCorrectMovement(int row1, int col1, int row2, int col2) {
        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        return isCorrectMovement(from, to);
    }

    public boolean isCorrectMovement(int rank1, char file1, int rank2, char file2) {
        val from = new Coord(rank1, file1);
        val to = new Coord(rank2, file2);

        return isCorrectMovement(from, to);
    }

    public boolean isCorrectMovement(Coord from, Coord to) {
        var source = squares.get(from.index);
        var target = squares.get(to.index);

        return source.isCorrectMovement(target);
    }

    public void addMove(Square source, Square target) {
        var move = new Move(source.coord.index, target.coord.index);
        moves.add(move);
    }

    public void addMove(Square source, Square target, Piece piece) {
        var move = new Move(source.coord.index, target.coord.index, piece.toString());
        moves.add(move);
    }

    public Optional<Move> getLastMove() {
        return Optional.ofNullable(moves.peekLast());
    }

    public Square getSquare(Coord coord) {
        return squares.get(coord.index);
    }

    public Square getSquare(final int index) {
        return squares.get(index);
    }

    public Square getSquare(int row, int col) {
        var coord = new Coord(row, col);
        return squares.get(coord.index);
    }

    public void setPiece(final int index, final Piece piece) {
        var square = squares.get(index);
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

    public void setDefaultLayout() {
        String[] layout = {
                "R;B","N;B","B;B","Q;B","K;B","B;B","N;B","R;B",
                "H;B","H;B","H;B","H;B","H;B","H;B","H;B","H;B",
                " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
                " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
                " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
                " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
                "L;W","L;W","L;W","L;W","L;W","L;W","L;W","L;W",
                "R;W","N;W","B;W","Q;W","K;W","B;W","N;W","R;W"
        };
        setLayout(layout);
    }

}
