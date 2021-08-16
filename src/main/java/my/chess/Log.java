/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import my.chess.pieces.Piece.PieceName;

/**
 *
 * @author bruce
 */
public class Log {

    public Log(Color color, int x1, int y1, int x2, int y2, PieceName name) {
        this.color = color;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.name = name;
    }
    public final Color color;
    public final int x1, y1, x2, y2;
    public final PieceName name;
}
