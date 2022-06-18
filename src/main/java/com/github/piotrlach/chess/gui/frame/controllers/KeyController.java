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

package com.github.piotrlach.chess.gui.frame.controllers;

import com.github.piotrlach.chess.gui.drawable.drawables.GameSquare;
import com.github.piotrlach.chess.gui.frame.GameBoard;
import com.github.piotrlach.chess.gui.frame.controllers.keys.*;
import com.github.piotrlach.chess.gui.frame.selectable.SelectableSquare;
import com.github.piotrlach.chess.logic.Logic;
import lombok.Setter;
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.*;

public class KeyController {

    private final List<GameSquare> squares;
    private final Logic logic;
    private final SelectableSquare selectedSource;
    private final SelectableSquare selectedTarget;
    @Setter
    private boolean selectTarget = false;
    private static final Direction[] DIRECTIONS = {
            new Up(),
            new Left(),
            new Down(),
            new Right()
    };

    public KeyController(GameBoard board, List<GameSquare> squares) {
        this.squares = squares;
        this.logic = board.getLogic();
        this.selectedSource = board.getSelectedSource();
        this.selectedTarget = board.getSelectedTarget();
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();

        if (isSelectionTypeChange(keyCode)) {
            selectTarget = !selectTarget;
        }

        if (isMovementAttempt(keyCode)) {
            val source = selectedSource.get();
            val target = selectedTarget.get();
            tryToMovePiece(source, target);
            return;
        }

        Arrays.stream(DIRECTIONS)
            .filter(direction -> direction.getKeyCode() == keyCode)
            .findAny()
            .ifPresent(direction -> {
                if (selectTarget) {
                    select(direction, selectedTarget);
                } else {
                    select(direction, selectedSource);
                }
            });
    }


    private boolean isSelectionTypeChange(int keyboardKey) {
        return selectedSource.isSelected() && keyboardKey == KeyEvent.VK_SPACE;
    }

    private boolean isMovementAttempt(int keyboardKey) {
        return selectedSource.isSelected() && selectedTarget.isSelected() && keyboardKey == KeyEvent.VK_SPACE;
    }

    private void tryToMovePiece(GameSquare source, GameSquare target) {
        logic.movePiece(source.coord, target.coord);
        selectTarget = false;
        selectedSource.unselect();
        selectedTarget.unselect();
    }

    private void select(Direction key, SelectableSquare selectableSquare) {
        if (!selectableSquare.isSelected()) {
            setAny(selectableSquare);
            return;
        }

        selectableSquare.unselect();

        findClosestInDimension(key, selectableSquare);
    }

    private void setAny(SelectableSquare selectableSquare) {
        squares.stream()
                .filter(selectableSquare::isValid)
                .findAny()
                .ifPresent(selectableSquare::set);
    }

    private void findClosestInDimension(Direction direction, SelectableSquare selectableSquare) {
        squares.stream()
                .filter(selectableSquare::isValid)
                .filter(square -> direction.isNextInDimension(selectableSquare.get(), square))
                .map(next -> next.coord.index)
                .min(direction.getComparator())
                .ifPresentOrElse(selectableSquare::set, () -> setAnyClosestInDirection(direction, selectableSquare));
    }

    private void setAnyClosestInDirection(Direction direction, SelectableSquare selectableSquare) {
        squares.stream()
                .filter(selectableSquare::isValid)
                .filter(square -> direction.isNextOutsideDimension(selectableSquare.get(), square))
                .map(direction::mapToDimension)
                .min(direction.getComparator())
                .ifPresent(index -> setAnyInDimension(direction, selectableSquare, index));
    }

    private void setAnyInDimension(Direction direction, SelectableSquare selectableSquare, int dimIndex) {
        squares.stream()
                .filter(selectableSquare::isValid)
                .filter(square -> direction.mapToDimension(square) == dimIndex)
                .min(Comparator.naturalOrder())
                .ifPresent(selectableSquare::set);
    }
}