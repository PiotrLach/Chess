/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import javax.swing.JLabel;

/**
 *
 * @author bruce
 */
abstract class ChessPiece {    
    private ChessField chessField;    
    private JLabel chessPieceLabel;
    public ChessPiece(ChessField chessField, JLabel chessPieceLabel){
        this.chessField=chessField;
        this.chessPieceLabel=chessPieceLabel;
    }    
    abstract public void move(ChessField[][] chessMatrix);
    abstract public void highlightOptions();    
}
