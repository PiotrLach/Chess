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
package my.chess;

import my.chess.gui.LookAndFeelSetter;
import my.chess.gui.frame.MainFrame;

import java.awt.*;

/**
 *
 * @author Piotr Lach
 */
public class Main {

    private static final LookAndFeelSetter lookAndFeelSetter = new LookAndFeelSetter();

    public static void main(String[] args) {

        lookAndFeelSetter.setNimbusLookAndFeel();

        EventQueue.invokeLater(() -> {
            var mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
