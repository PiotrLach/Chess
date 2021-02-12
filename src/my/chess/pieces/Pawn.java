/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.ChessField;
import my.chess.ChessBoard;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends ChessPiece {

    public Pawn(Color figureColor, PieceName p) {
        super(p, figureColor, Images.getPAWN(figureColor));

        startingX = p == PieceName.Pawn1 ? 1 : 6;

        boolean isOnBottomRow = startingX == 1;

        oneForwardMovement = isOnBottomRow ? 1 : -1;
        diagonalLeftMovement = isOnBottomRow ? -1 : 1;
        diagonalRightMovement = isOnBottomRow ? 1 : -1;
        twoForwardMovements = isOnBottomRow ? 2 : -2;
    }
    
    private boolean enPassant(int x1, int y1, int x2, int y2) {
        if ((x1 - x2) == (startingX == 1 ? -1 : 1) && Math.abs(y1 - y2) == 1) {
            System.out.println("---");
            System.out.format("%d %d %d %d", x1, y1, x2, y2);
            int temp = y1 - (y1 - y2);
            System.out.println(x1 + " " + temp);
            ChessField cf = ChessBoard.getChessMatrixField(x1, y1 - (y1 - y2));
            ChessPiece neighbor = cf.getCurrentChessPiece();
            if (neighbor instanceof Pawn) {
                Pawn p = (Pawn) neighbor;
                cf.setCurrentChessPiece(null);
                return true;
            }
        }
        return false;
    }

    private boolean check(String type, int x, int y) {
        ChessField cf = ChessBoard.getChessMatrixField(x, y);
        boolean isNull = cf.getCurrentChessPiece() == null;
        boolean isFoe = !isNull && this.isFoe(cf.getCurrentChessPiece());
        switch(type) {
            default:
                return isNull;
            case FOE:                            
                return isFoe;            
        }
    }
    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        boolean isVertical = x2 - x1 == oneForwardMovement;
        boolean isNotHorizontal = Math.abs(y1 - y2) == 0;        
        boolean isNullAhead = check(NULL, x2, y2);
        boolean isFoeDiagonal = check(FOE, x2, y2);
        boolean availableMovements[] = {
            isVertical && isNotHorizontal && isNullAhead,
            x1 == startingX && x2 - x1 == twoForwardMovements && isNotHorizontal && check(NULL, x1 + oneForwardMovement, y1) && isNullAhead,
            isVertical && y2 - y1 == diagonalLeftMovement && isFoeDiagonal,
            isVertical && y2 - y1 == diagonalRightMovement && isFoeDiagonal
        };        
        for (boolean b : availableMovements) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    private final String FOE = "FOE";
    private final String NULL = "NULL";
    private final int startingX,
            oneForwardMovement,
            diagonalLeftMovement,
            diagonalRightMovement,
            twoForwardMovements;

}
