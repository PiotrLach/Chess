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
package my.chess.logic.square;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;

/**
 *
 * @author Piotr Lach
 */
@EqualsAndHashCode
@ToString
public class Coord {

    public final int index;
    public final int row;
    public final int col;

    public Coord(final int rank, final char file) throws IllegalArgumentException {

        if (!isValidInput(rank, file)) {
            throw new IllegalArgumentException("Wrong input arguments passed!");
        }

        this.row = rank - 1;
        this.col = file - 65;
        index = row * 8 + col;
    }

    public Coord(final int row, final int col) throws IllegalArgumentException {

        if (!isValidInput(row, col)) {
            throw new IllegalArgumentException("Wrong input arguments passed!");
        }

        this.row = row;
        this.col = col;
        index = row * 8 + col;
    }

    public Coord(final int index) throws IllegalArgumentException {

        if (!isValidInput(index)) {
            throw new IllegalArgumentException("Wrong input arguments passed!");
        }

        this.index = index;
        col = index % 8;
        row = (index - col) / 8;
    }

    private boolean isValidInput(final int rank, final char file) {
        val isValidRank = rank >= 1 && rank <= 8;
        val isValidFile = file >= 65 && file <= 72;
        return isValidRank && isValidFile;
    }

    private boolean isValidInput(final int row, final int col) {
        val isValidRow = row >= 0 && row <= 7;
        val isValidCol = col >= 0 && col <= 7;
        return isValidRow && isValidCol;
    }

    private boolean isValidInput(final int index) {
        return index >= 0 && index <= 63;
    }
}
