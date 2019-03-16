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
    private String chessPieceName;
    private JLabel chessPieceLabel;
    abstract void move(ChessField[][] cf);
    abstract void highlightOptions();    
}
