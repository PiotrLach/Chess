/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import my.chess.pieces.ChessPiece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author bruce
 */
public class ChessField extends Rectangle {
    
    private ChessPiece currentChessPiece;
    private int matrixCoordinateX;
    private int matrixCoordinateY;
    private boolean  highlighted;
    public ChessField(int x, int y, int width, int height, int matrixCoordinateX,
                      int matrixCoordinateY) {
        super(x, y, width, height);       
        this.matrixCoordinateX=matrixCoordinateX;
        this.matrixCoordinateY=matrixCoordinateY;        
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

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }        

    public int getMatrixCoordinateX() {
        return matrixCoordinateX;
    }

    public void setMatrixCoordinateX(int matrixCoordinateX) {
        this.matrixCoordinateX = matrixCoordinateX;
    }

    public int getMatrixCoordinateY() {
        return matrixCoordinateY;
    }

    public void setMatrixCoordinateY(int matrixCoordinateY) {
        this.matrixCoordinateY = matrixCoordinateY;
    }

    public ChessPiece getCurrentChessPiece() {
        return currentChessPiece;
    }

    public void setCurrentChessPiece(ChessPiece currentChessPiece) {
        this.currentChessPiece = currentChessPiece;
    }    

    @Override
    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    @Override
    public String toString(){
        return "x: "+x+", y: "+y + ", matrixCoordinateX: " +matrixCoordinateX+
                ", matrixCoordinateY: " +matrixCoordinateY;
    }        
    
}
