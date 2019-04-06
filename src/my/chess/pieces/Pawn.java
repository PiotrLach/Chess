/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.ChessField;

/**
 *
 * @author bruce
 */
public class Pawn extends ChessPiece{
          
    public enum Movement{
        STRAIGHT,
        DIAGONAL;
    }
    private boolean diagonalLeftIsOccupied, diagonalRightIsOccupied;
    private boolean twoMovementsAvailable;
    private int startingX;
    
    public Pawn(Color figureColor, int startingX) {
        super("P", figureColor);
        twoMovementsAvailable=true;
        this.startingX=startingX;        
    } 
    public void checkDiagonalLeft(ChessField c){
        /* Przekazywanie w pathIsFree() */
        diagonalLeftIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
    }
    public void checkDiagonalRight(ChessField c){
        /* Przekazywanie w pathIsFree() 
          if selectedChessPiece instanceof Pawn
        */
        diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
    }
    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
//        System.out.println("X1: "+x1+
//                   " Y1: "+y1+
//                   " X2: "+x2+
//                   " Y2: "+y2);
        if ((x1==1 || x1==6) && twoMovementsAvailable) {
            twoMovementsAvailable=false;
            return (Math.abs(x1-x2)==1 && Math.abs(y1-y2)==0)
                    ||
                    (Math.abs(x1-x2)==2 && Math.abs(y1-y2)==0);
        }
        else if (diagonalLeftIsOccupied || diagonalRightIsOccupied ) {
            return Math.abs(x1-x2)==1 && Math.abs(y1-y2)==1;
        }        
        else {
            if (startingX==1) 
                return x2-x1==1 && Math.abs(y1-y2)==0;
            else
                return x2-x1==-1 && Math.abs(y1-y2)==0;
        }
    }   

}