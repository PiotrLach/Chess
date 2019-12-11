/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author bruce
 */
abstract public class ChessPiece {    
    public ChessPiece(String chessPieceName, Color figureColor, Image img/*, int x, int y*/) {
        this.chessPieceName = chessPieceName;
        this.figureColor = figureColor;
        this.img = img;
//        this.x=x;
//        this.y=y;
    }
    public void drawImage(Graphics g, int drawX, int drawY, int width, int height) {
        g.drawImage(img, drawX, drawY, width, height, null);
    }
    public void drawPieceSymbol(Graphics g, int drawX, int drawY) {        
        g.setColor(figureColor);
        Font myFont = new Font("Times New Roman", Font.BOLD, 40);
        g.setFont(myFont);
        g.drawString(chessPieceName, drawX+30, drawY+50);
    }        
    public Color getFigureColor() {
        return figureColor;
    }
    public boolean isFoe(ChessPiece cp) {        
        return cp.getFigureColor()!=figureColor;
    }
    public String getChessPieceName () {
        return chessPieceName;
    }
    abstract public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2);
    protected String chessPieceName;  
    protected Color figureColor;
//    protected int x,y;    
    protected Image img;
}