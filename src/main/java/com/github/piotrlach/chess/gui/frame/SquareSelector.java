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
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

public class SquareSelector implements Serializable {
    private final List<GameSquare> squares;
    private final Logic logic;
    @NonNull
    private GameSquare selectedSource;
    @NonNull
    private GameSquare selectedTarget;

    public SquareSelector(List<GameSquare> squares, Logic logic) {
        this.squares = squares;
        this.logic = logic;
        selectedSource = retrieveAnySquare();
        selectedTarget = retrieveAnySquare();
    }


    private GameSquare retrieveAnySquare() {
        return squares.stream()
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }


    public void setSelected(GameSquare gameSquare, GameSquare.Type type) {
        if (type.equals(GameSquare.Type.SOURCE)) {
            selectedSource = gameSquare;
            selectedSource.setType(type);
        } else if (type.equals(GameSquare.Type.TARGET)) {
            selectedTarget = gameSquare;
            selectedTarget.setType(type);
        } else {
            throw new IllegalArgumentException("Invalid square type has been specified!");
        }
    }

    public void setSelected(int index, GameSquare.Type type) {
        if (type.equals(GameSquare.Type.SOURCE)) {
            selectedSource = squares.get(index);
            selectedSource.setType(type);
        } else if (type.equals(GameSquare.Type.TARGET)) {
            selectedTarget = squares.get(index);
            selectedTarget.setType(type);
        } else {
            throw new IllegalArgumentException("Invalid square type has been specified!");
        }
    }

    public GameSquare getSelected(GameSquare.Type type) {
        if (type.equals(GameSquare.Type.SOURCE)) {
            return selectedSource;
        } else if (type.equals(GameSquare.Type.TARGET)) {
            return selectedTarget;
        } else {
            throw new IllegalArgumentException("Invalid square type has been specified!");
        }
    }

    public boolean isValid(GameSquare gameSquare, GameSquare.Type type) {
        return gameSquare.isValid(type, logic.getCurrentColor());
    }

    public void unselect(GameSquare.Type type) {
        if (type.equals(GameSquare.Type.SOURCE)) {
            selectedSource.unselect();
        } else if (type.equals(GameSquare.Type.TARGET)) {
            selectedTarget.unselect();
        } else {
            throw new IllegalArgumentException("Invalid square type has been specified!");
        }
    }

    public boolean isSelected(GameSquare.Type type) {
        if (type.equals(GameSquare.Type.SOURCE)) {
            return selectedSource.getType().equals(GameSquare.Type.SOURCE);
        } else if (type.equals(GameSquare.Type.TARGET)) {
            return selectedTarget.getType().equals(GameSquare.Type.TARGET);
        } else {
            throw new IllegalArgumentException("Invalid square type has been specified!");
        }
    }
}