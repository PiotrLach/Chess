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
package com.github.piotrlach.chess.gui.board.boards;

import com.github.piotrlach.chess.gui.Message;
import com.github.piotrlach.chess.gui.board.Board;
import com.github.piotrlach.chess.logic.Logic;
import com.github.piotrlach.chess.logic.Move;
import com.github.piotrlach.chess.logic.Save;
import com.github.piotrlach.chess.logic.square.Square;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class MockBoard implements Board {

    @Getter
    private final Deque<Move> moves = new LinkedList<>();
    private final List<Square> squares = new ArrayList<>();
    @Getter
    private final Logic logic = new Logic(this, squares, moves);
    private final Save save = new Save(this, squares);

    public MockBoard() {
        createSquares();
    }

    public void createSquares() {
        for (int idx = 0; idx < 64; idx++) {
            var square = new Square(idx);
            squares.add(square);
        }
    }

    public void loadGame(final String fileName) {
        save.loadGame(fileName);
    }

    @Override
    public int getPromotionChoice() {
        return 0;
    }

    @Override
    public void changeCurrentColor() {
        logic.changeCurrentColor();
    }

    @Override
    public void setDefaultGame() {
        logic.setDefaultLayout();
    }

    @Override
    public void displayMessage(Message message) {

    }

}
