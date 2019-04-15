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
import my.chess.pieces.King;
import my.chess.pieces.Knight;
import my.chess.pieces.Queen;
import my.chess.pieces.Rook;

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
                    && selectedChessPiece!=null
                    && 
                      (
                        chessMatrix[i][j].getCurrentChessPiece()==null 
                                || 
                                (chessMatrix[i][j].getCurrentChessPiece()!=null
                                    && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece() ) 
                                )
                      )                    
//                    && selectedChessPiece.movementConditionFullfilled(sourceI, sourceJ, i, j)
                    && pathIsFree(sourceI, sourceJ, i,j)
//                    && !(selectedChessPiece instanceof Pawn)
                    )
                {   
                    if (selectedChessPiece instanceof Pawn) {
                        Pawn selectedPawn = (Pawn) selectedChessPiece;
                        selectedPawn.checkBoardConditions(chessMatrix, sourceI, sourceJ);
                        if (selectedPawn.movementConditionFullfilled(sourceI, sourceJ, i, j)) {
                            chessMatrix[i][j].setCurrentChessPiece(selectedPawn);
                            selectedChessPiece=null;                    
                            flag=true;                    
                        }
                    }
                    else {
                        if (selectedChessPiece.movementConditionFullfilled(sourceI, sourceJ, i, j)) {
                            chessMatrix[i][j].setCurrentChessPiece(selectedChessPiece);
                            selectedChessPiece=null;                    
                            flag=true;                    
                        }
                    }
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
//        System.out.println("X1: "+x1+
//                           " Y1: "+y1+
//                           " X2: "+x2+
//                           " Y2: "+y2);
//        System.out.println("Yes");
        int nullCount=0;
        boolean result;//=false;
        if (!(selectedChessPiece instanceof Knight)) {                    
            if (x1<x2) {
                for (int i = x1; i <= x2; i++) {                       
                    if (y1>y2) {
                        for (int j = y1; j>= y2; j--) {
                            if (selectedChessPiece.movementConditionFullfilled(x1,y1,i,j)                            
                                && (i!=x1 && y1==y2) || (j!=y1 && x1==x2) || (i!=x1 && j!=y1 && Math.abs(y1-j) == Math.abs(x1-i)))  {
                                result=chessMatrix[i][j].getCurrentChessPiece()!=null;                                                          
                                if (result && !(i==x2 && j==y2 && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece())))
                                    nullCount++;
                                
                                System.out.println(result+" "+i+" "+j+" "+nullCount);
                            }                        
                        }
                    }
                    else {
                        for (int j = y1; j<=y2; j++) {           
                            if (selectedChessPiece.movementConditionFullfilled(x1,y1,i,j)
                                && (i!=x1 && y1==y2) || (j!=y1 && x1==x2) || (i!=x1 && j!=y1 && Math.abs(y1-j) == Math.abs(x1-i)))  {
                                result=chessMatrix[i][j].getCurrentChessPiece()!=null;                           
                                if (result && !(i==x2 && j==y2 && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece())))
                                    nullCount++;
                                
                                System.out.println(result+" "+i+" "+j+" "+nullCount);
                            }                        
                        }
                    }
                }
            }
            else {
                for (int i = x1; i >= x2; i--) {           
                    if (y1>y2) {
                        for (int j = y1; j>= y2; j--) {           
                            if (selectedChessPiece.movementConditionFullfilled(x1,y1,i,j)
                                && (i!=x1 && y1==y2) || (j!=y1 && x1==x2) || (i!=x1 && j!=y1 && Math.abs(y1-j) == Math.abs(x1-i)))  {
                                result=chessMatrix[i][j].getCurrentChessPiece()!=null;                           
                                if (result && !(i==x2 && j==y2 && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece())))
                                    nullCount++;
                                
                                System.out.println(result+" "+i+" "+j+" "+nullCount);
                            }                        
                        }
                    }
                    else {
                        for (int j = y1; j<=y2; j++) {           
                            if (selectedChessPiece.movementConditionFullfilled(x1,y1,i,j)
                                && (i!=x1 && y1==y2) || (j!=y1 && x1==x2) || (i!=x1 && j!=y1 && Math.abs(y1-j) == Math.abs(x1-i)))  {
                                result=chessMatrix[i][j].getCurrentChessPiece()!=null;                           
                                if (result && !(i==x2 && j==y2 && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece())))
                                    nullCount++;
                                
                                System.out.println(result+" "+i+" "+j+" "+nullCount);
                            }                        
                        }
                    }
                }            
            }
        }
        System.out.println("/////////////////////");
        return nullCount == 0;
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
        Color c = null;
        for (int i=0; i<8; i++) {            
            chessMatrix[1][i].setCurrentChessPiece(new Pawn(Color.BLACK,1));            
        }        
        chessMatrix[6][0].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[6][1].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[6][2].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[6][3].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[2][4].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[6][5].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[2][6].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        chessMatrix[6][7].setCurrentChessPiece(new Pawn(Color.WHITE,6));
        for (int i=0; i<=7; i+=7) {
            switch(i) {
                case 0:
                    c=Color.BLACK;
                    break;
                case 7:
                    c=Color.WHITE;
                    break;
            }
            chessMatrix[i][0].setCurrentChessPiece(new Rook(c));
            chessMatrix[i][1].setCurrentChessPiece(new Knight(c));
            chessMatrix[i][2].setCurrentChessPiece(new Bishop(c));
            chessMatrix[i][3].setCurrentChessPiece(new Queen(c));
            chessMatrix[i][4].setCurrentChessPiece(new King(c));
            chessMatrix[i][5].setCurrentChessPiece(new Bishop(c));                
            chessMatrix[i][6].setCurrentChessPiece(new Knight(c));
            chessMatrix[i][7].setCurrentChessPiece(new Rook(c));   
        }
        
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
