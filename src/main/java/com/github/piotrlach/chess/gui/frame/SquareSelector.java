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

package com.github.piotrlach.chess.gui.frame;

import com.github.piotrlach.chess.gui.drawable.drawables.GameSquare;
import com.github.piotrlach.chess.logic.Logic;

import java.io.Serializable;
import java.util.List;

public class SquareSelector implements Serializable {
    private final List<GameSquare> squares;
    private final Logic logic;
    private final GameSquare[] selected;

    public SquareSelector(List<GameSquare> squares, Logic logic) {
        this.squares = squares;
        this.logic = logic;
        selected = new GameSquare[] {
            retrieveAnySquare(),
            retrieveAnySquare()
        };
    }

    private GameSquare retrieveAnySquare() {
        return squares.stream()
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public void setSelected(GameSquare gameSquare, GameSquare.Type type) {
        selected[type.id] = gameSquare;
        selected[type.id].setType(type);
    }

    public void setSelected(int index, GameSquare.Type type) {
        selected[type.id] = squares.get(index);
        selected[type.id].setType(type);
    }

    public GameSquare getSelected(GameSquare.Type type) {
        return selected[type.id];
    }

    public boolean isOfValidType(GameSquare gameSquare, GameSquare.Type type) {
        return gameSquare.isValid(type, logic.getCurrentColor());
    }

    public void unselect(GameSquare.Type type) {
        selected[type.id].unselect();
    }

    public boolean isSelected(GameSquare.Type type) {
        return selected[type.id].getType().equals(type);
    }
}