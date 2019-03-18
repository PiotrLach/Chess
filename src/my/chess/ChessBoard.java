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
import javax.swing.JPanel;

/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel{    
    
    private ChessField[][] chessMatrix;
    private boolean changeColor;
    public ChessBoard() {
        chessMatrix=new ChessField[8][8];                
        createBoardElements();
    }
    public void highlightBoardField(Point p){
//        boolean isHighlighted = false;
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) && 
                    chessMatrix[i][j].getHighlighted()==0
//                        && isHighlighted==false
                    )
                {                    
                    chessMatrix[i][j].setHighlighted(1);
//                    isHighlighted=true;
    //                System.out.println("Highlight enabled!");
                }
                else if (chessMatrix[i][j].contains(p) && 
                        chessMatrix[i][j].getHighlighted()==1
 //                         && isHighlighted==true
                        )
                {                    
                    chessMatrix[i][j].setHighlighted(0);
//                    isHighlighted=false;
    //                System.out.println("Highlight disabled!");
                }            
                repaint();
            }
        }
    }
    @Override                 
    public void paint(Graphics g) {
        super.paint(g);         
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {
//                System.out.println(chessMatrix[i][j]);
                switch (chessMatrix[i][j].getHighlighted()) {
                    default:
                        chessMatrix[i][j].drawChessField(g);
                        break;
                    case 1:
                        chessMatrix[i][j].highlightChessField(g);
                        break;
                } 
            }
        }      
    }
    public void createBoardElements(){
        int x=0,y=0;
        for (int i=120; i<840; i+=80)
        {            
            for (int j=80; j<720; j+=80)
            {                
                ChessField c = new ChessField(i,j,80,80,x,y);
                chessMatrix[x][y] = c;
//                System.out.println(chessMatrix[x][y].toString());
                y++;                
//                System.out.println(c+" "+this.getWidth()+" "+this.getHeight());
            }
            y=0;            
            x++;
            if (x==8) 
                x=0;
        }
    }
    
    public boolean isChangeColor() {
        return changeColor;
    }

    public void setChangeColor(boolean changeColor) {
        this.changeColor = changeColor;
    }

    public ChessField[][] getChessMatrix() {
        return chessMatrix;
    }

    public void setChessMatrix(ChessField[][] chessMatrix) {
        this.chessMatrix = chessMatrix;
    }
    
}
