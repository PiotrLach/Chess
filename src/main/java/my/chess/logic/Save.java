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
package my.chess.logic;

import lombok.RequiredArgsConstructor;
import my.chess.gui.Board;
import my.chess.gui.Message;
import my.chess.logic.pieces.Empty;
import my.chess.logic.pieces.Piece;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Save {

    private final Board board;
    private final List<? extends Square> squares;

    public void loadGame(String filename) {
        var moves = readObject(filename);

        if (moves.isEmpty()) {
            return;
        }

        board.setDefaultGame();

        for (var move : moves) {
            Coord from = move.source;
            Coord to = move.target;

            Square source = squares.get(from.index);
            Square target = squares.get(to.index);

            Piece piece;
            /* Necessary for promoted pawns */
            if (!((piece = move.getPromotedPiece()) instanceof Empty)) {
                piece.setLogic(board.getLogic());
                piece.setImage();

                source.setPiece(Empty.INSTANCE);
                target.setPiece(piece);

                board.changeCurrentColor();
            } else {
                piece = source.getPiece();
                piece.move(source, target);
            }
        }

        board.repaint();
    }

    public void saveGame(String fileName) {

        var moves = board.getMoves();

        writeObject(moves, fileName);

    }

    @SuppressWarnings("unchecked")
    private Deque<Move> readObject(String filename) {
        try {

            var fileInputStream = new FileInputStream(filename);
            var objectInputStream = new ObjectInputStream(fileInputStream);

            var object = objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return (Deque<Move>) object;

        } catch (IOException exception) {

            board.displayMessage(Message.loadError);

        } catch (ClassNotFoundException exception) {

            board.displayMessage(Message.wrongFormat);

        }
        return new LinkedList<>();
    }

    private <T> void writeObject(T t, String filename) {
        try {

            var fileOutputStream = new FileOutputStream(filename);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            objectOutputStream.close();

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException exception) {

            board.displayMessage(Message.saveError);

        }
    }

}
