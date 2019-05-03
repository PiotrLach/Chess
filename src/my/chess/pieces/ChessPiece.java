/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author bruce
 */
abstract public class ChessPiece {    
    public ChessPiece(String chessPieceName, Color figureColor, int x, int y) {
        this.chessPieceName = chessPieceName;
        this.figureColor = figureColor;
        this.x=x;
        this.y=y;
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
    protected void updateCoordinates(boolean mov, int x2, int y2) {
        if (mov) {
            updateX(x2);
            updateY(y2);
        }
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    private void updateX(int x){
        this.x=x;
    }
    private void updateY(int y){
        this.y=y;
    }
    abstract public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2);
    protected String chessPieceName;  
    protected Color figureColor;
    protected int x,y;    
}