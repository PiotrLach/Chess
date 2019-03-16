/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author bruce
 */
public class ChessField extends Rectangle {

    private String index;
    private int highlighted;
    public ChessField(int x, int y, int width, int height) {
        super(x, y, width, height);       
    }
    public void drawChessField(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.drawRect(x,y,width,height);
    }
    public void highlightChessField(Graphics g){
        g.setColor(Color.GRAY);        
        g.fillRect(x, y, width, height);
    }    
    
//        g.setColor(c);        
//        g.fillRect(this.x,this.y,this.width,this.height);
//        if (squareFlag==true) {
//            
//        }
    
    public void setHighlighted(int highlighted) {
        this.highlighted = highlighted;
    }

    public int getHighlighted() {
        return highlighted;
    }
    @Override
    public String toString(){
        return "x: "+x+" y: "+y;
    }        
    
}
