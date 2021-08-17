/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
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
    public boolean isCorrectMovement(int x1, int y1, int x2, int y2) {
        boolean movement = (Math.abs(x1 - x2) > 0 && Math.abs(y1 - y2) == 0)
                || (Math.abs(x1 - x2) == 0 && Math.abs(y1 - y2) > 0);

        return movement;
    }

}