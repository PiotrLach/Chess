/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.chess.pieces.ChessPawn;
import my.chess.pieces.ChessPiece;

/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel{    
    private int tempI=-1, tempJ=-1;
    private ChessField[][] chessMatrix;  
    private ChessPiece selectedChessPiece;
    public ChessBoard() {        
        chessMatrix=new ChessField[8][8];                
        createBoardElements();
    }
    public void chooseBoardFieldFigure(Point p){        
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()!=null)
                {                    
                    chessMatrix[i][j].setHighlighted(true);
                    selectedChessPiece=chessMatrix[i][j].getCurrentChessPiece();
                    tempI=i; tempJ=j;
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }
        repaint();        
    }
    public void moveBoardFieldFigure(Point p){
        if (tempI >= 0 && tempJ >= 0) {
            chessMatrix[tempI][tempJ].setCurrentChessPiece(null);
            tempI=-1; tempJ=-1;
        }
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()==null
                    && selectedChessPiece!=null
                    && tempI!=i && tempJ!=j)
                {   
                    chessMatrix[i][j].setCurrentChessPiece(selectedChessPiece);
                    selectedChessPiece=null;                    
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }
        repaint();
    }
    @Override                 
    public void paint(Graphics g) {
        super.paint(g);         
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {
                if (chessMatrix[i][j].isHighlighted()) {                    
                    chessMatrix[i][j].highlightChessField(g);                                            
                }    
                else {
                    chessMatrix[i][j].drawChessField(g);
                }    
                if (chessMatrix[i][j].getCurrentChessPiece()!=null) {
                    Double x = chessMatrix[i][j].getX();
                    Double y = chessMatrix[i][j].getY();
                    chessMatrix[i][j].getCurrentChessPiece().drawPieceSymbol(g, x.intValue(),y.intValue());
                }
            }
        }
//        for (int i=0; i<8;i++) {
//            for (int j=0;j<8; j++) {
////                System.out.println(chessMatrix[i][j]);
//                switch (chessMatrix[i][j].getHighlighted()) {
//                    default:
//                        chessMatrix[i][j].drawChessField(g);                        
//                        break;
//                    case 1:
//                        chessMatrix[i][j].highlightChessField(g);
//                        if (chessMatrix[i][j].getCurrentChessPiece()!=null){
//                            Double x = chessMatrix[i][j].getX();
//                            Double y = chessMatrix[i][j].getY();
//                            chessMatrix[i][j].getCurrentChessPiece().drawPieceSymbol(g, x.intValue(),y.intValue());
//                        }
//                        break;
//                } 
//            }
//        }      
    }
    public void createBoardElements(){
        int x=0,y=0;
        for (int i=0; i<640; i+=80)
        {            
            for (int j=0; j<640; j+=80)
            {                
                ChessField c = new ChessField(j,i,80,80,x,y);
                chessMatrix[x][y] = c;                                
//                System.out.println(chessMatrix[x][y].toString());                
//                System.out.println(c+" "+this.getWidth()+" "+this.getHeight());
                y++;                
            }
            y=0;            
            x++;
            if (x==8) 
                x=0;
        }
        for (int i=0; i<8; i++) {
//            chessMatrix[i][1].setCurrentChessPiece(new ChessPawn("P", Color.WHITE));
            chessMatrix[6][i].setCurrentChessPiece(new ChessPawn("P", Color.BLACK));
        }
//        chessMatrix[1][1].setCurrentChessPiece(null);
    }       

    public ChessField[][] getChessMatrix() {
        return chessMatrix;
    }

    public void setChessMatrix(ChessField[][] chessMatrix) {
        this.chessMatrix = chessMatrix;
    }

    public ChessPiece getSelectedChessPiece() {
        return selectedChessPiece;
    }

    public void setSelectedChessPiece(ChessPiece selectedChessPiece) {
        this.selectedChessPiece = selectedChessPiece;
    }
    
}
