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
package com.github.piotrlach.chess.logic.pieces;

import java.util.List;

import com.github.piotrlach.chess.logic.square.Square;
import com.github.piotrlach.chess.logic.square.Coord;
import com.github.piotrlach.chess.logic.MockBoard;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Piotr Lach
 */
public class KingTest {

    @Test
    public void testIsCorrectMovement() {

        var board = new MockBoard();
        var king = new King("W", board.getLogic());
        var from = new Coord(3, 'C');
        var source = new Square(from);

        var correctCases = List.of(
                new Coord(2, 'B'),
                new Coord(2, 'C'),
                new Coord(2, 'D'),
                new Coord(3, 'B'),
                new Coord(3, 'D'),
                new Coord(4, 'B'),
                new Coord(4, 'C'),
                new Coord(4, 'D')
        );

        for (var to : correctCases) {
            var target = new Square(to);

            var isCorrect = king.isCorrectMovement(source, target);
            Assertions.assertTrue(isCorrect);
        }
    }

    @Test
    public void testIsIncorrectMovement() {

        var board = new MockBoard();
        var king = new King("B", board.getLogic());
        var from = new Coord(3, 'C');
        var source = new Square(from);

        var incorrectCases = List.of(
                new Coord(1, 'B'),
                new Coord(1, 'C'),
                new Coord(1, 'D'),
                new Coord(3, 'A'),
                new Coord(3, 'E'),
                new Coord(5, 'B'),
                new Coord(5, 'C'),
                new Coord(5, 'D')
        );

        for (var to : incorrectCases) {
            var target = new Square(to);

            var isIncorrect = !king.isCorrectMovement(source, target);
            Assertions.assertTrue(isIncorrect);
        }
    }

    @Test
    public void castlingTest() {
        var board = new MockBoard();
        var logic = board.getLogic();

        String[] layout = {
            " ; "," ; "," ; "," ; "," ; "," ; ","R;B"," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            "R;W"," ; "," ; "," ; ","K;W"," ; "," ; ","R;W"
        };

        logic.setLayout(layout);

        Assertions.assertFalse(logic.isCorrectMovement(0, 4, 0, 6));
        Assertions.assertTrue(logic.isCorrectMovement(0, 4, 0, 2));

    }

    @Test
    public void mateTest() {
        val board = new MockBoard();
        val logic = board.getLogic();

        String[] layout = {
                /* A     B     C     D     E     F     G     H  */
        /* 8 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; ","K;B", /* 8 */
        /* 7 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 7 */
        /* 6 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 6 */
        /* 5 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 5 */
        /* 4 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 4 */
        /* 3 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 3 */
        /* 2 */ "R;B"," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 2 */
        /* 1 */ "R;B"," ; "," ; ","K;W"," ; "," ; "," ; "," ; "  /* 1 */
                /* A     B     C     D     E     F     G     H  */
        };

        logic.setLayout(layout);

        Assertions.assertFalse(logic.movePiece(1, 'D', 2, 'D'));
        Assertions.assertFalse(logic.movePiece(1, 'D', 2, 'C'));
        Assertions.assertFalse(logic.movePiece(1, 'D', 2, 'E'));
        Assertions.assertFalse(logic.movePiece(1, 'D', 1, 'C'));
        Assertions.assertFalse(logic.movePiece(1, 'D', 1, 'E'));
    }

}
