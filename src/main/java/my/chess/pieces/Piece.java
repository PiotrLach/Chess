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
import my.chess.Square;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import lombok.ToString;
import lombok.Getter;

/**
 *
 * @author Piotr Lach
 */
@ToString
abstract public class Piece implements Serializable {
    
    private static final long serialVersionUID = -4958749740116233564L;

    public Piece(PieceName pieceName, Color color, Image image) {
        this.name = pieceName;
        this.color = color;
        this.image = image;
    }

    public void drawImage(Graphics graphics, int x, int y, int size) {
        graphics.drawImage(image, x, y, size, size, null);
    }

    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen
    }

    public boolean isFoe(Piece piece) {
        return !piece.color.equals(this.color);
    }
    
    public boolean isFoe(Color color) {
        return !this.color.equals(color);
    }

    public PieceName getName() {
        return name;
    }
        
    /**
     * Must be called for any deserialized piece, since images are not
     * saved.
     */
    abstract public void setImage();

    abstract public boolean isCorrectMovement(Square source, Square target);

    public final Color color;    
    @ToString.Exclude
    protected transient Image image;
    protected PieceName name;    
    protected boolean wasMoved = false;
    protected static PieceImageLoader imageLoader = PieceImageLoader.INSTANCE; 
}
