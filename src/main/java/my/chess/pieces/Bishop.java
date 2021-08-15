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
public class Bishop extends ChessPiece {

    public Bishop(Color figureColor) {
        super(PieceName.Bishop, figureColor, Images.getBISHOP(figureColor));
    }

    @Override
    public boolean movementConditionFullfilled(int x1, int y1, int x2, int y2) {
        boolean movement = Math.abs(x1 - x2) == Math.abs(y1 - y2);
        return movement;
    }

}
