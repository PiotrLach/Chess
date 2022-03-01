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
package my.chess;

import lombok.Getter;
import my.chess.pieces.Piece;

import java.util.*;

public class MockBoard implements Board {

    @Getter
    private final Deque<Move> moves = new LinkedList<>();
    @Getter
    private final List<Square> squares = new ArrayList<>();
    private final Logic logic = new Logic(this, squares, moves);

    public MockBoard() {
        createSquares();
    }

    public void createSquares() {
        for (int idx = 0; idx < 64; idx++) {
            var coord = new Coord(idx);
            var square = new Square(coord);
            squares.add(square);
        }
    }

    @Override
    public int getPromotionChoice() {
        return 0;
    }

    @Override
    public void setGame(String[] layout) {
        logic.setLayout(layout);
    }

    @Override
    public Optional<Move> getLastMove() {
        return logic.getLastMove();
    }

    @Override
    public void addMove(Move move) {
        logic.addMove(move);
    }

    @Override
    public void addMove(Square source, Square target) {
        logic.addMove(source, target);
    }

    @Override
    public void addMove(Square source, Square target, Piece piece) {
        logic.addMove(source, target, piece);
    }

    @Override
    public Square getSquare(Coord coord) {
        return logic.getSquare(coord);
    }

    @Override
    public Square getSquare(int row, int col) {
        return logic.getSquare(row, col);
    }

    @Override
    public void movePiece(int row1, int col1, int row2, int col2) {
        logic.movePiece(row1, col1, row2, col2);
    }

    @Override
    public void setPiece(Coord coord, Piece piece) {
        logic.setPiece(coord, piece);
    }

    @Override
    public boolean isCorrectMovement(int row1, int col1, int row2, int col2) {
        return logic.isCorrectMovement(row1, col1, row2, col2);
    }

    @Override
    public boolean isCorrectMovement(Coord from, Coord to) {
        return logic.isCorrectMovement(from, to);
    }

    @Override
    public void clearMoves() {
        logic.clearMoves();
    }

    @Override
    public boolean isValidMove(int row1, int col1, int row2, int col2) {
        return logic.isValidMove(row1, col1, row2, col2);
    }

    @Override
    public boolean isValidMove(Square source, Square target) {
        return logic.isValidMove(source, target);
    }

    @Override
    public void changeCurrentColor() {
        logic.changeCurrentColor();
    }

    @Override
    public boolean isAttacked(Square square) {
        return logic.isAttacked(square);
    }

    @Override
    public void setOptionalSourceEmpty() {

    }

    @Override
    public void setDefaultGame() {

    }

    @Override
    public void repaint() {

    }

    @Override
    public void displayMessage(Message message) {

    }

}
