/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

/**
 *
 * @author bruce
 */
public class ChessPawn extends ChessPiece{

    public ChessPawn() {
        super("P");
    }

    @Override
    public int moveX() {
        return 1;
    }

    @Override
    public int moveY() {
        return 2;
    }

    @Override
    public void highlightOptions() {
        
    }

}