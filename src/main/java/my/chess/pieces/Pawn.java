/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;
import java.util.List;
import my.chess.Square;
import my.chess.Board;

/**
 *
 * @author Piotr Lach
 */
public class Pawn extends Piece {

    public Pawn(Color color, PieceName pieceName) {
        super(pieceName, color, Images.getPAWN(color));

        startingRow = pieceName == PieceName.Pawn1 ? 1 : 6;

        isOnBottomRow = startingRow == 1;
    }
    
    private boolean enPassant(Coord source, Coord target) {
        if ((source.row - target.row) == (isOnBottomRow ? -1 : 1) && Math.abs(source.col - target.col) == 1) {
            System.out.println("---");
            System.out.format("%d %d %d %d", source.row, source.col, target.row, target.col);
            int temp = source.col - (source.col - target.col);
            System.out.println(source.row + " " + temp);
            Square square = Board.getSquare(source.row, source.col - (source.col - target.col));
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
    public boolean isCorrectMovement(Coord source, Coord target) {
        Square square = Board.getSquare(target.row, target.col);
        
        boolean isOneForwardMovement = Math.abs(source.row - target.row) == 1, 
                isTwoForwardMovements = Math.abs(source.row - target.row) == 2,
                isVertical = (isOnBottomRow ? target.row > source.row : target.row < source.row),
                isHorizontal = Math.abs(source.col - target.col) == 1,           
                isNotHorizontal = Math.abs(source.col - target.col) == 0,           
                isNullAhead = square.getPiece() == null,
                isFoeDiagonal = !isNullAhead && this.isFoe(square.getPiece());
        
        var availableMovements = List.of(
            isVertical && isOneForwardMovement && isNotHorizontal && isNullAhead,
            isVertical && source.row == startingRow && isTwoForwardMovements && isNotHorizontal && isNullAhead,
            isVertical && isOneForwardMovement && isHorizontal && isFoeDiagonal
        );
        return availableMovements.contains(true);      
    }
    private final boolean isOnBottomRow;
    private final int startingRow;

}
