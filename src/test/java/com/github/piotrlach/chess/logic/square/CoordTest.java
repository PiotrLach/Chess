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
package com.github.piotrlach.chess.logic.square;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordTest {
    @Test
    public void constructorParams() {
        Assertions.assertDoesNotThrow(() -> new Coord(1, 'C'));
        Assertions.assertDoesNotThrow(() -> new Coord(0, 7));
        Assertions.assertDoesNotThrow(() -> new Coord(53));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(0, 'C'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(1, 'Z'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(10, 'X'));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(0, 8));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(-5, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(10, 10));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(-22));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(64));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Coord(70));
    }
}
