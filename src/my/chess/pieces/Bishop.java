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
public class Bishop extends ChessPiece {

    public Bishop(String chessPieceName, Color figureColor) {
        super(chessPieceName, figureColor);
    }    

    @Override
    public int possibleVerticalMovements() {
        return 7;
    }

    @Override
    public int possibleHorizontalMovements() {
        return 7;
    }
    
}
