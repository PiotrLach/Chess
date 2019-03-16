/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel{    

    private ArrayList<ChessField> chessFields;    
    private ChessField[][] chessMatrix;
    private boolean changeColor;
    public ChessBoard() {
        chessMatrix=new ChessField[64][64];        
        chessFields=new ArrayList<>();
        drawChessFields();
    }    
//    public void uselessFunction(){}
    @Override                 
    public void paint(Graphics g) {
        super.paint(g); 
        for (ChessField c : chessFields)
        {
            switch (c.getHighlighted())
            {
                case 0:
                    c.drawChessField(g);
                    break;
                case 1:
                    c.highlightChessField(g);
                    break;
            }            
        }        
    }
    public void drawChessFields(){
        for (int i=120; i<840; i+=80)
        {
            for (int j=80; j<720; j+=80)
            {
                ChessField c = new ChessField(i,j,80,80);
                chessFields.add(c);
//                System.out.println(c+" "+this.getWidth()+" "+this.getHeight());
            }
        }
    }

    public boolean isChangeColor() {
        return changeColor;
    }

    public void setChangeColor(boolean changeColor) {
        this.changeColor = changeColor;
    }

    public ArrayList<ChessField> getChessFields() {
        return chessFields;
    }
    
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        Point p = e.getPoint();
//        if (ps.contains(p)) {
//            changeColor=true;
//            repaint();
//            System.out.println("Success!");
//        }
//    }
}
