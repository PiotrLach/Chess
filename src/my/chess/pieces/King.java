/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;

/**
 *
 * @author bruce
 */
public class King extends ChessPiece {

    public King(Color figureColor) {
        super("K", figureColor);
    }

    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        return (Math.abs(x1-x2) == 1 && Math.abs(y1-y2) == 1 ) 
                || (Math.abs(x1-x2)==1 && Math.abs(y1-y2)==0) 
                || (Math.abs(x1-x2)==0 && Math.abs(y1-y2)==1);
    }
    
}
