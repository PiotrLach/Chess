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

package my.chess.gui;

import my.chess.Main;

import javax.swing.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LookAndFeelSetter {

    public void setNimbusLookAndFeel() {
        Arrays.stream(UIManager.getInstalledLookAndFeels())
                .filter(feel -> "Nimbus".equals(feel.getName()))
                .findAny()
                .map(UIManager.LookAndFeelInfo::getClassName)
                .ifPresent(this::setLookAndFeel);
    }

    private void setLookAndFeel(final String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    private void logException(final Exception exception) {
        var className = Main.class.getName();
        var logger = Logger.getLogger(className);
        var message = exception.getMessage();

        logger.log(Level.SEVERE, message, exception);
    }
}