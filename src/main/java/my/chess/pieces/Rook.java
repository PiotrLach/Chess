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
public class Rook extends Piece {

    public Rook(Color figureColor) {
        super(PieceName.Rook, figureColor, Images.getROOK(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        boolean movement = (Math.abs(source.row - target.row) > 0 && Math.abs(source.col - target.col) == 0)
                || (Math.abs(source.row - target.row) == 0 && Math.abs(source.col - target.col) > 0);

        return movement;
    }

}
