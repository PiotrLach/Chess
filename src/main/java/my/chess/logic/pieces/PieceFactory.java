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
package my.chess.logic.pieces;

import lombok.RequiredArgsConstructor;
import my.chess.logic.Logic;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class PieceFactory {

    private final Logic logic;

    public Piece getPiece(String string) {

        if (!isValidString(string)) {
            throw new IllegalArgumentException("Wrong string specified!");
        }

        var values = string.split(";");

        if (isEmptySquare(values)) {
            return Empty.INSTANCE;
        }

        var pieceSymbol = values[0];
        var colorSymbol = values[1];

        var color = getColor(colorSymbol);

        return getPieces(color)
                .stream()
                .filter(piece -> piece.symbol.equals(pieceSymbol))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Wrong piece symbol specified!"));
    }

    private boolean isValidString(String string) {
        return string.matches("([A-Z]|\\s);([A-Z]|\\s)");
    }

    private boolean isEmptySquare(String[] values) {
        return values[0].isBlank() && values[1].isBlank();
    }

    private Color getColor(String colorSymbol) {
        return switch (colorSymbol) {
            default -> throw new IllegalArgumentException("Wrong color symbol specified!");
            case "W" -> Color.WHITE;
            case "B" -> Color.BLACK;
        };
    }

    private List<Piece> getPieces(Color color) {
        return List.of(
            new Pawn(color, "L", logic),
            new Pawn(color, "H", logic),
            new Bishop(color, logic),
            new Knight(color, logic),
            new Rook(color, logic),
            new King(color, logic),
            new Queen(color, logic),
            Empty.INSTANCE
        );
    }
}
