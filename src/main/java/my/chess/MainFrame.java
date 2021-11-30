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
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.UIManager;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;

/**
 *
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    public static void main(String args[]) {
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));

        setMenuOptions();

	add(board);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setMenuOptions() {
        gameMenu.setText(bundle.getString("MainFrame.gameMenu.text"));

        newGameOption.setText(bundle.getString("MainFrame.newGameOption.text"));
        newGameOption.addActionListener(this::newGameOptionActionPerformed);
        gameMenu.add(newGameOption);

        loadGameOption.setText(bundle.getString("MainFrame.loadGameOption.text"));
        loadGameOption.addActionListener(this::loadGameOptionActionPerformed);
        gameMenu.add(loadGameOption);

        saveGameOption.setText(bundle.getString("MainFrame.saveGameOption.text"));
        saveGameOption.addActionListener(this::saveGameOptionActionPerformed);
        gameMenu.add(saveGameOption);

        menuBar.add(gameMenu);

        aboutMenu.setText(bundle.getString("MainFrame.aboutMenu.text"));

        licenseOption.setText(bundle.getString("MainFrame.licenseOption.text"));
        licenseOption.addActionListener(this::licenseOptionActionPerformed);
        aboutMenu.add(licenseOption);

        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

    private void newGameOptionActionPerformed(ActionEvent evt) {//GEN-FIRST:event_newGameOptionActionPerformed
        board.setGame(board::defaultLayout);
    }//GEN-LAST:event_newGameOptionActionPerformed

    private void loadGameOptionActionPerformed(ActionEvent evt) {//GEN-FIRST:event_loadGameOptionActionPerformed
        var fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (!(result == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        var file = fileChooser.getSelectedFile();
        var fileName = file.getAbsolutePath();

        var save = new Save(board);
        save.loadGame(fileName);
    }//GEN-LAST:event_loadGameOptionActionPerformed

    private void saveGameOptionActionPerformed(ActionEvent evt) {//GEN-FIRST:event_saveGameOptionActionPerformed

        var fileChooser = new JFileChooser();
        int saveOption = fileChooser.showSaveDialog(this);

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
        var save = new Save(board);
        save.saveGame(fileName);
    }//GEN-LAST:event_saveGameOptionActionPerformed

    private void licenseOptionActionPerformed(ActionEvent evt) {//GEN-FIRST:event_licenseOptionActionPerformed

        var fileName = bundle.getString("MainFrame.licenseFile");

        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);
        var stringBuilder = new StringBuilder();

        try {
            var inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            int char1 = 0;
            while ((char1 = inputStreamReader.read()) > -1) {
                stringBuilder.append((char) char1);
            }
        } catch (IOException exception) {
            var errorMessage = bundle.getString("MainFrame.errorReadingLicense");
            JOptionPane.showMessageDialog(this, errorMessage);
            return;
        }

        JOptionPane.showMessageDialog(this, stringBuilder.toString());
    }//GEN-LAST:event_licenseOptionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final JMenu aboutMenu = new JMenu();
    private final Board board = new Board();
    private final JMenu gameMenu = new JMenu();
    private final JMenuItem licenseOption = new JMenuItem();
    private final JMenuItem loadGameOption = new JMenuItem();
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenuItem newGameOption = new JMenuItem();
    private final JMenuItem saveGameOption = new JMenuItem();
    // End of variables declaration//GEN-END:variables
    private final ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle");

}
