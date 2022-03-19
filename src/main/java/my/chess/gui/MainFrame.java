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

import my.chess.gui.board.boards.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu gameMenu = new JMenu();
    private final JMenu aboutMenu = new JMenu();
    private final GameBoard board = new GameBoard();
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

        gameMenu.add(buildNewGameOption());
        gameMenu.add(buildLoadGameOption());
        gameMenu.add(buildSaveGameOption());

        menuBar.add(gameMenu);

        aboutMenu.setText(getString(Text.aboutMenu));
        aboutMenu.add(buildLicenseOption());

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

    private JMenuItem buildNewGameOption() {
        JMenuItem newGameOption = new JMenuItem();
        newGameOption.setText(getString(Text.newGameOption));
        newGameOption.addActionListener(actionPerformer::newGame);
        return newGameOption;
    }

    private JMenuItem buildLoadGameOption() {
        JMenuItem loadGameOption = new JMenuItem();
        loadGameOption.setText(getString(Text.loadGameOption));
        loadGameOption.addActionListener(actionPerformer::loadGame);
        return loadGameOption;
    }

    private JMenuItem buildSaveGameOption() {
        JMenuItem saveGameOption = new JMenuItem();
        saveGameOption.setText(getString(Text.saveGameOption));
        saveGameOption.addActionListener(actionPerformer::saveGame);
        return saveGameOption;
    }

    private JMenuItem buildLicenseOption() {
        JMenuItem licenseOption = new JMenuItem();
        licenseOption.setText(getString(Text.licenseOption));
        licenseOption.addActionListener(actionPerformer::displayLicense);
        return licenseOption;
    }

    private String getString(Text text) {
        return bundle.getString(text.key);
    }
}
