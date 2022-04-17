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
import java.util.Map;

/**
 *
 * @author Piotr Lach
 */
class PieceImageLoader {

    static final PieceImageLoader INSTANCE = new PieceImageLoader();

    private final Map<String, Image> whitePieces;
    private final Map<String, Image> blackPieces;

    @SneakyThrows
    private PieceImageLoader() {
        blackPieces = loadImageSet("black");
        whitePieces = loadImageSet("white");
    }

    private Map<String, Image> loadImageSet(final String color) throws Exception {
        return Map.ofEntries(
                Map.entry("L", loadImage(color, "pawn.png")),
                Map.entry("H", loadImage(color, "pawn.png")),
                Map.entry("B", loadImage(color, "bishop.png")),
                Map.entry("N", loadImage(color, "knight.png")),
                Map.entry("R", loadImage(color, "rook.png")),
                Map.entry("K", loadImage(color, "king.png")),
                Map.entry("Q", loadImage(color, "queen.png")),
                Map.entry("E", new BufferedImage(1, 1, 1))
        );
    }

    private BufferedImage loadImage(final String color, final String fileName) throws Exception {
        return ImageIO.read(getResource(color + File.separator + fileName));
    }

    private InputStream getResource(final String fileName) {
        val classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    Image getImage(final String symbol, final String color) {
        if (color.equals("B")) {
            return blackPieces.get(symbol);
        } else {
            return whitePieces.get(symbol);
        }
    }
}
