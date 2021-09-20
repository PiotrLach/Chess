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
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.GroupLayout;
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
import javax.swing.JFrame;

/**
 *
 * @author Piotr Lach
 */
public class MainFrame extends JFrame {

    /**
     * Creates new form NewJFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        board = new Board();
        menuBar = new JMenuBar();
        gameMenu = new JMenu();
        newGameOption = new JMenuItem();
        loadGameOption = new JMenuItem();
        saveGameOption = new JMenuItem();
        aboutMenu = new JMenu();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 640));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        board.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                boardMouseClicked(evt);
            }
        });

        GroupLayout boardLayout = new GroupLayout(board);
        board.setLayout(boardLayout);
        boardLayout.setHorizontalGroup(boardLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 1036, Short.MAX_VALUE)
        );
        boardLayout.setVerticalGroup(boardLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 734, Short.MAX_VALUE)
        );

        ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle"); // NOI18N
        gameMenu.setText(bundle.getString("MainFrame.gameMenu.text")); // NOI18N

        newGameOption.setText(bundle.getString("MainFrame.newGameOption.text")); // NOI18N
        newGameOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                newGameOptionActionPerformed(evt);
            }
        });
        gameMenu.add(newGameOption);

        loadGameOption.setText(bundle.getString("MainFrame.loadGameOption.text")); // NOI18N
        loadGameOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadGameOptionActionPerformed(evt);
            }
        });
        gameMenu.add(loadGameOption);

        saveGameOption.setText(bundle.getString("MainFrame.saveGameOption.text")); // NOI18N
        saveGameOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveGameOptionActionPerformed(evt);
            }
        });
        gameMenu.add(saveGameOption);

        menuBar.add(gameMenu);

        aboutMenu.setText(bundle.getString("MainFrame.aboutMenu.text")); // NOI18N
        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(board, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(board, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void formComponentResized(ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        board.resizeBoard();
    }//GEN-LAST:event_formComponentResized

    private void newGameOptionActionPerformed(ActionEvent evt) {//GEN-FIRST:event_newGameOptionActionPerformed
        board.setNewGame();
    }//GEN-LAST:event_newGameOptionActionPerformed

    private void boardMouseClicked(MouseEvent evt) {//GEN-FIRST:event_boardMouseClicked
        board.chooseOrMove(evt);
    }//GEN-LAST:event_boardMouseClicked

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
        int result = fileChooser.showSaveDialog(this), chosen = 0;

        if (!(result == JFileChooser.APPROVE_OPTION)) {
            return;
        }
        
        var file = fileChooser.getSelectedFile();
        
        if (file.exists()) {
            var bundle = ResourceBundle.getBundle("my/chess/Bundle");
            var message = bundle.getString("MainFrame.fileExists.text");
            var formattedMessage = String.format(message, file.getName());
            chosen = JOptionPane.showConfirmDialog(fileChooser, formattedMessage);            
        }
        
        if (chosen != JOptionPane.OK_OPTION) {
            return;
        }
        
        var fileName = file.getAbsolutePath();
       
        var save = new Save(board);
        save.saveGame(fileName);
    }//GEN-LAST:event_saveGameOptionActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        var className = MainFrame.class.getName();
        try {
            for (var lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(lookAndFeelInfo.getName())) {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                    break;
                }
            }
        } catch (Exception exception) {
            Logger.getLogger(className).log(Level.SEVERE, null, exception);
        } 
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */                
        EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JMenu aboutMenu;
    private Board board;
    private JMenu gameMenu;
    private JMenuItem loadGameOption;
    private JMenuBar menuBar;
    private JMenuItem newGameOption;
    private JMenuItem saveGameOption;
    // End of variables declaration//GEN-END:variables
}
