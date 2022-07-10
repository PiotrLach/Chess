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
import com.github.piotrlach.chess.logic.Logic;
import lombok.Setter;
import lombok.val;

import java.awt.event.KeyEvent;
import java.util.*;

public class KeyController {

    private final List<GameSquare> squares;
    private final Logic logic;
    private final GameBoard board;
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
        this.board = board;
        this.logic = board.getLogic();
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();

        if (isSelectionTypeChange(keyCode)) {
            selectTarget = !selectTarget;
        }

        if (isMovementAttempt(keyCode)) {
            val source = board.getSelected(GameSquare.Type.SOURCE);
            val target = board.getSelected(GameSquare.Type.TARGET);
            tryToMovePiece(source, target);
            return;
        }

        Arrays.stream(DIRECTIONS)
            .filter(direction -> direction.getKeyCode() == keyCode)
            .findAny()
            .ifPresent(direction -> {
                if (selectTarget) {
                    select(direction, GameSquare.Type.TARGET);
                } else {
                    select(direction, GameSquare.Type.SOURCE);
                }
            });
    }


    private boolean isSelectionTypeChange(int keyboardKey) {
        return board.isSelected(GameSquare.Type.SOURCE) && keyboardKey == KeyEvent.VK_SPACE;
    }

    private boolean isMovementAttempt(int keyboardKey) {
        return board.isSelected(GameSquare.Type.SOURCE) && board.isSelected(GameSquare.Type.TARGET) && keyboardKey == KeyEvent.VK_SPACE;
    }

    private void tryToMovePiece(GameSquare source, GameSquare target) {
        logic.movePiece(source.coord, target.coord);
        selectTarget = false;
        board.unselect(GameSquare.Type.SOURCE);
        board.unselect(GameSquare.Type.TARGET);
    }

    private void select(Direction key, GameSquare.Type type) {
        if (!board.isSelected(type)) {
            setAny(type);
            return;
        }

        board.unselect(type);

        findClosestInDimension(key, type);
    }

    private void setAny(GameSquare.Type type) {
        squares.stream()
                .filter(square -> board.isValid(square, type))
                .findAny()
                .ifPresent(square -> board.setSelected(square, type));
    }

    private void findClosestInDimension(Direction direction, GameSquare.Type type) {
        squares.stream()
                .filter(square -> board.isValid(square, type))
                .filter(square -> direction.isNextInDimension(board.getSelected(type), square))
                .map(next -> next.coord.index)
                .min(direction.getComparator())
                .ifPresentOrElse(index -> board.setSelected(index, type), () -> setAnyClosestInDirection(direction, type));
    }

    private void setAnyClosestInDirection(Direction direction, GameSquare.Type type) {
        squares.stream()
                .filter(square -> board.isValid(square, type))
                .filter(square -> direction.isNextOutsideDimension(board.getSelected(type), square))
                .map(direction::mapToDimension)
                .min(direction.getComparator())
                .ifPresent(index -> setAnyInDimension(direction, type, index));
    }

    private void setAnyInDimension(Direction direction, GameSquare.Type type, int dimIndex) {
        squares.stream()
                .filter(square -> board.isValid(square, type))
                .filter(square -> direction.mapToDimension(square) == dimIndex)
                .min(Comparator.naturalOrder())
                .ifPresent(square -> board.setSelected(square, type));
    }
}