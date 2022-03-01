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

import my.chess.pieces.Piece;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public interface Board {

    void repaint();

    int getPromotionChoice();

    void displayMessage(Message message);

    void setGame(String[] layout);

    Optional<Move> getLastMove();

    void addMove(Move move);

    void addMove(Square source, Square target);

    void addMove(Square source, Square target, Piece piece);

    Square getSquare(Coord coord);

    Square getSquare(int row, int col);

    void setOptionalSourceEmpty();

    void movePiece(int row1, int col1, int row2, int col2);

    void setPiece(Coord coord, Piece piece);

    boolean isCorrectMovement(int row1, int col1, int row2, int col2);

    boolean isCorrectMovement(Coord from, Coord to);

    void clearMoves();

    boolean isValidMove(int row1, int col1, int row2, int col2);

    boolean isValidMove(Square source, Square target);

    void changeCurrentColor();

    boolean isAttacked(Square square);

    Deque<Move> getMoves();

    List<Square> getSquares();

    void setDefaultGame();
}
