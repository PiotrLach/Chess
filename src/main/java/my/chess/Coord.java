/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Piotr Lach
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Coord {        
    public final int row;
    public final int col; 
    
    public boolean isOutOfBounds() {
        return row > 7 || row < 0 || col < 0 || col > 7;
    }
}
