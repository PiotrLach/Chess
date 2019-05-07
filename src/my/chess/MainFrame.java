/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;


/**
 *
 * @author bruce
 */
public class MainFrame extends javax.swing.JFrame {

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        mainMenuPanel = new javax.swing.JPanel();
        gameStartButton = new javax.swing.JButton();
        gameLoadButton = new javax.swing.JButton();
        gameExitButton = new javax.swing.JButton();
        gameSaveButton = new javax.swing.JButton();
        mainChessBoard = new my.chess.ChessBoard();
        savesPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 640));
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);

        gameStartButton.setText("Start a new game");
        gameStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameStartButtonActionPerformed(evt);
            }
        });

        gameLoadButton.setText("Load previous game");
        gameLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameLoadButtonActionPerformed(evt);
            }
        });

        gameExitButton.setText("Exit without saving");
        gameExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameExitButtonActionPerformed(evt);
            }
        });

        gameSaveButton.setText("Save current game");
        gameSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainMenuPanelLayout = new javax.swing.GroupLayout(mainMenuPanel);
        mainMenuPanel.setLayout(mainMenuPanelLayout);
        mainMenuPanelLayout.setHorizontalGroup(
            mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuPanelLayout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addGroup(mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gameSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(gameStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameLoadButton, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(gameExitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(406, Short.MAX_VALUE))
        );
        mainMenuPanelLayout.setVerticalGroup(
            mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuPanelLayout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(gameStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameLoadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(311, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Main menu", mainMenuPanel);

        mainChessBoard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mainChessBoard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainChessBoardMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainChessBoardLayout = new javax.swing.GroupLayout(mainChessBoard);
        mainChessBoard.setLayout(mainChessBoardLayout);
        mainChessBoardLayout.setHorizontalGroup(
            mainChessBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
        );
        mainChessBoardLayout.setVerticalGroup(
            mainChessBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Game", mainChessBoard);

        javax.swing.GroupLayout savesPanelLayout = new javax.swing.GroupLayout(savesPanel);
        savesPanel.setLayout(savesPanelLayout);
        savesPanelLayout.setHorizontalGroup(
            savesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1057, Short.MAX_VALUE)
        );
        savesPanelLayout.setVerticalGroup(
            savesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 805, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Saves Panel", savesPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mainChessBoardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainChessBoardMouseClicked
        mainChessBoard.selectAndMove(evt);
    }//GEN-LAST:event_mainChessBoardMouseClicked

    private void gameStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameStartButtonActionPerformed
        mainChessBoard.setPieces(ChessBoard.GameState.NEW);
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_gameStartButtonActionPerformed

    private void gameExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameExitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_gameExitButtonActionPerformed

    private void gameLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameLoadButtonActionPerformed
        mainChessBoard.loadGame();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_gameLoadButtonActionPerformed

    private void gameSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameSaveButtonActionPerformed
        mainChessBoard.saveGame();
    }//GEN-LAST:event_gameSaveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gameExitButton;
    private javax.swing.JButton gameLoadButton;
    private javax.swing.JButton gameSaveButton;
    private javax.swing.JButton gameStartButton;
    private javax.swing.JTabbedPane jTabbedPane1;
    private my.chess.ChessBoard mainChessBoard;
    private javax.swing.JPanel mainMenuPanel;
    private javax.swing.JPanel savesPanel;
    // End of variables declaration//GEN-END:variables
}
