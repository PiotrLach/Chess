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

/**
 *
 * @author Piotr Lach
 */
public class KingTest {

    @Test
    public void testIsCorrectMovement() {

        var board = new Board();
        var king = new King(Color.WHITE, board);
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
            assertTrue(isCorrect);
        }
    }

    @Test
    public void testIsIncorrectMovement() {

        var board = new Board();
        var king = new King(Color.WHITE, board);
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
            assertTrue(isIncorrect);
        }
    }

    @Test
    public void castlingTest() {
        final var board = new Board();

        LayoutDefinition layoutDefinition = (squares) -> {
            squares.get(0).setPiece(new Rook  (Color.WHITE, board));
            squares.get(4).setPiece(new King  (Color.WHITE, board));
            squares.get(7).setPiece(new Rook  (Color.WHITE, board));
            squares.get(7 * 8 + 6).setPiece(new Rook  (Color.BLACK, board));
        };

        board.setGame(layoutDefinition);

        var source =  board.getSquare(new Coord(4));
        var target1 = board.getSquare(new Coord(2));
        var target2 = board.getSquare(new Coord(6));
        var king = source.getPiece();

        var isCorrect = king.isCorrectMovement(source, target1);
        var isIncorrect = !king.isCorrectMovement(source, target2);

        assertTrue(isCorrect);
        assertTrue(isIncorrect);

    }

}
