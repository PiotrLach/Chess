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

        leftBoundary = isOnBottomRow ? 0 : 7;
        rightBoundary = isOnBottomRow ? 7 : 0;
        topBoundary = isOnBottomRow ? 7 : 0;
        oneForwardMovement = isOnBottomRow ? 1 : -1;
        diagonalLeftMovement = isOnBottomRow ? -1 : 1;
        diagonalRightMovement = isOnBottomRow ? 1 : -1;
        twoForwardMovements = isOnBottomRow ? 2 : -2;
    }

    private void checkAvailableMovements(int type, ChessField c) {
        boolean isNull = c.getCurrentChessPiece() == null;
        boolean isFoe = !isNull && this.isFoe(c.getCurrentChessPiece());        
        switch (type) {
            case 0:
                diagonalLeftIsOccupied = isFoe;
                break;
            case 1:
                diagonalRightIsOccupied = isFoe;
                break;
            case 2:
                straightAheadIsNotOccupied = isNull;
                break;
            case 3:
                twoFieldsAheadIsNotOccupied = isNull;
                break;
        }
    }

    private void checkBoardConditions(int x, int y) {        
        boolean constraints[] = { 
            y != leftBoundary && x != topBoundary,
            y != rightBoundary && x != topBoundary,
            x != topBoundary,
            x == startingX
        };
        int coordinates[] = {
            (x + oneForwardMovement) << 4 | (y + diagonalLeftMovement),
            (x + oneForwardMovement) << 4 | (y + diagonalRightMovement),
            (x + oneForwardMovement) << 4 | (y),
            (x + twoForwardMovements) << 4 | (y)
        };
        for (int type = 0; type < 4; type++) { 
            if (constraints[type]) {                
                x = coordinates[type] >> 4 & MASK;
                y = coordinates[type] & MASK;
                checkAvailableMovements(type, ChessBoard.getChessMatrixField(x, y));
            }
        }
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

    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        checkBoardConditions(x1, y1);
        boolean isVertical = x2 - x1 == oneForwardMovement;
        boolean isNotHorizontal = Math.abs(y1 - y2) == 0;
        boolean availableMovements[] = {
            (isVertical && isNotHorizontal) && straightAheadIsNotOccupied,
            ((x1 == startingX && x2 - x1 == twoForwardMovements && isNotHorizontal) && twoFieldsAheadIsNotOccupied && straightAheadIsNotOccupied),
            ((isVertical && y2 - y1 == diagonalLeftMovement) && diagonalLeftIsOccupied),
            ((isVertical && y2 - y1 == diagonalRightMovement) && diagonalRightIsOccupied)
        };
        for (boolean b : availableMovements) {
            if (b) {
                return true;
            }
        }
//        boolean movement = ((x2 - x1 == oneForwardMovement && Math.abs(y1 - y2) == 0) && straightAheadIsNotOccupied)
//                || ((x1 == startingX && x2 - x1 == twoForwardMovements && Math.abs(y1 - y2) == 0) && twoFieldsAheadIsNotOccupied && straightAheadIsNotOccupied)
//                || ((x2 - x1 == oneForwardMovement && y2 - y1 == diagonalLeftMovement) && diagonalLeftIsOccupied)
//                || ((x2 - x1 == oneForwardMovement && y2 - y1 == diagonalRightMovement) && diagonalRightIsOccupied);
//                || enPassant(x1, y1, x2, y2);
        return false;
    }

    private final int MASK = 0xF;
    private boolean diagonalLeftIsOccupied,
            diagonalRightIsOccupied,
            straightAheadIsNotOccupied,
            twoFieldsAheadIsNotOccupied;
    private final int startingX,
            leftBoundary,
            rightBoundary,
            topBoundary,
            oneForwardMovement,
            diagonalLeftMovement,
            diagonalRightMovement,
            twoForwardMovements;

}
