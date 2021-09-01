/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import my.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Piotr Lach
 */
public class Square extends Rectangle {

    private Piece piece;
    private boolean highlighted;

    public Square(int x, int y, int width, int height) {
        super(x, y, width, height);       
    }

    public void drawSquare(Graphics graphics, int row, int col) {        
        
        var isRowOdd = row % 2 == 1;
        var isColOdd = col % 2 == 1;
        
        var isWhite = (isRowOdd && !isColOdd) || (!isRowOdd && isColOdd);
                
        if (isWhite) {
            graphics.setColor(myWhite);
        } else {
            graphics.setColor(myBrown);
        }
        graphics.fillRect(x, y, width, height);
    }

    public void highlightSquare(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(x, y, width, height);
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    private final Color myWhite = new Color(255, 255, 204),
            myBrown = new Color(153, 102, 0);

}
