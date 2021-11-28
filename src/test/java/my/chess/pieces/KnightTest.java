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
import my.chess.Board;
import my.chess.Coord;
import my.chess.Square;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr Lach
 */
public class KnightTest {

    private final Board board = new Board();
    private final Knight knight = new Knight(Color.BLACK, board);
    private final Coord from = new Coord(3, 'C');
    private final Square source = new Square(from);

    @Test
    public void testIsCorrectMovement() {
        var correctCases = List.of(
                new Coord(4, 'E'),
                new Coord(4, 'A'),
                new Coord(2, 'A'),
                new Coord(2, 'E')
        );
        for (var to : correctCases) {
            var target = new Square(to);

            var isCorrect = knight.isCorrectMovement(source, target);
            assertTrue(isCorrect);
        }
    }

    @Test
    public void testIsIncorrectMovement() {
        var incorrectCases = List.of(
                new Coord(4, 'D'),
                new Coord(2, 'B'),
                new Coord(4, 'B'),
                new Coord(2, 'D'),
                new Coord(3, 'A'),
                new Coord(1, 'C'),
                new Coord(3, 'F'),
                new Coord(6, 'C'),
                new Coord(0, 'B')
        );

        for (var to : incorrectCases) {
            var target = new Square(to);

            var isIncorrect = !knight.isCorrectMovement(source, target);
            assertTrue(isIncorrect);
        }
    }


}
