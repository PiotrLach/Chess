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
package my.chess;

import java.awt.Color;
import my.chess.pieces.Empty;
import my.chess.pieces.PieceFactory;
import my.chess.pieces.Rook;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Piotr Lach
 */
public class PieceFactoryTest {
    private final Board board = new Board();
    private final PieceFactory pieceFactory = new PieceFactory(board);

    @Test
    public void testPieceFactory() {
        var rook1 = new Rook(Color.WHITE, board);
        var rook2 = pieceFactory.getPiece("R;W");
        assertEquals(rook1, rook2);

        var empty1 = Empty.INSTANCE;
        var empty2 = pieceFactory.getPiece(" ; ");

        assertEquals(empty1, empty2);
    }
}
