/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.chess.ChessBoard.QueryType;
import static my.chess.ChessBoard.chessMatrix;
import my.chess.pieces.ChessPiece;
//import my.chess.Database.*;
/**
 *
 * @author bruce
 */
public class SavesPanel extends JPanel {
//    private JRadioButton myRadio;
    private ArrayList<JRadioButton> radioButtons;
    private ButtonGroup buttonGroup;
    private void initUI() {                
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
    private void clearUI() {
        removeAll();
        radioButtons = null;
        buttonGroup = null;
        Database.games = null;
    }
    public void restartUI(){
        clearUI();
        initUI();
        updateUI();
    }
    public void deleteDatabaseRecord(){
        Integer gameID = getSelected();
        String s = "DELETE FROM chessFields WHERE game ="+gameID+";\n";
        s += "DELETE FROM games WHERE gameID ="+gameID+";";
        Database.sqlConnection(s, QueryType.OTHER);
        restartUI();
    }
    public void updateDatabaseRecord(){ 
        Integer gameID = getSelected();
        String s = "";
        for (int i=0; i<8; i++) {             
            for (int j=0; j<8; j++) {
                if (chessMatrix[i][j].getCurrentChessPiece() != null) {
                    s += "UPDATE chessFields "
                        + "SET piece="+pieceIntValue(chessMatrix[i][j].getCurrentChessPiece()).toString()
                        +" WHERE x="+i+" AND y="+j+" AND game ="+gameID+";";
                }
                else {
                    s += "UPDATE chessFields SET piece=null WHERE x="+i+" AND y="+j+" AND game ="+gameID+";";
                }
            }
        }
//        String s = "UPDATE chessFields SET piece="+piece+"WHERE x="+x+"AND y="+y+" AND game ="+gameID+";";
        Database.sqlConnection(s, QueryType.OTHER);
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
    private Integer pieceIntValue(ChessPiece cp) {
        Integer i;
        if (cp.getFigureColor() == Color.BLACK)
            i = 0;
        else
            i = 6;            
        switch (cp.getChessPieceName()) {
            default:
                return null;
            case "P":
                return 0+i;            
            case "W":
                return 1+i;            
            case "G":
                return 2+i;            
            case "S":
                return 3+i;            
            case "H":
                return 4+i;            
            case "K":
                return 5+i;
        }
    }

}
