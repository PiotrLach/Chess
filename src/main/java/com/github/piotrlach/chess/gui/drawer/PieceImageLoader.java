/*
 * Copyright (C) 2022 Piotr Lach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.piotrlach.chess.gui.drawer;

import com.github.piotrlach.chess.logic.pieces.*;
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

    private final Map<Class<? extends Piece>, Image> whiteImages;
    private final Map<Class<? extends Piece>, Image> blackImages;

    @SneakyThrows
    private PieceImageLoader() {
        blackImages = loadImageSet("black");
        whiteImages = loadImageSet("white");
    }

    private Map<Class<? extends Piece>, Image> loadImageSet(final String color) throws Exception {
        return Map.ofEntries(
                Map.entry(Pawn.class, loadImage(color, "pawn.png")),
                Map.entry(Bishop.class, loadImage(color, "bishop.png")),
                Map.entry(Knight.class, loadImage(color, "knight.png")),
                Map.entry(Rook.class, loadImage(color, "rook.png")),
                Map.entry(King.class, loadImage(color, "king.png")),
                Map.entry(Queen.class, loadImage(color, "queen.png"))
        );
    }

    private BufferedImage loadImage(final String color, final String fileName) throws Exception {
        return ImageIO.read(getResource(color + File.separator + fileName));
    }

    private InputStream getResource(final String fileName) {
        val classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    Image getImage(final Class<? extends Piece> clazz, final String color) {
        if (color.equals("B")) {
            return blackImages.get(clazz);
        } else if(color.equals("W")) {
            return whiteImages.get(clazz);
        } else {
            return null;
        }
    }
}
