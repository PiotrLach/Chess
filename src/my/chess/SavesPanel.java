/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.chess.Database.QueryType;
import static my.chess.ChessBoard.setChessMatrixField;
import my.chess.pieces.ChessPiece;
//import my.chess.Database.*;
/**
 *
 * @author bruce
 */
public class SavesPanel extends JPanel {
//    private JRadioButton myRadio;    
    public SavesPanel() {
        setLayout(new GridLayout(0, 1, 5, 10));
        initUI();
    }  
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
//        clearUI();
//        initUI();
//        updateUI();
    }
    private void clearBoard() {                
        ChessBoard.setCurrentColor(Color.WHITE);        
        for (int i=0; i<8; i++)             
            for (int j=0; j<8; j++)
                setChessMatrixField(i, j, null);                
    }
    public void loadSavedGame() {
        Integer i = getSelected();
        clearBoard();
        getGameColorFromDB(i);
        String selectChessFields = "SELECT x, y, piece FROM chessFields WHERE game="+i+";";
        Database.sqlConnection(selectChessFields, QueryType.SELECT_CHESS_FIELDS); 
    }
    private void getGameColorFromDB(Integer i) {
        String selectMaxGameID = "SELECT currentColor FROM games WHERE gameID = "+i+";"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_COLOR);
    }
    public void saveNewGame() {        
        StringBuilder selectPieceID;
        StringBuilder insertFields = new StringBuilder("");
        StringBuilder insertNewGame = new StringBuilder("INSERT INTO games VALUES((SELECT MAX(gameID) FROM games)+1,");
                            insertNewGame.append(parseColorValue(ChessBoard.getCurrentColor()));
                            insertNewGame.append(");");        
        getGameIDfromDB();
        Database.gameID++;
        for (int x=0; x<8; x++) {             
            for (int y=0; y<8; y++) {
                if (ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece() != null) {
                    selectPieceID = new StringBuilder("(SELECT pieceID FROM chessPieces WHERE pieceName = '");
                    selectPieceID.append(ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getChessPieceName());
                    selectPieceID.append("' AND pieceColor = ");
                    selectPieceID.append(parseColorValue(ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getFigureColor()));
                    selectPieceID.append(")");
                    insertFields.append("INSERT INTO chessFields VALUES (");
                    insertFields.append("(SELECT MAX(chessFieldID) FROM chessFields)+1,");
                    insertFields.append(x);
                    insertFields.append(",");
                    insertFields.append(y);
                    insertFields.append(",");
                    insertFields.append(selectPieceID);
                    insertFields.append(",");
                    insertFields.append(Database.gameID);
                    insertFields.append(");\n");
                    
                }
//                else {
//                    insertFields.append( "INSERT INTO chessFields (x, y, game) VALUES (");
//                    insertFields.append( x);
//                    insertFields.append( ",");
//                    insertFields.append( y);
//                    insertFields.append( ",");
//                    insertFields.append( Database.gameID);
//                    insertFields.append( ");\n");
//                }
            }
        }
        insertNewGame.append(insertFields);
        System.out.println(insertFields.toString());
        Database.sqlConnection(insertNewGame.toString(), QueryType.OTHER);
        JRadioButton jb = new JRadioButton(String.valueOf(Database.gameID));
        radioButtons.add(jb);            
        buttonGroup.add(jb);
        add(jb);
        updateUI();
//        System.out.println(insertFields);
//        System.out.println(ctr);
    }
    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_MAX_GAME_ID);
    }
    public void deleteDatabaseRecord(){
        Integer gameID = getSelected();
//        String s = "DELETE FROM chessFields WHERE game ="+gameID+";\n";
//        s += "DELETE FROM games WHERE gameID ="+gameID+";";
//        Database.sqlConnection(s, QueryType.OTHER);
        Comparator<JRadioButton> c = (JRadioButton b1, JRadioButton b2) -> b1.getText().compareTo(b2.getText()); 
        JRadioButton jb = new JRadioButton(gameID.toString());
        Collections.sort(radioButtons, c);
//        System.out.println(Collections.binarySearch(radioButtons, jb, c));
        remove(Collections.binarySearch(radioButtons, jb, c));
        buttonGroup.remove(jb);
        radioButtons.remove(jb);
        updateUI();
    }
    public void updateDatabaseRecord(){ 
        Integer gameID = getSelected();
//        String s = "";
        StringBuilder sb = new StringBuilder("");
        sb.append("UPDATE games SET currentColor =");
        sb.append(parseColorValue(ChessBoard.getCurrentColor()));
        sb.append(" WHERE gameID=");
        sb.append(gameID);
        sb.append(";");
        for (int i=0; i<8; i++) {             
            for (int j=0; j<8; j++) {
                if (ChessBoard.getChessMatrixField(i, j).getCurrentChessPiece() != null) {
                    sb.append( "UPDATE chessFields ");
                    sb.append("SET piece=");
                    sb.append(pieceIntValue(ChessBoard.getChessMatrixField(i, j).getCurrentChessPiece()).toString());
                    sb.append(" WHERE x=");
                    sb.append(i);
                    sb.append(" AND y=");
                    sb.append(j);
                    sb.append(" AND game =");
                    sb.append(gameID);
                    sb.append(";");
                }
                else {
                    sb.append("UPDATE chessFields SET piece=null WHERE x=");
                    sb.append(i);
                    sb.append(" AND y=");
                    sb.append(j);
                    sb.append(" AND game =");
                    sb.append(gameID);
                    sb.append(";");
                }
            }
        }
        Database.sqlConnection(sb.toString(), QueryType.OTHER);
    }
    private Integer getSelected(){
        Integer gameid = 0;
//        for (JRadioButton rb : radioButtons) {            
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isSelected()) {
                gameid = Integer.parseInt(radioButtons.get(i).getText());
//                System.out.println(i);
                break;
            }
        }
        return gameid;
    }     
    private int parseColorValue(Color c) throws IllegalArgumentException {
        if (c == Color.BLACK) 
            return 0;        
        else if (c == Color.WHITE) 
            return 1;
        else
            throw new IllegalArgumentException("There can only be black and white colors");
            
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
    private ArrayList<JRadioButton> radioButtons;
    private ButtonGroup buttonGroup;
}
