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
import lombok.val;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

public class MouseController implements Serializable {
    private final GameBoard gameBoard;
    private final List<GameSquare> squares;

    public MouseController(GameBoard gameBoard, List<GameSquare> squares) {
        this.gameBoard = gameBoard;
        this.squares = squares;
    }

    /**
     * Finds the square clicked on with the LMB and sets it as either
     * source or target, depending on the piece it contains.
     */
    void chooseOrMove(final MouseEvent mouseEvent) {
        val point = mouseEvent.getPoint();
        squares.stream()
                .filter(square -> square.contains(point))
                .findAny()
                .ifPresent(this::chooseOrMove);
    }

    private void chooseOrMove(final GameSquare selected) {
        if (gameBoard.isValidSource(selected)) {
            gameBoard.getSelectedSource()
                    .ifPresent(gameSquare -> gameSquare.setSelectedSource(false));
            selected.setSelectedSource(true);
            gameBoard.setSelectedSource(selected);
            gameBoard.repaint();
            return;
        }

        if (!gameBoard.isValidTarget(selected)) {
            return;
        }

        val source = gameBoard.getSelectedSource()
                .orElseThrow(NoSelectedSourceException::new);

        if (source.movePieceTo(selected)) {
            source.setSelectedSource(false);
            gameBoard.setSelectedSourceEmpty();
            gameBoard.setSelectedTargetEmpty();
            gameBoard.keyController
                    .setSelectTarget(false);
        }
        gameBoard.repaint();
    }
}