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

package com.github.piotrlach.chess.gui;

import com.github.piotrlach.chess.Main;
import lombok.val;

import javax.swing.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LookAndFeelSetter {

    private static final String NIMBUS_FEEL = "Nimbus";

    public static void setNimbusLookAndFeel() {
        Arrays.stream(UIManager.getInstalledLookAndFeels())
                .filter(feel -> NIMBUS_FEEL.equals(feel.getName()))
                .findAny()
                .map(UIManager.LookAndFeelInfo::getClassName)
                .ifPresent(LookAndFeelSetter::setLookAndFeel);
    }

    private static void setLookAndFeel(final String className) {
        try {
            UIManager.setLookAndFeel(className);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    private static void logException(final Exception exception) {
        val className = Main.class.getName();
        val logger = Logger.getLogger(className);
        val message = exception.getMessage();

        logger.log(Level.SEVERE, message, exception);
    }
}