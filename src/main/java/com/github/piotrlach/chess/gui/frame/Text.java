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

import lombok.AllArgsConstructor;

@AllArgsConstructor
enum Text {
    gameMenu("MainFrame.gameMenu.text"),
    newGameOption("MainFrame.newGameOption.text"),
    loadGameOption("MainFrame.loadGameOption.text"),
    saveGameOption("MainFrame.saveGameOption.text"),
    aboutMenu("MainFrame.aboutMenu.text"),
    licenseOption("MainFrame.licenseOption.text");
    public final String key;
}
