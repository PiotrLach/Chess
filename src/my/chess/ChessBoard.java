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
        chessMatrix=new ChessField[64][64];                
        createBoardElements();
    }
    public void highlightBoardFields(Point p){
        int index;
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) && 
                    chessMatrix[i][j].getHighlighted()==0)
                {                    
                    chessMatrix[i][j].setHighlighted(1);
    //                System.out.println("Highlight enabled!");
                }
                else if (chessMatrix[i][j].contains(p) 
                        && chessMatrix[i][j].getHighlighted()==1) 
                {                    
                    chessMatrix[i][j].setHighlighted(0);
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
                ChessField c = new ChessField(i,j,80,80);
                chessMatrix[x][y] = c;
                y++;
//                System.out.println(c+" "+this.getWidth()+" "+this.getHeight());
            }
            y=0;
            x++;
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
