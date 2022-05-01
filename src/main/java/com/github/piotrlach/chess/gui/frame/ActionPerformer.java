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

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
class ActionPerformer {

    private final ResourceBundle bundle = ResourceBundle.getBundle("com/github/piotrlach/chess/Bundle");
    private final MainFrame mainFrame;
    private final GameBoard board;

    void newGame(ActionEvent evt) {
        board.setDefaultGame();
    }

    void loadGame(ActionEvent evt) {
        var fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(mainFrame);

        if (!(result == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        var file = fileChooser.getSelectedFile();
        var fileName = file.getAbsolutePath();

        board.loadGame(fileName);
    }

    void saveGame(ActionEvent evt) {

        var fileChooser = new JFileChooser();
        int saveOption = fileChooser.showSaveDialog(mainFrame);

        if (!(saveOption == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        var file = fileChooser.getSelectedFile();
        int confirmationOption = 0;

        if (file.exists()) {
            var message = bundle.getString("MainFrame.fileExists.text");
            var formattedMessage = String.format(message, file.getName());
            confirmationOption = JOptionPane.showConfirmDialog(fileChooser, formattedMessage);
        }

        if (confirmationOption != JOptionPane.OK_OPTION) {
            return;
        }

        var fileName = file.getAbsolutePath();
        board.saveGame(fileName);
    }

    void displayLicense(ActionEvent evt) {

        var fileName = bundle.getString("MainFrame.licenseFile");

        var classLoader = mainFrame.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);
        var stringBuilder = new StringBuilder();

        try {
            if (inputStream == null) {
                throw new NullPointerException("Input stream is null!");
            }
            var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            int char1 = 0;
            while ((char1 = inputStreamReader.read()) > -1) {
                stringBuilder.append((char) char1);
            }
        } catch (IOException | NullPointerException exception) {
            var errorMessage = bundle.getString("MainFrame.errorReadingLicense");
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            return;
        }

        JOptionPane.showMessageDialog(mainFrame, stringBuilder.toString());
    }
}