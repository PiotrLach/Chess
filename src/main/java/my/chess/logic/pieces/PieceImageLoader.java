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
package my.chess.logic.pieces;

import lombok.SneakyThrows;
import lombok.val;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Piotr Lach
 */
class PieceImageLoader {

    static final PieceImageLoader INSTANCE = new PieceImageLoader();

    private final List<Image> whitePieces;
    private final List<Image> blackPieces;

    @SneakyThrows
    private PieceImageLoader() {
        blackPieces = loadImageSet("black");
        whitePieces = loadImageSet("white");
    }

    private List<Image> loadImageSet(final String color) throws Exception {
        return List.of(
                loadImage(color, "pawn.png"),
                loadImage(color, "bishop.png"),
                loadImage(color, "knight.png"),
                loadImage(color, "rook.png"),
                loadImage(color, "king.png"),
                loadImage(color, "queen.png"),
                new BufferedImage(1, 1, 1)
        );
    }

    private BufferedImage loadImage(final String color, final String fileName) throws Exception {
        return ImageIO.read(getResource(color + File.separator + fileName));
    }

    private InputStream getResource(final String fileName) throws Exception {

        val classLoader = getClass().getClassLoader();
        val inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new Exception("File not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    Image getImage(final Piece.Name name, final Color color) {
        if (color.equals(Color.BLACK)) {
            return blackPieces.get(name.imageId);
        } else {
            return whitePieces.get(name.imageId);
        }
    }
}
