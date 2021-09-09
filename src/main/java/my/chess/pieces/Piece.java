/* 
 * Java chess game implementation
 * Copyright (C) 2021 Piotr Lach
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
*/
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Piotr Lach
 */
abstract public class Piece {

    public Piece(PieceName pieceName, Color color, Image image) {
        this.name = pieceName;
        this.color = color;
        this.image = image;
    }

    public void drawImage(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(image, x, y, width, height, null);
    }

    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen
    }

    public boolean isFoe(Piece piece) {
        return piece.color != color;
    }

    public PieceName getName() {
        return name;
    }

    abstract public boolean isCorrectMovement(Coord source, Coord target);

    public final Color color;
    protected Image image;
    protected PieceName name;
    protected static PieceImageLoader imageLoader = PieceImageLoader.INSTANCE; 
}
