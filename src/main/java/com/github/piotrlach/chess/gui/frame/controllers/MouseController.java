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
import com.github.piotrlach.chess.gui.frame.SquareSelector;
import lombok.val;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.List;

public class MouseController implements Serializable {
    private final SquareSelector squareSelector;
    private final List<GameSquare> squares;
    private final GameBoard board;

    public MouseController(SquareSelector squareSelector, List<GameSquare> squares, GameBoard board) {
        this.squareSelector = squareSelector;
        this.squares = squares;
        this.board = board;
    }

    /**
     * Finds the square clicked on with the LMB and sets it as either
     * source or target, depending on the piece it contains.
     */
    public void chooseOrMove(final MouseEvent mouseEvent) {
        val point = mouseEvent.getPoint();
        squares.stream()
                .filter(square -> square.contains(point))
                .findAny()
                .ifPresent(this::chooseOrMove);
    }

    private void chooseOrMove(final GameSquare selected) {
        if (squareSelector.isOfValidType(selected, GameSquare.Type.SOURCE)) {
            squareSelector.unselect(GameSquare.Type.SOURCE);
            squareSelector.setSelected(selected, GameSquare.Type.SOURCE);
            board.repaint();
            return;
        }

        if (!squareSelector.isOfValidType(selected, GameSquare.Type.TARGET)) {
            return;
        }

        val source = squareSelector.getSelected(GameSquare.Type.SOURCE);

        if (source.movePieceTo(selected)) {
            squareSelector.unselect(GameSquare.Type.SOURCE);
            squareSelector.unselect(GameSquare.Type.TARGET);
            board.getKeyController()
                    .setSelectTarget(false);
        }
        board.repaint();
    }
}