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
public class Queen extends ChessPiece {

    public Queen(Color figureColor, int x, int y) {
        super("H", figureColor, x, y);
    }

    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        boolean movement = (Math.abs(x1-x2) == Math.abs(y1-y2))
                || (Math.abs(x1-x2)>0 && Math.abs(y1-y2)==0) 
                || (Math.abs(x1-x2)==0 && Math.abs(y1-y2)>0);
        updateCoordinates(movement, x2, y2);
        return movement;
    }
    
}
