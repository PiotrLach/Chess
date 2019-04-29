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
        DIAGONAL_RIGHT,
        TWO_FIELDS_AHEAD;
    }    
    private boolean diagonalLeftIsOccupied, diagonalRightIsOccupied, straightAheadIsNotOccupied,
            twoFieldsAheadIsNotOccupied;
//    private boolean twoMovementsAvailable;
    private final int startingX;
    
    public Pawn(Color figureColor, int startingX) {
        super("P", figureColor);
//        twoMovementsAvailable=true;
        this.startingX=startingX;           
    }
    private void checkAvailableMovement(Movement m, ChessField c){ 
        switch(m) {
            case STRAIGHT_AHEAD:
                straightAheadIsNotOccupied=c.getCurrentChessPiece()==null;
                break;
            case TWO_FIELDS_AHEAD:
                twoFieldsAheadIsNotOccupied=c.getCurrentChessPiece()==null;
                break;
            case DIAGONAL_LEFT:
                diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
                break;
            case DIAGONAL_RIGHT:
                diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
                break;            
        }
    }
    private void checkBoardConditions(/*ChessField[][] chessMatrix,*/ int x, int y) {
        int leftBoundary, rightBoundary, topBoundary;
        int forwardMovement, diagonalLeftMovement, diagonalRightMovement;
        int twoFieldsForwardMovement;
        if (startingX==1) {
            leftBoundary=0;
            rightBoundary=7;
            topBoundary=7;
            forwardMovement=1;
            diagonalLeftMovement=-1;
            diagonalRightMovement=1;
            twoFieldsForwardMovement=2;
        }
        else {
            leftBoundary=7;
            rightBoundary=0;
            topBoundary=0;                        
            forwardMovement=-1;
            diagonalLeftMovement=1;
            diagonalRightMovement=-1;
            twoFieldsForwardMovement=-2;
        }            
        if (y!=leftBoundary && x!=topBoundary)
            checkDiagonalLeft(my.chess.ChessBoard.chessMatrix[x+forwardMovement][y+diagonalLeftMovement]);
        if (y!=rightBoundary && x!=topBoundary)
            checkDiagonalRight(my.chess.ChessBoard.chessMatrix[x+forwardMovement][y+diagonalRightMovement]);
        if (x!=topBoundary)
            checkStraightAhead(my.chess.ChessBoard.chessMatrix[x+forwardMovement][y]);
        if (x==startingX) 
            checkTwoFieldsAhead(my.chess.ChessBoard.chessMatrix[x+twoFieldsForwardMovement][y]);        
//        System.out.println(x+forwardMovement+" "+(y+diagonalRightMovement));
    }
    private void checkDiagonalLeft(ChessField c){
        diagonalLeftIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
//        System.out.println("checkDiagonalLeft "+diagonalLeftIsOccupied);
    }
    private void checkDiagonalRight(ChessField c){
        diagonalRightIsOccupied=c.getCurrentChessPiece()!=null && this.isFoe(c.getCurrentChessPiece());
//        System.out.println("checkDiagonalRight "+diagonalRightIsOccupied);
    }
    private void checkStraightAhead(ChessField c){
        straightAheadIsNotOccupied=c.getCurrentChessPiece()==null;
    }
    private void checkTwoFieldsAhead(ChessField c){
        twoFieldsAheadIsNotOccupied=c.getCurrentChessPiece()==null;
    }
    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
//        System.out.println("X1: "+x1+
//                   " Y1: "+y1+
//                   " X2: "+x2+
//                   " Y2: "+y2);
        checkBoardConditions(x1,y1);
        int twoMovements=2, oneMovement=1, left=-1, right=1;
        if (startingX==6) {
            twoMovements*=-1;
            oneMovement*=-1;
            left*=-1;
            right*=-1;
        }
        return      ( (x2-x1==oneMovement && Math.abs(y1-y2)==0) && straightAheadIsNotOccupied )
                ||  ( (x1==startingX && x2-x1==twoMovements && Math.abs(y1-y2)==0) && twoFieldsAheadIsNotOccupied & straightAheadIsNotOccupied)
                ||  ( (x2-x1==oneMovement && y2-y1==left ) && diagonalLeftIsOccupied)
                ||  ( (x2-x1==oneMovement && y2-y1==right ) && diagonalRightIsOccupied )                
                ;
    }   

}