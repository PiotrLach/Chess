/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import javax.swing.JLabel;

/**
 *
 * @author bruce
 */
abstract public class ChessPiece {    
    private String chessPieceName;
    private JLabel chessPieceLabel;
    public ChessPiece(String chessPieceName) {
        this.chessPieceName = chessPieceName;
        if (this.chessPieceName!=null){ 
            this.chessPieceLabel = new JLabel(chessPieceName);
        }
    }
    abstract public int moveX();
    abstract public int moveY();
    abstract public void highlightOptions();    

    public String getChessPieceName() {
        return chessPieceName;
    }

    public void setChessPieceName(String chessPieceName) {
        this.chessPieceName = chessPieceName;
    }
    
}
//    private int matrixCoordinateX, matrixCoordinateY;
//    private ChessField chessField;    
//    private JLabel chessPieceLabel;
//    public ChessPiece(ChessField chessField, JLabel chessPieceLabel ){ 
//                    ,int matrixCoordinateX, int matrixCoordinateY){
//        this.chessField=chessField;
//        this.chessPieceLabel=chessPieceLabel;
//    }        
//    abstract public void highlightOptions();    
//    public int moveX(){                
//        return matrixCoordinateX+2;
//    }
//    public int moveY(){                
//        return matrixCoordinateY+1;
//    }