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
class PieceImageLoader {

    static final PieceImageLoader INSTANCE = new PieceImageLoader();

    private PieceImageLoader() {
        try {
            loadImages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadImages() throws Exception {
        blackPawn = ImageIO.read(getResource("black/pawn.png"));
        whitePawn = ImageIO.read(getResource("white/pawn.png"));
        blackRook = ImageIO.read(getResource("black/rook.png"));
        whiteRook = ImageIO.read(getResource("white/rook.png"));
        blackBishop = ImageIO.read(getResource("black/bishop.png"));
        whiteBishop = ImageIO.read(getResource("white/bishop.png"));
        blackKnight = ImageIO.read(getResource("black/knight.png"));
        whiteKnight = ImageIO.read(getResource("white/knight.png"));
        blackQueen = ImageIO.read(getResource("black/queen.png"));
        whiteQueen = ImageIO.read(getResource("white/queen.png"));
        blackKing = ImageIO.read(getResource("black/king.png"));
        whiteKing = ImageIO.read(getResource("white/king.png"));
    }

    private InputStream getResource(String fileName) throws Exception {

        var classLoader = getClass().getClassLoader();

        var inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new Exception("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    Image getPAWN(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackPawn;
        } else {
            return whitePawn;
        }
    }

    Image getROOK(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackRook;
        } else {
            return whiteRook;
        }
    }

    Image getBISHOP(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackBishop;
        } else {
            return whiteBishop;
        }
    }

    Image getKNIGHT(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackKnight;
        } else {
            return whiteKnight;
        }
    }

    Image getQUEEN(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackQueen;
        } else {
            return whiteQueen;
        }
    }

    Image getKING(Color color) {
        if (color.equals(Color.BLACK)) {
            return blackKing;
        } else {
            return whiteKing;
        }
    }
    private Image blackPawn;
    private Image whitePawn;
    private Image blackRook;
    private Image whiteRook;
    private Image blackBishop;
    private Image whiteBishop;
    private Image blackKnight;
    private Image whiteKnight;
    private Image blackQueen;
    private Image whiteQueen;
    private Image blackKing;
    private Image whiteKing;
}
