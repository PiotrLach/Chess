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
 * @author Piotr Lach
 */
abstract public class ChessPiece {    
    public ChessPiece(PieceName pieceName, Color figureColor, Image img/*, int x, int y*/) {
        this.pieceName = pieceName;
        this.figureColor = figureColor;
        this.img = img;
//        this.x=x;
//        this.y=y;
    }
    public void drawImage(Graphics g, int drawX, int drawY, int width, int height) {
        g.drawImage(img, drawX, drawY, width, height, null);
    }
    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen 
    }
    public Color getFigureColor() {
        return figureColor;
    }
    public boolean isFoe(ChessPiece cp) {        
        return cp.getFigureColor()!=figureColor;
    }
    public PieceName getPieceName () {
        return pieceName;
    }
    abstract public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2);
    
    private static String message;
    protected String chessPieceName;  
    protected Color figureColor;
//    protected int x,y;    
    protected Image img;
    protected PieceName pieceName;
}