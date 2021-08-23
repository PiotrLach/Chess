/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import my.chess.Coord;

/**
 *
 * @author Piotr Lach
 */
public class Bishop extends Piece {

    public Bishop(Color figureColor) {
        super(PieceName.Bishop, figureColor, Images.getBISHOP(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        boolean movement = Math.abs(source.row - target.row) == Math.abs(source.col - target.col);
        return movement;
    }

}
