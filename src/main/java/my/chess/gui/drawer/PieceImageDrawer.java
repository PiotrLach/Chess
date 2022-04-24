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

package my.chess.gui.drawer;

import lombok.val;
import my.chess.logic.pieces.Piece;

import java.awt.*;

public class PieceImageDrawer {

    private static final PieceImageLoader imageLoader = PieceImageLoader.INSTANCE;

    public static void drawImage(final Graphics graphics,
                          final int x,
                          final int y,
                          final int size,
                          final Piece piece) {
        val image = imageLoader.getImage(piece.symbol, piece.color);
        graphics.drawImage(image, x, y, size, size, null);
    }
}
