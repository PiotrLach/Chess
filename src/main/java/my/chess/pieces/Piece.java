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
        this.name = pieceName;
        this.color = figureColor;
        this.image = img;
    }

    public void drawImage(Graphics graphics, int drawX, int drawY, int width, int height) {
        graphics.drawImage(image, drawX, drawY, width, height, null);
    }

    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen
    }

    public boolean isFoe(Piece piece) {
        return piece.color != color;
    }

    public PieceName getName() {
        return name;
    }

    abstract public boolean isCorrectMovement(int x1, int y1, int x2, int y2);

    public final Color color;
    protected Image image;
    protected PieceName name;
}
