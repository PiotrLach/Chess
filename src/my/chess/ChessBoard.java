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
import my.chess.pieces.Bishop;
import my.chess.pieces.Pawn;
import my.chess.pieces.ChessPiece;

/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel{    
    private int sourceI=-1, sourceJ=-1;
    private ChessField[][] chessMatrix;  
    private ChessPiece selectedChessPiece;
    public ChessBoard() {        
        chessMatrix=new ChessField[8][8];                
        createBoardElements();
    }
    public void chooseBoardPiece(Point p){        
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()!=null)
                {                    
                    chessMatrix[i][j].setHighlighted(true);
                    selectedChessPiece=chessMatrix[i][j].getCurrentChessPiece();
                    sourceI=i; sourceJ=j;
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }
        repaint();        
    }
    public void moveBoardPiece(Point p){
        boolean flag=false;
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()==null
                    && selectedChessPiece!=null
                    && chessMatrix[sourceI][sourceJ].getCurrentChessPiece().movementConditionFullfilled(sourceI, sourceJ, i, j)
//                    && calculatePositionDifference(i, sourceI) 
//                        <= chessMatrix[sourceI][sourceJ].getCurrentChessPiece().possibleVerticalMovements()
//                    && calculatePositionDifference(j, sourceJ)
//                        <= chessMatrix[sourceI][sourceJ].getCurrentChessPiece().possibleHorizontalMovements()
//                    && pathIsFree(sourceI, sourceJ, i,j)==false
                    )
                {   
                    chessMatrix[i][j].setCurrentChessPiece(selectedChessPiece);
                    selectedChessPiece=null;                    
                    flag=true;                    
//                    System.out.print("Horizontal difference:"+calculatePositionDifference(i, sourceI)+" Vertical difference:");
//                    System.out.println(calculatePositionDifference(j, sourceJ));
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }
        if (sourceI >= 0 && sourceJ >= 0 && flag==true) {
            chessMatrix[sourceI][sourceJ].setCurrentChessPiece(null);
            sourceI=-1; sourceJ=-1;
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
    }
    //zrealizować w klasie
    private boolean pathIsFree(int x1, int y1, int x2, int y2){
        System.out.println("X1: "+x1+
                           " Y1: "+y1+
                           " X2: "+x2+
                           " Y2: "+y2);
        boolean result=false;
        for (int i=x1; i<x2; i++) {
            for (int j=y1; j<y2; j++) {
                System.out.println(i+" "+j);
                if (chessMatrix[i][j].getCurrentChessPiece()==null){
                    System.out.println("true");
                    result=true;
//                    break;
                }                
            }
        }
        return result;
    }
    private void createBoardElements(){
        int x=0,y=0;
        for (int i=640; i>0; i-=80)
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
//            chessMatrix[1][i].setCurrentChessPiece(new Pawn("P", Color.WHITE));
            chessMatrix[1][i].setCurrentChessPiece(new Pawn("P", Color.BLACK));
        }
        chessMatrix[0][2].setCurrentChessPiece(new Bishop("B", Color.BLACK));
        chessMatrix[0][5].setCurrentChessPiece(new Bishop("B", Color.BLACK));
    }
    private int calculatePositionDifference(int c1, int c2){
        
        return Math.abs(c1-c2);        
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
