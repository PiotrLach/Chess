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
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.List;

public class KeyController {

    private final GameBoard board;
    private final List<GameSquare> squares;

    private final List<Integer> keys = List.of(
            KeyEvent.VK_W,
            KeyEvent.VK_A,
            KeyEvent.VK_S,
            KeyEvent.VK_D
    );

    public KeyController(GameBoard board, List<GameSquare> squares) {
        this.board = board;
        this.squares = squares;
    }

    public void select(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();

        if (!keys.contains(key)) {
            return;
        }

        if (board.isSourceSelected()) {
            squares.stream()
                    .filter(board::isValidSource)
                    .findAny()
                    .ifPresent(board::setSelectedSource);
        } else {
            val source = board.getSelectedSource()
                    .orElseThrow(() -> new IllegalStateException("Source is not present!"));
            board.setSelectedSourceEmpty();

            squares.stream()
                    .filter(board::isValidSource)
                    .filter(target -> isNext(key, source, target))
                    .findFirst()
                    .ifPresent(board::setSelectedSource);
        }

        board.repaint();
    }

    private boolean isNext(int key, GameSquare source, GameSquare target) {
        return switch (key) {
            case KeyEvent.VK_W -> isUp(source, target);
            case KeyEvent.VK_A -> isOnLeft(source, target);
            case KeyEvent.VK_S -> isDown(source, target);
            case KeyEvent.VK_D -> isOnRight(source, target);
            default -> false;
        };
    }

    public boolean isUp(GameSquare source, GameSquare target) {
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

    public boolean isDown(GameSquare source, GameSquare target) {
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

    public boolean isOnRight(GameSquare source, GameSquare target) {
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

    public boolean isOnLeft(GameSquare source, GameSquare target) {
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
