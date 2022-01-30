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
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Piotr Lach
 */
class PieceImageLoader {

    static final PieceImageLoader INSTANCE = new PieceImageLoader();

    private List<Image> whitePieces;
    private List<Image> blackPieces;

    private PieceImageLoader() {
        try {
            loadImages();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void loadImages() throws Exception {

        blackPieces = List.of(
            ImageIO.read(getResource("black/pawn.png")),
            ImageIO.read(getResource("black/bishop.png")),
            ImageIO.read(getResource("black/knight.png")),
            ImageIO.read(getResource("black/rook.png")),
            ImageIO.read(getResource("black/king.png")),
            ImageIO.read(getResource("black/queen.png")),
            new BufferedImage(1, 1, 1)
        );

        whitePieces = List.of(
            ImageIO.read(getResource("white/pawn.png")),
            ImageIO.read(getResource("white/bishop.png")),
            ImageIO.read(getResource("white/knight.png")),
            ImageIO.read(getResource("white/rook.png")),
            ImageIO.read(getResource("white/king.png")),
            ImageIO.read(getResource("white/queen.png")),
            new BufferedImage(1, 1, 1)
        );
    }

    private InputStream getResource(String fileName) throws Exception {

        var classLoader = getClass().getClassLoader();

        var inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new Exception("File not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    Image getImage(Piece.Name name, Color color) {

        if (color.equals(Color.BLACK)) {
            return blackPieces.get(name.imageId);
        } else {
            return whitePieces.get(name.imageId);
        }
    }
}
