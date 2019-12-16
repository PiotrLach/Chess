/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Edward Cullen
 */
public class Images {
    
    public static void loadImages() throws IOException {
        PAWN_BLACK = ImageIO.read(new File("res/black/pawn.png"));
        PAWN_WHITE = ImageIO.read(new File("res/white/pawn.png"));
        ROOK_BLACK = ImageIO.read(new File("res/black/rook.png"));
        ROOK_WHITE = ImageIO.read(new File("res/white/rook.png"));
        BISHOP_BLACK = ImageIO.read(new File("res/black/bishop.png"));
        BISHOP_WHITE = ImageIO.read(new File("res/white/bishop.png"));
        KNIGHT_BLACK = ImageIO.read(new File("res/black/knight.png"));
        KNIGHT_WHITE = ImageIO.read(new File("res/white/knight.png"));
        QUEEN_BLACK = ImageIO.read(new File("res/black/queen.png"));
        QUEEN_WHITE = ImageIO.read(new File("res/white/queen.png"));
        KING_BLACK = ImageIO.read(new File("res/black/king.png"));
        KING_WHITE = ImageIO.read(new File("res/white/king.png"));
    }

    public static Image getPAWN_BLACK() {
        return PAWN_BLACK;
    }

    public static Image getPAWN_WHITE() {
        return PAWN_WHITE;
    }

    public static Image getROOK_BLACK() {
        return ROOK_BLACK;
    }

    public static Image getROOK_WHITE() {
        return ROOK_WHITE;
    }

    public static Image getBISHOP_BLACK() {
        return BISHOP_BLACK;
    }

    public static Image getBISHOP_WHITE() {
        return BISHOP_WHITE;
    }

    public static Image getKNIGHT_BLACK() {
        return KNIGHT_BLACK;
    }

    public static Image getKNIGHT_WHITE() {
        return KNIGHT_WHITE;
    }

    public static Image getQUEEN_BLACK() {
        return QUEEN_BLACK;
    }

    public static Image getQUEEN_WHITE() {
        return QUEEN_WHITE;
    }

    public static Image getKING_BLACK() {
        return KING_BLACK;
    }

    public static Image getKING_WHITE() {
        return KING_WHITE;
    }
    
    private static Image PAWN_BLACK;    
    private static Image PAWN_WHITE;
    private static Image ROOK_BLACK;
    private static Image ROOK_WHITE;
    private static Image BISHOP_BLACK;
    private static Image BISHOP_WHITE;
    private static Image KNIGHT_BLACK;
    private static Image KNIGHT_WHITE;
    private static Image QUEEN_BLACK;
    private static Image QUEEN_WHITE;
    private static Image KING_BLACK;
    private static Image KING_WHITE;
}
