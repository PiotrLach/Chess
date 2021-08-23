/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Piotr Lach
 */
abstract public class Piece {

    public Piece(PieceName pieceName, Color color, Image image) {
        this.name = pieceName;
        this.color = color;
        this.image = image;
    }

    public void drawImage(Graphics graphics, int x, int y, int width, int height) {
        graphics.drawImage(image, x, y, width, height, null);
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

    abstract public boolean isCorrectMovement(Coord source, Coord target);

    public final Color color;
    protected Image image;
    protected PieceName name;
}
