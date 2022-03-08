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
package my.chess.logic;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author Piotr Lach
 */
@EqualsAndHashCode
@ToString
public class Coord implements Serializable {

    @Serial
    private static final long serialVersionUID = 5888462570748191830L;

    public final int index;
    public final int row;
    public final int col;

    public Coord(int rank, char file) {
        this.row = rank - 1;
        this.col = file - 65;
        index = row * 8 + col;
    }

    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
        index = row * 8 + col;
    }

    public Coord(int index) {
        this.index = index;
        col = index % 8;
        row = (index - col) / 8;
    }
}
