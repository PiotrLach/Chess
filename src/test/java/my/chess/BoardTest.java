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
package my.chess;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Piotr Lach
 */
public class BoardTest {

    @Test
    public void isPathFreeTest() {
        var board = new Board();
        var source = board.getSquare(new Coord(1, 'A'));
        var target = board.getSquare(new Coord(6, 'A'));

        assertFalse(board.isPathFree(source, target));
    }

    @Test
    public void getPathTest() {
        System.out.println("getPathTest");
        var board = new Board();
        var source = board.getSquare(new Coord(1, 'A'));
        var target = board.getSquare(new Coord(6, 'A'));

        var path = board.getPath(source, target);

        var expected = List.of(
            board.getSquare(new Coord(2, 'A')),
            board.getSquare(new Coord(3, 'A')),
            board.getSquare(new Coord(4, 'A')),
            board.getSquare(new Coord(5, 'A'))
        );

        assertEquals(path, expected);
    }
}
