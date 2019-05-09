/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.chess.ChessBoard.QueryType;
//import my.chess.Database.*;
/**
 *
 * @author bruce
 */
public class SavesPanel extends JPanel {
//    private JRadioButton myRadio;
    private ArrayList<JRadioButton> radioButtons;
    private ButtonGroup buttonGroup;
    public void initUI() {                
        radioButtons =  new ArrayList();
        buttonGroup = new ButtonGroup();
        String selectGames = "SELECT gameID FROM games;";
        Database.sqlConnection(selectGames, QueryType.SELECT_GAMES);
        for (Integer i = 0; i<Database.games.size(); i++) {
            radioButtons.add(new JRadioButton(Database.games.get(i).toString()));            
            buttonGroup.add(radioButtons.get(i));
            add(radioButtons.get(i));
        }        
    }
    public void clearUI() {
        removeAll();
        radioButtons = null;
        buttonGroup = null;
        Database.games = null;
    }
    public Integer getSelected(){
        Integer i = 0;
        for (JRadioButton rb : radioButtons) {            
            if (rb.isSelected()) {
                i = Integer.parseInt(rb.getText());
//                System.out.println(i);
                break;
            }
        }
        return i;
    }
    public SavesPanel() {
        setLayout(new GridLayout(0, 1, 5, 10));
        initUI();
    }

}
