/*
 * Copyright (C) 2021 Piotr Lach
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
package my.chess.gui;

import lombok.AllArgsConstructor;

/**
 *
 * @author Piotr Lach
 */

@AllArgsConstructor
public enum Message {
    isMate("Board.isMate.text"),
    pieceAlreadyChosen("Board.pieceAlreadyChosen.text"),
    getOutOfCheck("Board.pieceGetOutOfCheck.text"),
    noSelectedPiece("Board.noSelectedPiece.text"),
    wrongMove("Board.wrongMove.text"),
    pathBlocked("Board.pathBlocked.text"),
    selfMadeCheck("Board.selfMadeCheck.text"),
    loadError("Save.loadError"),
    saveError("Save.saveError"),
    wrongFormat("Save.wrongFormat");
    public final String key;
}
