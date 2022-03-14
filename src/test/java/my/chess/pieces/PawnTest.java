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
import my.chess.gui.board.Board;
import my.chess.logic.Coord;
import my.chess.logic.Logic;
import my.chess.gui.board.boards.MockBoard;
import my.chess.logic.pieces.Queen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Piotr Lach
 */
public class PawnTest {

    Board board = new MockBoard();
    Logic logic = board.getLogic();
    Coord from = new Coord(2, 'C');

    @Test
    public void isCorrectMovement() {

        setBoardLayout();

        List<Coord> correctCases = List.of(
            new Coord(3, 'B'),
            new Coord(3, 'C'),
            new Coord(4, 'C')
        );

        for (var to : correctCases) {

            var isCorrect = logic.isCorrectMovement(from, to);
            Assertions.assertTrue(isCorrect);
        }
    }

    @Test
    public void isIncorrectMovement() {

        setBoardLayout();

        List<Coord> incorrectCases = List.of(
                new Coord(1, 'C'),
                new Coord(1, 'B'),
                new Coord(1, 'D'),
                new Coord(2, 'D'),
                new Coord(2, 'B'),
                new Coord(3, 'D')
        );

        for (var to : incorrectCases) {

            var isIncorrect = !logic.isCorrectMovement(from, to);
            Assertions.assertTrue(isIncorrect);
        }
    }

    private void setBoardLayout() {
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
        logic.setLayout(layout);
    }

    @Test
    public void enPassantTest() {
        String[] layout = {
           /* A     B     C     D     E     F     G     H  */
    /* 8 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; ","K;B", /* 8 */
    /* 7 */ " ; "," ; "," ; ","H;B"," ; "," ; "," ; "," ; ", /* 7 */
    /* 6 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 6 */
    /* 5 */ " ; "," ; ","L;W"," ; "," ; "," ; "," ; "," ; ", /* 5 */
    /* 4 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 4 */
    /* 3 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 3 */
    /* 2 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 2 */
    /* 1 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; ","K;W"  /* 1 */
           /* A     B     C     D     E     F     G     H  */
        };
        logic.setLayout(layout);
        Assertions.assertDoesNotThrow(() -> logic.movePiece(6, 3, 4, 3));
        Assertions.assertTrue(logic.isValidMove(4, 2, 5, 3));
    }

    @Test
    public void promotionTest() {
        String[] layout = {
           /* A     B     C     D     E     F     G     H  */
    /* 8 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; ","K;B", /* 8 */
    /* 7 */ " ; "," ; "," ; ","L;W"," ; "," ; "," ; "," ; ", /* 7 */
    /* 6 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 6 */
    /* 5 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 5 */
    /* 4 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 4 */
    /* 3 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 3 */
    /* 2 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ", /* 2 */
    /* 1 */ " ; "," ; "," ; "," ; "," ; "," ; "," ; ","K;W"  /* 1 */
           /* A     B     C     D     E     F     G     H  */
        };
        logic.setLayout(layout);
        Assertions.assertDoesNotThrow(() -> logic.movePiece(7, 'D', 8, 'D'));
        Assertions.assertTrue(logic.getPiece(8, 'D') instanceof Queen);
    }
}
