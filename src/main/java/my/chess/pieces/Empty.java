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
package my.chess.pieces;

import java.awt.Color;
import java.awt.Graphics;
import my.chess.Square;

/**
 *
 * @author Piotr Lach
 */
public class Empty extends Piece {

    public static final Empty INSTANCE = new Empty();

    private Empty() {
        super(Name.Empty, Color.GRAY, null);
    }

    @Override
    public void move(Square source, Square target) {}

    @Override
    public void drawImage(Graphics graphics, int x, int y, int size) {}

    @Override
    public void setImage() {}

    @Override
    public boolean isCorrectMovement(Square source, Square target) {
        return false;
    }

}
