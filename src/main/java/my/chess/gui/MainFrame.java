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

import lombok.val;
import my.chess.gui.board.boards.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    private final GameBoard board = new GameBoard();
    private final ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle");
    private final ActionPerformer actionPerformer = new ActionPerformer(this, board);

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));

        setMenuBar();
        add(board);

        pack();
    }

    private void setMenuBar() {
        val menuBar = new JMenuBar();

        menuBar.add(buildAboutMenu());
        menuBar.add(buildGameMenu());

        setJMenuBar(menuBar);
    }

    private JMenu buildAboutMenu() {
        val aboutMenu = new JMenu();
        aboutMenu.setText(getString(Text.aboutMenu));
        aboutMenu.add(buildLicenseOption());
        return aboutMenu;
    }

    private JMenu buildGameMenu() {
        val gameMenu = new JMenu();
        gameMenu.setText(getString(Text.gameMenu));

        gameMenu.add(buildNewGameOption());
        gameMenu.add(buildLoadGameOption());
        gameMenu.add(buildSaveGameOption());
        return gameMenu;
    }

    private JMenuItem buildNewGameOption() {
        return buildOption(Text.newGameOption, actionPerformer::newGame);
    }

    private JMenuItem buildLoadGameOption() {
        return buildOption(Text.loadGameOption, actionPerformer::loadGame);
    }

    private JMenuItem buildSaveGameOption() {
        return buildOption(Text.saveGameOption, actionPerformer::saveGame);
    }

    private JMenuItem buildLicenseOption() {
        return buildOption(Text.licenseOption, actionPerformer::displayLicense);
    }

    private JMenuItem buildOption(final Text text, final ActionListener actionListener) {
        val option = new JMenuItem();
        option.setText(getString(text));
        option.addActionListener(actionListener);
        return option;
    }

    private String getString(final Text text) {
        return bundle.getString(text.key);
    }
}
