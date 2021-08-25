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
public class Rook extends Piece {

    public Rook(Color figureColor) {
        super(PieceName.Rook, figureColor, Images.getROOK(figureColor));
    }

    @Override
    public boolean isCorrectMovement(Coord source, Coord target) {
        
        int vDiff, hDiff; // vertical and horizontal difference;
        vDiff = Math.abs(source.row - target.row);
        hDiff = Math.abs(source.col - target.col);
        
        var isVerticalMov = vDiff > 0;
        var isHorizontalMov = hDiff > 0;        
        
        return (isVerticalMov && !isHorizontalMov) || (isHorizontalMov && !isVerticalMov);
    }

}
