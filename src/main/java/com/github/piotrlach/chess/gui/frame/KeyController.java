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

import com.github.piotrlach.chess.gui.NoSelectedSourceException;
import com.github.piotrlach.chess.gui.drawable.drawables.GameSquare;
import com.github.piotrlach.chess.logic.Logic;
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.*;

public class KeyController {

    private final GameBoard board;
    private final List<GameSquare> squares;
    private final Logic logic;
    private boolean selectTarget = false;
    private final Map<Integer, Comparator<Integer>> comparators = Map.ofEntries(
            Map.entry(KeyEvent.VK_W, Comparator.naturalOrder()),
            Map.entry(KeyEvent.VK_A, Comparator.reverseOrder()),
            Map.entry(KeyEvent.VK_S, Comparator.reverseOrder()),
            Map.entry(KeyEvent.VK_D, Comparator.naturalOrder())
    );

    public KeyController(GameBoard board, List<GameSquare> squares) {
        this.board = board;
        this.squares = squares;
        this.logic = board.getLogic();
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        int keyboardKey = keyEvent.getKeyCode();

        if (board.isSourceSelected() && keyboardKey == KeyEvent.VK_SPACE) {
            selectTarget = true;
        }

        if (board.isSourceSelected() && board.isTargetSelected() && keyboardKey == KeyEvent.VK_SPACE) {
            val source = board.getSelectedSource()
                    .orElseThrow(IllegalStateException::new);
            val target = board.getSelectedTarget()
                    .orElseThrow(IllegalStateException::new);
            tryToMovePiece(source, target);
            return;
        }

        if (!isValidKey(keyboardKey)) {
            return;
        }

        if (selectTarget) {
            selectTarget(keyboardKey);
        } else {
            selectSource(keyboardKey);
        }
    }

    private void tryToMovePiece(GameSquare source, GameSquare target) {
        if (logic.movePiece(source.coord, target.coord)) {
            selectTarget = false;
            board.setSelectedSourceEmpty();
            board.setSelectedTargetEmpty();
        }
    }

    private boolean isValidKey(int keyboardKey) {
        return comparators.containsKey(keyboardKey);
    }

    private void selectSource(int keyboardKey) {
        if (!board.isSourceSelected()) {
            squares.stream()
                    .filter(board::isValidSource)
                    .findAny()
                    .ifPresent(board::setSelectedSource);
        } else {
            val previous = board.getSelectedSource()
                    .orElseThrow(NoSelectedSourceException::new);
            board.setSelectedSourceEmpty();

            squares.stream()
                    .filter(board::isValidSource)
                    .filter(square -> isNext(keyboardKey, previous, square))
                    .map(next -> next.coord.index)
                    .min(comparators.get(keyboardKey))
                    .ifPresent(board::setSelectedSource);
        }
    }

    private void selectTarget(int keyboardKey) {
        if (!board.isTargetSelected()) {
            squares.stream()
                    .filter(board::isValidTarget)
                    .findAny()
                    .ifPresent(board::setSelectedTarget);
        } else {
            val previous = board.getSelectedTarget()
                    .orElseThrow(NoSelectedSourceException::new);
            board.setSelectedTargetEmpty();

            squares.stream()
                    .filter(board::isValidTarget)
                    .filter(square -> isNext(keyboardKey, previous, square))
                    .map(next -> next.coord.index)
                    .min(comparators.get(keyboardKey))
                    .ifPresent(board::setSelectedTarget);
        }
    }

    private boolean isNext(int keyboardKey, GameSquare source, GameSquare target) {
        return switch (keyboardKey) {
            case KeyEvent.VK_W -> isUp(source, target);
            case KeyEvent.VK_A -> isOnLeft(source, target);
            case KeyEvent.VK_S -> isDown(source, target);
            case KeyEvent.VK_D -> isOnRight(source, target);
            default -> false;
        };
    }

    private boolean isUp(GameSquare source, GameSquare target) {
        if (!source.isInSameCol(target))  {
            return false;
        }

        int from = source.coord.row;
        int to = target.coord.row;

        if (from < 7) {
            return to > from;
        } else if (from == 7) {
            return to == 0;
        } else {
            return false;
        }
    }

    private boolean isDown(GameSquare source, GameSquare target) {
        if (!source.isInSameCol(target))  {
            return false;
        }

        int from = source.coord.row;
        int to = target.coord.row;

        if (from > 0) {
            return to < from;
        } else if (from == 0) {
            return to == 7;
        } else {
            return false;
        }
    }

    private boolean isOnRight(GameSquare source, GameSquare target) {
        if (!source.isInSameRow(target))  {
            return false;
        }

        int from = source.coord.col;
        int to = target.coord.col;

        if (from < 7) {
            return to > from;
        } else if (from == 7) {
            return to == 0;
        } else {
            return false;
        }
    }

    private boolean isOnLeft(GameSquare source, GameSquare target) {
        if (!source.isInSameRow(target))  {
            return false;
        }

        int from = source.coord.col;
        int to = target.coord.col;

        if (from > 0) {
            return to < from;
        } else if (from == 0) {
            return to == 7;
        } else {
            return false;
        }
    }
}
