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

import my.chess.*;
import my.chess.gui.Board;
import my.chess.logic.Coord;
import my.chess.logic.Logic;
import my.chess.logic.Square;
import my.chess.logic.pieces.Bishop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Piotr Lach
 */
public class BishopTest {

    private final Board board = new MockBoard();
    private final Logic logic = board.getLogic();
    private final Bishop bishop = new Bishop(Color.BLACK, logic );
    private final Coord from = new Coord(3, 'C');
    private final Square source = new Square(from);

    @Test
    public void testIsCorrectMovement() {
        var correctCases = List.of(
                new Coord(4, 'D'),
                new Coord(2, 'B'),
                new Coord(4, 'B'),
                new Coord(2, 'D')
        );
        for (var to : correctCases) {
            var target = new Square(to);

            var isCorrect = bishop.isCorrectMovement(source, target);
            Assertions.assertTrue(isCorrect);
        }
    }

    @Test
    public void testIsIncorrectMovement() {
        var incorrectCases = List.of(
                new Coord(4, 'E'),
                new Coord(3, 'E'),
                new Coord(3, 'A'),
                new Coord(4, 'C')
        );

        for (var to : incorrectCases) {
            var target = new Square(to);

            var isIncorrect = !bishop.isCorrectMovement(source, target);
            Assertions.assertTrue(isIncorrect);
        }
    }
}
