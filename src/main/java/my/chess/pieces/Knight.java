/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;
import java.awt.Image;

/**
 *
 * @author Piotr Lach
 */
public class Knight extends Piece {

    public Knight(Color figureColor) {
        super(PieceName.Knight, figureColor, Images.getKNIGHT(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        boolean movement = (Math.abs(source.row - target.row) == 2 && Math.abs(source.col - target.col) == 1)
                || (Math.abs(source.row - target.row) == 1 && Math.abs(source.col - target.col) == 2);
        return movement;
    }

}
