/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Image;

/**
 *
 * @author bruce
 */
public class Knight extends ChessPiece{

    public Knight(Color figureColor, Image img/*, int x, int y*/) {
        super("S", figureColor,img/*, x, y*/);
    }

    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        boolean movement = (Math.abs(x1-x2)==2 && Math.abs(y1-y2)==1)
                            || (Math.abs(x1-x2)==1 && Math.abs(y1-y2)==2);
        //updateCoordinates(movement, x2, y2);
        return movement;
    }
    
}
