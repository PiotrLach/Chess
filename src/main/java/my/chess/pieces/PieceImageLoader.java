/* 
 * Java chess game implementation
 * Copyright (C) 2021 Piotr Lach
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
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
public class PieceImageLoader {

    private PieceImageLoader() {
        try {
            loadImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static final PieceImageLoader INSTANCE = new PieceImageLoader();
    
    private InputStream getResource(String fileName) throws Exception {        
        
        var classLoader = getClass().getClassLoader();
        
        var inputStream = classLoader.getResourceAsStream(fileName);
        
        if (inputStream == null) {
            throw new Exception("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
    
    private void loadImages() throws Exception {
        PAWN_BLACK = ImageIO.read(getResource("black/pawn.png"));
        PAWN_WHITE = ImageIO.read(getResource("white/pawn.png"));
        ROOK_BLACK = ImageIO.read(getResource("black/rook.png"));
        ROOK_WHITE = ImageIO.read(getResource("white/rook.png"));
        BISHOP_BLACK = ImageIO.read(getResource("black/bishop.png"));
        BISHOP_WHITE = ImageIO.read(getResource("white/bishop.png"));
        KNIGHT_BLACK = ImageIO.read(getResource("black/knight.png"));
        KNIGHT_WHITE = ImageIO.read(getResource("white/knight.png"));
        QUEEN_BLACK = ImageIO.read(getResource("black/queen.png"));
        QUEEN_WHITE = ImageIO.read(getResource("white/queen.png"));
        KING_BLACK = ImageIO.read(getResource("black/king.png"));
        KING_WHITE = ImageIO.read(getResource("white/king.png"));
    }   

    public Image getPAWN(Color color) {
        if (color.equals(Color.BLACK)) {
            return PAWN_BLACK;
        } else {
            return PAWN_WHITE;
        }
    }

    public Image getROOK(Color color) {
        if (color.equals(Color.BLACK)) {
            return ROOK_BLACK;
        } else {
            return ROOK_WHITE;
        }
    }

    public Image getBISHOP(Color color) {
        if (color.equals(Color.BLACK)) {
            return BISHOP_BLACK;
        } else {
            return BISHOP_WHITE;
        }
    }

    public Image getKNIGHT(Color color) {
        if (color.equals(Color.BLACK)) {
            return KNIGHT_BLACK;
        } else {
            return KNIGHT_WHITE;
        }
    }

    public Image getQUEEN(Color color) {
        if (color.equals(Color.BLACK)) {
            return QUEEN_BLACK;
        } else {
            return QUEEN_WHITE;
        }
    }

    public Image getKING(Color color) {
        if (color.equals(Color.BLACK)) {
            return KING_BLACK;
        } else {
            return KING_WHITE;
        }
    }
    private Image PAWN_BLACK;
    private Image PAWN_WHITE;
    private Image ROOK_BLACK;
    private Image ROOK_WHITE;
    private Image BISHOP_BLACK;
    private Image BISHOP_WHITE;
    private Image KNIGHT_BLACK;
    private Image KNIGHT_WHITE;
    private Image QUEEN_BLACK;
    private Image QUEEN_WHITE;
    private Image KING_BLACK;
    private Image KING_WHITE;
}
