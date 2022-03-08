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
import java.awt.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    private final JMenu aboutMenu = new JMenu();
    private final GameBoard board = new GameBoard();
    private final JMenu gameMenu = new JMenu();
    private final JMenuItem licenseOption = new JMenuItem();
    private final JMenuItem loadGameOption = new JMenuItem();
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenuItem newGameOption = new JMenuItem();
    private final JMenuItem saveGameOption = new JMenuItem();
    private final ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle");
    private final ActionPerformer actionPerformer = new ActionPerformer(this, board);

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));

        setMenuOptions();

	    add(board);

        pack();
    }

    private void setMenuOptions() {
        gameMenu.setText(getString(Text.gameMenu));

        newGameOption.setText(getString(Text.newGameOption));
        newGameOption.addActionListener(actionPerformer::newGame);
        gameMenu.add(newGameOption);

        loadGameOption.setText(getString(Text.loadGameOption));
        loadGameOption.addActionListener(actionPerformer::loadGame);
        gameMenu.add(loadGameOption);

        saveGameOption.setText(getString(Text.saveGameOption));
        saveGameOption.addActionListener(actionPerformer::saveGame);
        gameMenu.add(saveGameOption);

        menuBar.add(gameMenu);

        aboutMenu.setText(getString(Text.aboutMenu));

        licenseOption.setText(getString(Text.licenseOption));
        licenseOption.addActionListener(actionPerformer::displayLicense);
        aboutMenu.add(licenseOption);

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

    private String getString(Text text) {
        return bundle.getString(text.key);
    }
}
