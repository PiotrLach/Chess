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

import java.util.List;
import my.chess.Board;
import my.chess.Coord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Piotr Lach
 */
public class PawnTest {

    Board board = new Board();
    Coord from = new Coord(2, 'C');

    @BeforeAll
    public void setUp() {
        String[] layout = {
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; ","R;W"," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; ","L;B"," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; "
        };
        board.setGame(layout);
    }

    @Test
    public void isCorrectMovement() {

        List<Coord> correctCases = List.of(
            new Coord(3, 'B'),
            new Coord(3, 'C'),
            new Coord(4, 'C')
        );

        for (var to : correctCases) {

            var isCorrect = board.isCorrectMovement(from, to);
            Assertions.assertTrue(isCorrect);
        }
    }

    @Test
    public void isIncorrectMovement() {

        List<Coord> incorrectCases = List.of(
                new Coord(1, 'C'),
                new Coord(1, 'B'),
                new Coord(1, 'D'),
                new Coord(2, 'D'),
                new Coord(2, 'B'),
                new Coord(3, 'D')
        );

        for (var to : incorrectCases) {

            var isIncorrect = !board.isCorrectMovement(from, to);
            Assertions.assertTrue(isIncorrect);
        }
    }
}
