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

package com.github.piotrlach.chess.gui.frame.controllers.keys;

import com.github.piotrlach.chess.gui.drawable.drawables.GameSquare;
import lombok.Getter;
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.Comparator;

public class Left implements Direction {
    @Getter
    private final int keyCode = KeyEvent.VK_A;
    @Getter
    private final Comparator<Integer> comparator = Comparator.reverseOrder();

    @Override
    public int mapToDimension(GameSquare next) {
        return next.coord.col;
    }

    @Override
    public boolean isNextOutsideDimension(GameSquare source, GameSquare target) {
        return target.coord.col < source.coord.col;
    }

    @Override
    public boolean isNextInDimension(GameSquare source, GameSquare target) {
        if (!source.isInSameRow(target))  {
            return false;
        }

        val from = source.coord.col;
        val to = target.coord.col;

        if (from > 0) {
            return to < from;
        } else if (from == 0) {
            return to == 7;
        } else {
            return false;
        }
    }
}
