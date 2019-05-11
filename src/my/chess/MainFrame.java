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

        myTabbedPane = new javax.swing.JTabbedPane();
        mainMenuPanel = new javax.swing.JPanel();
        gameStartButton = new javax.swing.JButton();
        gameManageSavesButton = new javax.swing.JButton();
        gameExitButton = new javax.swing.JButton();
        mainChessBoard = new my.chess.ChessBoard();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        savesPanel1 = new my.chess.SavesPanel();
        jPanel1 = new javax.swing.JPanel();
        manageSavesLoad = new javax.swing.JButton();
        manageSavesOverwrite = new javax.swing.JButton();
        manageSavesDelete = new javax.swing.JButton();
        manageSavesNewSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 640));
        setResizable(false);

        gameStartButton.setText("Start a new game");
        gameStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameStartButtonActionPerformed(evt);
            }
        });

        gameManageSavesButton.setText("Manage saves");
        gameManageSavesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameManageSavesButtonActionPerformed(evt);
            }
        });

        gameExitButton.setText("Exit without saving");
        gameExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameExitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainMenuPanelLayout = new javax.swing.GroupLayout(mainMenuPanel);
        mainMenuPanel.setLayout(mainMenuPanelLayout);
        mainMenuPanelLayout.setHorizontalGroup(
            mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuPanelLayout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addGroup(mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gameManageSavesButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(gameStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gameExitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)))
                .addContainerGap(341, Short.MAX_VALUE))
        );
        mainMenuPanelLayout.setVerticalGroup(
            mainMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenuPanelLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(gameStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameManageSavesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gameExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );

        myTabbedPane.addTab("Main menu", mainMenuPanel);

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
            .addGap(0, 990, Short.MAX_VALUE)
        );
        mainChessBoardLayout.setVerticalGroup(
            mainChessBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 717, Short.MAX_VALUE)
        );

        myTabbedPane.addTab("Game", mainChessBoard);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(savesPanel1);

        jPanel2.add(jScrollPane1);

        jPanel1.setLayout(new java.awt.GridLayout(2, 2, 20, 20));

        manageSavesLoad.setText("Load");
        manageSavesLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSavesLoadActionPerformed(evt);
            }
        });
        jPanel1.add(manageSavesLoad);

        manageSavesOverwrite.setText("Overwrite selected");
        manageSavesOverwrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSavesOverwriteActionPerformed(evt);
            }
        });
        jPanel1.add(manageSavesOverwrite);

        manageSavesDelete.setText("Delete");
        manageSavesDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSavesDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(manageSavesDelete);

        manageSavesNewSave.setText("New save");
        manageSavesNewSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSavesNewSaveActionPerformed(evt);
            }
        });
        jPanel1.add(manageSavesNewSave);

        jPanel2.add(jPanel1);

        myTabbedPane.addTab("Saves", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myTabbedPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myTabbedPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mainChessBoardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainChessBoardMouseClicked
        mainChessBoard.selectAndMove(evt);
    }//GEN-LAST:event_mainChessBoardMouseClicked

    private void gameStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameStartButtonActionPerformed
        mainChessBoard.setNewGame();
        myTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_gameStartButtonActionPerformed

    private void gameExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameExitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_gameExitButtonActionPerformed

    private void gameManageSavesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameManageSavesButtonActionPerformed
        myTabbedPane.setSelectedIndex(2);       
    }//GEN-LAST:event_gameManageSavesButtonActionPerformed

    private void manageSavesLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSavesLoadActionPerformed
        savesPanel1.loadSavedGame();
        myTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_manageSavesLoadActionPerformed

    private void manageSavesDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSavesDeleteActionPerformed
        savesPanel1.deleteDatabaseRecord();
    }//GEN-LAST:event_manageSavesDeleteActionPerformed

    private void manageSavesOverwriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSavesOverwriteActionPerformed
        savesPanel1.updateDatabaseRecord();
    }//GEN-LAST:event_manageSavesOverwriteActionPerformed

    private void manageSavesNewSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSavesNewSaveActionPerformed
        savesPanel1.saveNewGame();
        savesPanel1.restartUI();
        myTabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_manageSavesNewSaveActionPerformed

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
    private javax.swing.JButton gameManageSavesButton;
    private javax.swing.JButton gameStartButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private my.chess.ChessBoard mainChessBoard;
    private javax.swing.JPanel mainMenuPanel;
    private javax.swing.JButton manageSavesDelete;
    private javax.swing.JButton manageSavesLoad;
    private javax.swing.JButton manageSavesNewSave;
    private javax.swing.JButton manageSavesOverwrite;
    private javax.swing.JTabbedPane myTabbedPane;
    private my.chess.SavesPanel savesPanel1;
    // End of variables declaration//GEN-END:variables
}
