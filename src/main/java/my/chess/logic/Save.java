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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import my.chess.gui.Message;
import my.chess.gui.board.Board;
import my.chess.logic.pieces.Empty;
import my.chess.logic.pieces.PieceFactory;
import my.chess.logic.square.Square;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Save {

    private final Board board;
    private final List<? extends Square> squares;

    public void loadGame(final String filename) {
        val moves = readObject(filename);

        board.setDefaultGame();

        for (val move : moves) {
            val source = squares.get(move.from);
            val target = squares.get(move.to);

            /* Necessary for promoted pawns */
            if (!move.getPromoted().equals(Empty.INSTANCE.string)) {
                val pieceFactory = new PieceFactory(board.getLogic());
                val piece = pieceFactory.getPiece(move.getPromoted());

                source.setPiece(Empty.INSTANCE);
                target.setPiece(piece);

                board.changeCurrentColor();
            } else {
                val piece = source.getPiece();
                piece.move(source, target);
            }
        }
        board.repaint();
    }

    public void saveGame(final String fileName) {
        writeObject(board.getMoves(), fileName);
    }

    private Move[] readObject(final String filename) {
        try {
            val objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(filename), Move[].class);
        } catch (final IOException exception) {
            board.displayMessage(Message.loadError);
        }
        return new Move[] {};
    }

    private <T> void writeObject(final T t, final String filename) {
        try {
            val objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filename), t);
        } catch (final IOException exception) {
            board.displayMessage(Message.saveError);
        }
    }
}