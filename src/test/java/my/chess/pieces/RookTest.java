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
import my.chess.Square;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr Lach
 */
public class RookTest {

    private final Rook rook = new Rook(Color.BLACK, null);
    private final Coord from = new Coord(3,3);
    private final Square source = new Square(from);

    @Test
    public void testIsCorrectMovement() {
        var correctCases = List.of(
                new Coord(3, 1),
                new Coord(1, 3),
                new Coord(3, 6),
                new Coord(6, 3)
        );
        for (var to : correctCases) {
            var target = new Square(to);

            var isCorrect = rook.isCorrectMovement(source, target);
            assertTrue(isCorrect);
        }
    }

    @Test
    public void testIsIncorrectMovement() {
        var incorrectCases = List.of(
                new Coord(1, 1),
                new Coord(7, 7),
                new Coord(2, 2),
                new Coord(4, 5)
        );

        for (var to : incorrectCases) {
            var target = new Square(to);

            var isIncorrect = !rook.isCorrectMovement(source, target);
            assertTrue(isIncorrect);
        }
    }
}
