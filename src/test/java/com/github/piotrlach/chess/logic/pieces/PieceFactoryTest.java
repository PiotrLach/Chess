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
package com.github.piotrlach.chess.logic.pieces;

import com.github.piotrlach.chess.logic.Board;
import com.github.piotrlach.chess.logic.MockBoard;
import com.github.piotrlach.chess.logic.Logic;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Piotr Lach
 */
public class PieceFactoryTest {
    private final Board board = new MockBoard();
    private final Logic logic = board.getLogic();
    private final PieceFactory pieceFactory = new PieceFactory(logic);

    @Test
    public void rook() {
        val rook = pieceFactory.getPiece("R;W");
        Assertions.assertEquals(Rook.class, rook.getClass());
    }

    @Test
    public void empty() {
        val empty = pieceFactory.getPiece(" ; ");
        Assertions.assertEquals(Empty.class, empty.getClass());
    }

    @Test
    public void king() {
        val king = pieceFactory.getPiece("K;W");
        Assertions.assertEquals(King.class, king.getClass());
    }

    @Test
    public void knight() {
        val knight = pieceFactory.getPiece("N;W");
        Assertions.assertEquals(Knight.class, knight.getClass());
    }

    @Test
    public void queen() {
        val queen = pieceFactory.getPiece("Q;W");
        Assertions.assertEquals(Queen.class, queen.getClass());
    }

    @Test
    public void bishop() {
        val bishop = pieceFactory.getPiece("B;W");
        Assertions.assertEquals(Bishop.class, bishop.getClass());
    }

    @Test
    public void pawn() {
        val pawn1 = pieceFactory.getPiece("L;W");
        Assertions.assertEquals(Pawn.class, pawn1.getClass());
        val pawn2 = pieceFactory.getPiece("H;W");
        Assertions.assertEquals(Pawn.class, pawn2.getClass());
    }

    @Test
    public void invalidPiece() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> pieceFactory.getPiece("Z;W"));
    }
   @Test
    public void invalidColor() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> pieceFactory.getPiece("B;Z"));
    }
}
