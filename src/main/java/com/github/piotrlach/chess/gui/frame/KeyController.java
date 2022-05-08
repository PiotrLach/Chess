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
import com.github.piotrlach.chess.gui.frame.selectable.SelectableSquare;
import com.github.piotrlach.chess.logic.Logic;
import lombok.Setter;
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KeyController {

    private final List<GameSquare> squares;
    private final Logic logic;
    private final SelectableSquare selectedSource;
    private final SelectableSquare selectedTarget;
    @Setter
    private boolean selectTarget = false;
    private final Map<Integer, Comparator<Integer>> comparators = Map.ofEntries(
            Map.entry(KeyEvent.VK_W, Comparator.naturalOrder()),
            Map.entry(KeyEvent.VK_A, Comparator.reverseOrder()),
            Map.entry(KeyEvent.VK_S, Comparator.reverseOrder()),
            Map.entry(KeyEvent.VK_D, Comparator.naturalOrder())
    );

    public KeyController(GameBoard board, List<GameSquare> squares) {
        this.squares = squares;
        this.logic = board.getLogic();
        this.selectedSource = board.getSelectedSource();
        this.selectedTarget = board.getSelectedTarget();
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        int keyboardKey = keyEvent.getKeyCode();

        if (isSelectionTypeChange(keyboardKey)) {
            selectTarget = !selectTarget;
        }

        if (isMovementAttempt(keyboardKey)) {
            val source = selectedSource.get();
            val target = selectedTarget.get();
            tryToMovePiece(source, target);
            return;
        }

        if (!isValidKey(keyboardKey)) {
            return;
        }

        if (selectTarget) {
            select(keyboardKey, selectedTarget);
        } else {
            select(keyboardKey, selectedSource);
        }
    }


    private boolean isSelectionTypeChange(int keyboardKey) {
        return selectedSource.isSelected() && keyboardKey == KeyEvent.VK_SPACE;
    }

    private boolean isMovementAttempt(int keyboardKey) {
        return selectedSource.isSelected() && selectedTarget.isSelected() && keyboardKey == KeyEvent.VK_SPACE;
    }

    private void tryToMovePiece(GameSquare source, GameSquare target) {
        if (logic.movePiece(source.coord, target.coord)) {
            selectTarget = false;
            selectedSource.unselect();
            selectedTarget.unselect();
        }
    }

    private boolean isValidKey(int keyboardKey) {
        return comparators.containsKey(keyboardKey);
    }

    private void select(int keyboardKey, SelectableSquare selectableSquare) {
        if (!selectableSquare.isSelected()) {
            squares.stream()
                    .filter(selectableSquare::isValid)
                    .findAny()
                    .ifPresent(selectableSquare::set);
        } else {
            selectableSquare.unselect();

            squares.stream()
                    .filter(selectableSquare::isValid)
                    .filter(square -> isNext(keyboardKey, selectableSquare.get(), square))
                    .map(next -> next.coord.index)
                    .min(comparators.get(keyboardKey))
                    .ifPresent(selectableSquare::set);
        }
    }

    private boolean isNext(int keyboardKey, GameSquare source, GameSquare target) {
       return switch (keyboardKey) {
            case KeyEvent.VK_W, KeyEvent.VK_S -> isVerticalMove(keyboardKey, source, target);
            case KeyEvent.VK_A, KeyEvent.VK_D -> isHorizontalMove(keyboardKey, source, target);
            default -> false;
        };
    }

    private boolean isVerticalMove(int keyboardKey, GameSquare source, GameSquare target) {
        if (!source.isInSameCol(target))  {
            return false;
        }

        val from = source.coord.row;
        val to = target.coord.row;

        return switch (keyboardKey) {
            case KeyEvent.VK_W -> isUp(from, to);
            case KeyEvent.VK_S -> isDown(from, to);
            default -> false;
        };
    }

    private boolean isHorizontalMove(int keyboardKey, GameSquare source, GameSquare target) {
        if (!source.isInSameRow(target))  {
            return false;
        }

        val from = source.coord.col;
        val to = target.coord.col;

        return switch (keyboardKey) {
            case KeyEvent.VK_A -> isOnLeft(from, to);
            case KeyEvent.VK_D -> isOnRight(from, to);
            default -> false;
        };
    }

    private boolean isUp(int from, int to) {
        return isFromSmaller(from, to);
    }

    private boolean isOnRight(int from, int to) {
        return isFromSmaller(from, to);
    }

    private boolean isFromSmaller(int from, int to) {
        if (from < 7) {
            return to > from;
        } else if (from == 7) {
            return to == 0;
        } else {
            return false;
        }
    }

    private boolean isDown(int from, int to) {
        return isFromGreater(from, to);
    }

    private boolean isOnLeft(int from, int to) {
        return isFromGreater(from, to);
    }

    private boolean isFromGreater(int from, int to) {
        if (from > 0) {
            return to < from;
        } else if (from == 0) {
            return to == 7;
        } else {
            return false;
        }
    }
}