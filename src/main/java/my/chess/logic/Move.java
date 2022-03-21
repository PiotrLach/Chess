/*
 * Copyright (C) 2021 Piotr Lach
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
package my.chess.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import my.chess.logic.pieces.Empty;
import my.chess.logic.pieces.Piece;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author Piotr Lach
 */
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Move implements Serializable {

    @Serial
    private static final long serialVersionUID = -5610296767681737878L;

    public final Coord from;
    public final Coord to;
    @Getter
    private Piece promotedPiece = Empty.INSTANCE;
}
