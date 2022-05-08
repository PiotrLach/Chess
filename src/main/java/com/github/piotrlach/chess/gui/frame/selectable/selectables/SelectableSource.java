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

package com.github.piotrlach.chess.gui.frame.selectable.selectables;

import com.github.piotrlach.chess.gui.drawable.drawables.GameSquare;
import com.github.piotrlach.chess.gui.frame.GameBoard;
import com.github.piotrlach.chess.gui.frame.selectable.SelectableSquare;
import com.github.piotrlach.chess.logic.Logic;
import lombok.NonNull;

import java.util.List;

public class SelectableSource implements SelectableSquare {
    private final List<GameSquare> squares;
    @NonNull
    private GameSquare selectedSource;
    private final Logic logic;

    public SelectableSource(GameBoard board, List<GameSquare> squares) {
        this.squares = squares;
        this.logic = board.getLogic();
        this.selectedSource = squares.get(0);
    }

    @Override
    public boolean isSelected() {
        return selectedSource.isSelectedSource();
    }

    @Override
    public void unselect() {
        selectedSource.setSelectedSource(false);
    }

    @Override
    public void set(int index) {
        selectedSource.setSelectedSource(false);
        selectedSource = squares.get(index);
        selectedSource.setSelectedSource(true);
    }

    @Override
    public void set(GameSquare square) {
        selectedSource.setSelectedSource(false);
        selectedSource = square;
        selectedSource.setSelectedSource(true);
    }

    @Override
    public GameSquare get() {
        return selectedSource;
    }

    @Override
    public boolean isValid(GameSquare square) {
        return !square.hasFoe(logic.getCurrentColor()) && !square.isSelectedSource();
    }
}