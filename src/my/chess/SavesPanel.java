/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author bruce
 */
public class SavesPanel extends JPanel {
    private JRadioButton myRadio;
    private ButtonGroup buttonGroup;
    public void initUI() {
        setLayout(new GridLayout(0, 1, 5, 10));
        buttonGroup = new ButtonGroup();        
        for (int i = 0; i<100; i++){
            myRadio = new JRadioButton("text" + i);
            buttonGroup.add(myRadio);
            add(myRadio);
        }        
    }
    public SavesPanel() {
        initUI();
    }

}
