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

import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    private final GameBoard board = new GameBoard();
    private final ResourceBundle bundle = ResourceBundle.getBundle("com/github/piotrlach/chess/Bundle");
    private final ActionPerformer actionPerformer = new ActionPerformer(this, board);

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));

        setMenuBar();
        add(board);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                board.getKeyController()
                        .handleKeyPress(keyEvent);
                board.repaint();
            }
        });

        pack();
    }

    private void setMenuBar() {
        val menuBar = new JMenuBar();

        menuBar.add(buildGameMenu());
        menuBar.add(buildAboutMenu());

        setJMenuBar(menuBar);
    }

    private JMenu buildGameMenu() {
        val gameMenu = new JMenu();
        gameMenu.setText(getString(Text.gameMenu));

        gameMenu.add(buildOption(Text.newGameOption, actionPerformer::newGame));
        gameMenu.add(buildOption(Text.loadGameOption, actionPerformer::loadGame));
        gameMenu.add(buildOption(Text.saveGameOption, actionPerformer::saveGame));
        return gameMenu;
    }

    private JMenu buildAboutMenu() {
        val aboutMenu = new JMenu();
        aboutMenu.setText(getString(Text.aboutMenu));
        aboutMenu.add(buildOption(Text.licenseOption, actionPerformer::displayLicense));
        return aboutMenu;
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
