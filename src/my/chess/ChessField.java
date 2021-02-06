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
 * @author Piotr Lach
 */
public class ChessField extends Rectangle {

    private ChessPiece currentChessPiece;
    private final int matrixCoordinateX;
    private final int matrixCoordinateY;
    private boolean highlighted;

    public ChessField(int x, int y, int width, int height, int matrixCoordinateX,
            int matrixCoordinateY) {
        super(x, y, width, height);
        this.matrixCoordinateX = matrixCoordinateX;
        this.matrixCoordinateY = matrixCoordinateY;
    }

    public void drawChessField(Graphics g, int row, int column) {
        Color myWhite = new Color(255, 255, 204);
        Color myBrown = new Color(153, 102, 0);
        if ((row % 2 == 1 && column % 2 == 0) || (row % 2 == 0 && column % 2 == 1)) {
            g.setColor(myWhite);
        } else {
            g.setColor(myBrown);
        }
        g.fillRect(x, y, width, height);
    }

    public void highlightChessField(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public ChessPiece getCurrentChessPiece() {
        return currentChessPiece;
    }

    public void setCurrentChessPiece(ChessPiece currentChessPiece) {
        this.currentChessPiece = currentChessPiece;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + ", matrixCoordinateX: " + matrixCoordinateX
                + ", matrixCoordinateY: " + matrixCoordinateY;
    }

}
