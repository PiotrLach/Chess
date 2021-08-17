/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess.pieces;

import java.awt.Color;
import java.awt.Image;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Piotr Lach
 */
public class Images {

//    private Images() {
//        try {
//            loadImages();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    
//    private static Images images = new Images();   

    
    private static InputStream getResource(String fileName, ClassLoader classLoader) throws Exception {
        // The class loader that loaded the class        
        var inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new Exception("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
    
    public static void loadImages(ClassLoader classLoader) throws Exception {
        PAWN_BLACK = ImageIO.read(getResource("black/pawn.png", classLoader));
        PAWN_WHITE = ImageIO.read(getResource("white/pawn.png", classLoader));
        ROOK_BLACK = ImageIO.read(getResource("black/rook.png", classLoader));
        ROOK_WHITE = ImageIO.read(getResource("white/rook.png", classLoader));
        BISHOP_BLACK = ImageIO.read(getResource("black/bishop.png", classLoader));
        BISHOP_WHITE = ImageIO.read(getResource("white/bishop.png", classLoader));
        KNIGHT_BLACK = ImageIO.read(getResource("black/knight.png", classLoader));
        KNIGHT_WHITE = ImageIO.read(getResource("white/knight.png", classLoader));
        QUEEN_BLACK = ImageIO.read(getResource("black/queen.png", classLoader));
        QUEEN_WHITE = ImageIO.read(getResource("white/queen.png", classLoader));
        KING_BLACK = ImageIO.read(getResource("black/king.png", classLoader));
        KING_WHITE = ImageIO.read(getResource("white/king.png", classLoader));
    }   

    public static Image getPAWN(Color color) {
        if (color ==  Color.BLACK) {
            return PAWN_BLACK;
        } else {
            return PAWN_WHITE;
        }
    }

    public static Image getROOK(Color color) {
        if (color ==  Color.BLACK) {
            return ROOK_BLACK;
        } else {
            return ROOK_WHITE;
        }
    }

    public static Image getBISHOP(Color color) {
        if (color ==  Color.BLACK) {
            return BISHOP_BLACK;
        } else {
            return BISHOP_WHITE;
        }
    }

    public static Image getKNIGHT(Color color) {
        if (color ==  Color.BLACK) {
            return KNIGHT_BLACK;
        } else {
            return KNIGHT_WHITE;
        }
    }

    public static Image getQUEEN(Color color) {
        if (color ==  Color.BLACK) {
            return QUEEN_BLACK;
        } else {
            return QUEEN_WHITE;
        }
    }

    public static Image getKING(Color color) {
        if (color ==  Color.BLACK) {
            return KING_BLACK;
        } else {
            return KING_WHITE;
        }
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
