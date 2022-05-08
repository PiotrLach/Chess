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

import java.util.List;

public class SelectableTarget implements SelectableSquare {
    private final List<GameSquare> squares;
    private final Logic logic;
    private GameSquare selectedTarget;
    private final SelectableSquare selectedSource;

    public SelectableTarget(GameBoard board, List<GameSquare> squares) {
        this.squares = board.getSquares();
        this.logic = board.getLogic();
        this.selectedTarget = squares.get(0);
        this.selectedSource = board.getSelectedSource();
    }

    @Override
    public boolean isSelected() {
        return selectedTarget.isSelectedTarget();
    }

    @Override
    public void unselect() {
        selectedTarget.setSelectedTarget(false);
    }

    @Override
    public void set(int index) {
        selectedTarget.setSelectedTarget(false);
        selectedTarget = squares.get(index);
        selectedTarget.setSelectedTarget(true);
    }

    @Override
    public void set(GameSquare square) {
        selectedTarget.setSelectedTarget(false);
        selectedTarget = square;
        selectedTarget.setSelectedTarget(true);
    }

    @Override
    public GameSquare get() {
        return selectedTarget;
    }

    @Override
    public boolean isValid(GameSquare square) {
        return !square.isSelectedSource() && selectedSource.isSelected() && square.hasFoe(logic.getCurrentColor());
    }
}
