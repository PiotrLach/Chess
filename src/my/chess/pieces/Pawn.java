/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Image;
import my.chess.ChessField;
import my.chess.ChessBoard;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends ChessPiece {

    public enum Movement {
        STRAIGHT_AHEAD,
        DIAGONAL_LEFT,
        DIAGONAL_RIGHT,
        TWO_FIELDS_AHEAD;
    }
    private boolean diagonalLeftIsOccupied, diagonalRightIsOccupied, straightAheadIsNotOccupied,
            twoFieldsAheadIsNotOccupied;
    public boolean twoMovementsMade = false;
    private int startingX;

    public Pawn(Color figureColor, Image img, PieceName p/*, int y*/) {
        super(p, figureColor, img/*, startingX, y*/);
        this.startingX = p == PieceName.Pawn1 ? 1 : 6;
    }

    private void chooseStartingX() {
        if (figureColor == Color.BLACK) {
            startingX = 1;
        } else {
            startingX = 6;
        }
    }

    private void checkAvailableMovement(Movement m, ChessField c) {
        switch (m) {
            case STRAIGHT_AHEAD:
                straightAheadIsNotOccupied = c.getCurrentChessPiece() == null;
                break;
            case TWO_FIELDS_AHEAD:
                twoFieldsAheadIsNotOccupied = c.getCurrentChessPiece() == null;
                break;
            case DIAGONAL_LEFT:
                diagonalRightIsOccupied = c.getCurrentChessPiece() != null && this.isFoe(c.getCurrentChessPiece());
                break;
            case DIAGONAL_RIGHT:
                diagonalRightIsOccupied = c.getCurrentChessPiece() != null && this.isFoe(c.getCurrentChessPiece());
                break;
        }
    }

    private void checkBoardConditions(int x, int y) {
        int leftBoundary, rightBoundary, topBoundary;
        int forwardMovement, diagonalLeftMovement, diagonalRightMovement;
        int twoFieldsForwardMovement;
        if (startingX == 1) {
            leftBoundary = 0;
            rightBoundary = 7;
            topBoundary = 7;
            forwardMovement = 1;
            diagonalLeftMovement = -1;
            diagonalRightMovement = 1;
            twoFieldsForwardMovement = 2;
        } else {
            leftBoundary = 7;
            rightBoundary = 0;
            topBoundary = 0;
            forwardMovement = -1;
            diagonalLeftMovement = 1;
            diagonalRightMovement = -1;
            twoFieldsForwardMovement = -2;
        }
        if (y != leftBoundary && x != topBoundary) {
            checkDiagonalLeft(ChessBoard.getChessMatrixField(x + forwardMovement, y + diagonalLeftMovement));
        }
        if (y != rightBoundary && x != topBoundary) {
            checkDiagonalRight(ChessBoard.getChessMatrixField(x + forwardMovement, y + diagonalRightMovement));
        }
        if (x != topBoundary) {
            checkStraightAhead(ChessBoard.getChessMatrixField(x + forwardMovement, y));
        }
        if (x == startingX) {
            checkTwoFieldsAhead(ChessBoard.getChessMatrixField(x + twoFieldsForwardMovement, y));
        }
    }

    private void checkDiagonalLeft(ChessField c) {
        diagonalLeftIsOccupied = c.getCurrentChessPiece() != null && this.isFoe(c.getCurrentChessPiece());
    }

    private void checkDiagonalRight(ChessField c) {
        diagonalRightIsOccupied = c.getCurrentChessPiece() != null && this.isFoe(c.getCurrentChessPiece());
    }

    private void checkStraightAhead(ChessField c) {
        straightAheadIsNotOccupied = c.getCurrentChessPiece() == null;
    }

    private void checkTwoFieldsAhead(ChessField c) {
        twoFieldsAheadIsNotOccupied = c.getCurrentChessPiece() == null;
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
        boolean top = startingX == 6;
        int twoMovements = top ? 2 : -2,
            oneMovement = top ? 1 : -1, 
            left = top ? -1 : 1, 
            right = top ? 1 : -1;  
        
        boolean movement = ((x2 - x1 == oneMovement && Math.abs(y1 - y2) == 0) && straightAheadIsNotOccupied)
                || ((x1 == startingX && x2 - x1 == twoMovements && Math.abs(y1 - y2) == 0) && twoFieldsAheadIsNotOccupied & straightAheadIsNotOccupied)
                || ((x2 - x1 == oneMovement && y2 - y1 == left) && diagonalLeftIsOccupied)
                || ((x2 - x1 == oneMovement && y2 - y1 == right) && diagonalRightIsOccupied)
                || enPassant(x1, y1, x2, y2);
        return movement;
    }

}
