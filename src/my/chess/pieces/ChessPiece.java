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
    protected String chessPieceName;  
    protected Color figureColor;

    public ChessPiece(String chessPieceName, Color figureColor) {
        this.chessPieceName = chessPieceName;
        this.figureColor = figureColor;
    }
//    abstract public boolean captureConditionFullfilled();
    abstract public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2);
    public void drawPieceSymbol(Graphics g, int x, int y) {        
        g.setColor(figureColor);
        Font myFont = new Font("Times New Roman", Font.BOLD, 40);
        g.setFont(myFont);
        g.drawString(chessPieceName, x+30, y+50);
    }    
    
    
}