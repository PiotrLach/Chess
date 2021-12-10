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
import my.chess.LayoutDefinition;
import my.chess.Square;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Piotr Lach
 */
public class PawnTest {

    final List<Coord> correctCases = List.of(
        new Coord(3, 'B'),
        new Coord(3, 'C'),
        new Coord(4, 'C')
    );

    final List<Coord> incorrectCases = List.of(
            new Coord(1, 'C'),
            new Coord(1, 'B'),
            new Coord(1, 'D'),
            new Coord(2, 'D'),
            new Coord(2, 'B'),
            new Coord(3, 'D')
    );

    final Board board = new Board();
    final Pawn pawn = new Pawn(Color.BLACK, Piece.Name.Pawn1, board);
    final Coord from = new Coord(2, 'C');
    final Coord toEnemy = new Coord(3, 'B');
    final Rook enemy = new Rook(Color.WHITE, board);
    final LayoutDefinition layoutDefinition = (squares) -> {
        squares.get(from.index).setPiece(pawn);
        squares.get(toEnemy.index).setPiece(enemy);
    };

    final Square source = board.getSquare(from);

    @Before
    public void setUp() {
        board.setGame(layoutDefinition);
    }

    @Test
    public void isCorrectMovement() {

        for (var to : correctCases) {
            var target = board.getSquare(to);

            var isCorrect = pawn.isCorrectMovement(source, target);
            assertTrue(isCorrect);
        }
    }

    @Test
    public void isIncorrectMovement() {
        for (var to : incorrectCases) {
            var target = board.getSquare(to);

            var isIncorrect = !pawn.isCorrectMovement(source, target);
            assertTrue(isIncorrect);
        }
    }
}
