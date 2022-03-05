/*
 * Java chess game implementation
 * Copyright (C) 2021 Piotr Lach
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
*/
package my.chess;

import java.awt.Dimension;
import java.util.ResourceBundle;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.UIManager;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    public static void main(String[] args) {
        setNimbusLookAndFeel();

        EventQueue.invokeLater(() -> {
            var mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }


    private static void setNimbusLookAndFeel() {

        var className = MainFrame.class.getName();
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

    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));

        setMenuOptions();

	    add(board);

        pack();
    }

    private void setMenuOptions() {
        gameMenu.setText(bundle.getString("MainFrame.gameMenu.text"));

        newGameOption.setText(bundle.getString("MainFrame.newGameOption.text"));
        newGameOption.addActionListener(actionPerformer::newGame);
        gameMenu.add(newGameOption);

        loadGameOption.setText(bundle.getString("MainFrame.loadGameOption.text"));
        loadGameOption.addActionListener(actionPerformer::loadGame);
        gameMenu.add(loadGameOption);

        saveGameOption.setText(bundle.getString("MainFrame.saveGameOption.text"));
        saveGameOption.addActionListener(actionPerformer::saveGame);
        gameMenu.add(saveGameOption);

        menuBar.add(gameMenu);

        aboutMenu.setText(bundle.getString("MainFrame.aboutMenu.text"));

        licenseOption.setText(bundle.getString("MainFrame.licenseOption.text"));
        licenseOption.addActionListener(actionPerformer::displayLicense);
        aboutMenu.add(licenseOption);

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

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

}
