/*
 * Copyright (C) 2021 Piotr Lach
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
package my.chess.pieces;

import java.awt.Color;
import java.util.List;

import my.chess.Coord;
import my.chess.MockBoard;
import my.chess.Square;
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
        var king = new King(Color.WHITE, board.getLogic());
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
        var king = new King(Color.WHITE, board.getLogic());
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

}
