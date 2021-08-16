/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Piotr Lach
 */
abstract public class Piece {

    public Piece(PieceName pieceName, Color figureColor, Image img) {
        this.pieceName = pieceName;
        this.figureColor = figureColor;
        this.img = img;
    }

    public void drawImage(Graphics graphics, int drawX, int drawY, int width, int height) {
        graphics.drawImage(img, drawX, drawY, width, height, null);
    }

    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen
    }

    public Color getFigureColor() {
        return figureColor;
    }

    public boolean isFoe(Piece piece) {
        return piece.getFigureColor() != figureColor;
    }

    public PieceName getPieceName() {
        return pieceName;
    }

    abstract public boolean isCorrectMovement(int x1, int y1, int x2, int y2);

    private static String message;
    protected String chessPieceName;
    protected Color figureColor;
    protected Image img;
    protected PieceName pieceName;
}
