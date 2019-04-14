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
        STRAIGHT_AHEAD,
        DIAGONAL_LEFT,
        DIAGONAL_RIGHT;
    }
    private boolean[] conditions;
    private boolean diagonalLeftIsOccupied, diagonalRightIsOccupied, straightAheadIsNotOccupied;
    private boolean twoMovementsAvailable;
    private int startingX;
    
    public Pawn(Color figureColor, int startingX) {
        super("P", figureColor);
        twoMovementsAvailable=true;
        this.startingX=startingX;
        conditions = new boolean[3];
    }
    public void checkAvailableMovement(Movement m, ChessField c){ 
        switch(m){
            case STRAIGHT_AHEAD:
                straightAheadIsNotOccupied=c.getCurrentChessPiece()==null;
                break;
            case DIAGONAL_LEFT:
                diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
                break;
            case DIAGONAL_RIGHT:
                diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
                break;
        }
    }
    public void checkDiagonalLeft(ChessField c){
        diagonalLeftIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
    }
    public void checkDiagonalRight(ChessField c){
        diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
    }
    public void checkStraightAhead(ChessField c){
        straightAheadIsNotOccupied=c.getCurrentChessPiece()==null;
    }
    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
//        System.out.println("X1: "+x1+
//                   " Y1: "+y1+
//                   " X2: "+x2+
//                   " Y2: "+y2);
        if (startingX==1) {
            
        }
        else {
            
        }
        return ( (x2-x1==1 && Math.abs(y1-y2)==0) && straightAheadIsNotOccupied)
                || ((x1==1 || x1==6) && (x2-x1==2 && Math.abs(y1-y2)==0) && twoMovementsAvailable)
                || ( (Math.abs(x1-x2)==1 && Math.abs(y1-y2)==1 ) && (diagonalLeftIsOccupied || diagonalRightIsOccupied ))
                ;
//        if (straightAheadIsNotOccupied) {
//            if (startingX==1) 
//                return x2-x1==1 && Math.abs(y1-y2)==0;
//            else
//                return x2-x1==-1 && Math.abs(y1-y2)==0;
//        }
//        else if ((x1==1 || x1==6) && twoMovementsAvailable) {
//            twoMovementsAvailable=false;
//            return (Math.abs(x1-x2)==1 && Math.abs(y1-y2)==0)
//                    ||
//                    (Math.abs(x1-x2)==2 && Math.abs(y1-y2)==0);
//        }
//        else if (diagonalLeftIsOccupied || diagonalRightIsOccupied ) {
//            return Math.abs(x1-x2)==1 && Math.abs(y1-y2)==1;
////            if (diagonalLeftIsOccupied)
////                return Math.abs(x1-x2)==1 && y2-y1==-1;
////            else 
////                return Math.abs(x1-x2)==1 && y2-y1==1;
//        }                
//        else 
//            return Math.abs(x1-x2)==0 && Math.abs(y1-y2)==0;
    }   

}