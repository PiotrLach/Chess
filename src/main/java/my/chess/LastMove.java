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

import java.awt.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.chess.pieces.Pawn;

/**
 *
 * @author Piotr Lach
 */
@NoArgsConstructor
public class LastMove {
       
    private Square source;
    @Getter
    private Square target;
    public void setLastMove(Square source, Square target) {
        this.source = source;
        this.target = target;
    }
    
    public void setEmpty() {
        source = null;
        target = null;
    }
    
    public boolean isTwoSquaresAdvancedEnemyPawn(Color color) {
        
        if (source == null || target == null) {
            return false;
        }
        
        var piece = target.getPiece();        
        if (piece == null) {
            return false;
        }
        
        if (!(piece instanceof Pawn)) {
            return false;
        }
        
        int vDiff; // vertical difference
        vDiff = Math.abs(source.coord.row - target.coord.row);
        
        if (vDiff != 2) {
            return false;
        }
        
        if (!piece.isFoe(color)) {
            return false;
        }
                
        
        return true;
        
    }
}
