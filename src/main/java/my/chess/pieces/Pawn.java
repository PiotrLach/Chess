/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.util.List;
import my.chess.Square;
import my.chess.Board;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    public Pawn(Color figureColor, PieceName p) {
        super(p, figureColor, Images.getPAWN(figureColor));

        startingX = p == PieceName.Pawn1 ? 1 : 6;

        isOnBottomRow = startingX == 1;
    }
    
    private boolean enPassant(int x1, int y1, int x2, int y2) {
        if ((x1 - x2) == (startingX == 1 ? -1 : 1) && Math.abs(y1 - y2) == 1) {
            System.out.println("---");
            System.out.format("%d %d %d %d", x1, y1, x2, y2);
            int temp = y1 - (y1 - y2);
            System.out.println(x1 + " " + temp);
            Square square = Board.getSquare(x1, y1 - (y1 - y2));
            Piece neighbor = square.getPiece();
            if (neighbor instanceof Pawn) {
                Pawn p = (Pawn) neighbor;
                square.setPiece(null);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        Square square = Board.getSquare(x2, y2);
        
        boolean bOneForwardMovement = Math.abs(x1 - x2) == 1, 
                bTwoForwardMovements = Math.abs(x1 - x2) == 2,
                isVertical = (isOnBottomRow ? x2 > x1 : x2 < x1),
                isHorizontal = Math.abs(y1 - y2) == 1,           
                isNotHorizontal = Math.abs(y1 - y2) == 0,           
                isNullAhead = square.getPiece() == null,
                isFoeDiagonal = !isNullAhead && this.isFoe(square.getPiece());
        
        var availableMovements = List.of(
            isVertical && bOneForwardMovement && isNotHorizontal && isNullAhead,
            isVertical && x1 == startingX && bTwoForwardMovements && isNotHorizontal && isNullAhead,
            isVertical && bOneForwardMovement && isHorizontal && isFoeDiagonal
        );
        return availableMovements.contains(true);      
    }
    private final boolean isOnBottomRow;
    private final int startingX;

}
