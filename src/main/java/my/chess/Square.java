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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author Piotr Lach
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Square extends Rectangle {
        
    @Getter
    @Setter
    @ToString.Include
    private Piece piece;    
    @Getter
    @Setter
    private boolean highlighted = false;   
    @EqualsAndHashCode.Include
    @ToString.Include
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
            graphics.setColor(MY_WHITE);
        } else {
            graphics.setColor(MY_BROWN);
        }
        graphics.fillRect(x, y, width, height);
    }
    
    public ImmutablePair<Coord, Piece> getPair() {
        return new ImmutablePair<>(coord, piece);
    }

    public void highlightSquare(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(x, y, width, height);
    }
    private static final Color MY_WHITE = new Color(255, 255, 204);    
    private static final Color MY_BROWN = new Color(153, 102, 0);

}
