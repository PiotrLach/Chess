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
package my.chess;

import my.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Piotr Lach
 */
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Square extends Rectangle {
        
    private Piece piece;    
    private boolean highlighted = false;   
    @EqualsAndHashCode.Include
    public final Coord coord;

    public Square(int x, int y, int width, int height, Coord coord) {
        super(x, y, width, height);       
        this.coord = coord;
    }

    public void draw(Graphics graphics) {        
        
        var isRowOdd = coord.row % 2 == 1;
        var isColOdd = coord.col % 2 == 1;
        
        var isWhite = (isRowOdd && !isColOdd) || (!isRowOdd && isColOdd);
                
        if (isWhite) {
            graphics.setColor(myWhite);
        } else {
            graphics.setColor(myBrown);
        }
        graphics.fillRect(x, y, width, height);
    }

    public void highlightSquare(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(x, y, width, height);
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }    
    private final Color myWhite = new Color(255, 255, 204);    
    private final Color myBrown = new Color(153, 102, 0);

}
