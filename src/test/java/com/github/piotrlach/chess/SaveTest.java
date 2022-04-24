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

package com.github.piotrlach.chess;

import lombok.val;
import com.github.piotrlach.chess.gui.board.boards.MockBoard;
import com.github.piotrlach.chess.logic.Save;
import com.github.piotrlach.chess.logic.pieces.Knight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SaveTest {

    @Test
    public void loadsCorrectly() {
        val board = new MockBoard();
        val url = Save.class.getClassLoader().getResource("save.json");
        Assertions.assertNotNull(url);

        board.loadGame(url.getPath());
        val squares = board.getLogic().getSquares();
        val squareWithKnight = squares.stream()
                .filter(square -> square.coord.index == 16)
                .findAny()
                .orElseThrow(IllegalStateException::new);
        val piece = squareWithKnight.getPiece();
        Assertions.assertEquals(Knight.class, piece.getClass());
    }
}
