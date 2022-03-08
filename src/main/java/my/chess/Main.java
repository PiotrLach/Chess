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

import my.chess.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piotr Lach
 */
public class Main {

    public static void main(String[] args) {
        setNimbusLookAndFeel();

        EventQueue.invokeLater(() -> {
            var mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private static void setNimbusLookAndFeel() {

        var className = Main.class.getName();
        var lookAndFeels = UIManager.getInstalledLookAndFeels();

        try {
            for (var lookAndFeel : lookAndFeels) {
                var lookAndFeelName = lookAndFeel.getName();
                if ("Nimbus".equals(lookAndFeelName)) {
                    UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    return;
                }
            }
        } catch (Exception exception) {
            var logger = Logger.getLogger(className);
            var message = exception.getMessage();
            logger.log(Level.SEVERE, message, exception);
        }
    }
}
