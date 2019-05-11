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
        clearUI();
        initUI();
        updateUI();
    }
    private void clearBoard() {                
        ChessBoard.setCurrentColor(Color.WHITE);;        
        for (int i=0; i<8; i++)             
            for (int j=0; j<8; j++)
                setChessMatrixField(i, j, null);                
    }
    public void loadSavedGame() {
        Integer i = getSelected();
        clearBoard();
        getGameColorFromDB(i);
        String selectChessFields = "SELECT x, y, piece FROM chessFields WHERE game="+i.toString()+";";
        Database.sqlConnection(selectChessFields, QueryType.SELECT_CHESS_FIELDS); 
    }
    private void getGameColorFromDB(Integer i) {
        String selectMaxGameID = "SELECT currentColor FROM games WHERE gameID = "+i.toString()+";"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_COLOR);
    }
    public void saveNewGame() {        
        String selectPieceID; 
        String insertFields = "";
        String insertNewGame = "INSERT INTO games VALUES((SELECT MAX(gameID) FROM games)+1,"+parseColorValue(ChessBoard.getCurrentColor())+");";        
        getGameIDfromDB();
        Database.gameID++;
        for (int x=0; x<8; x++) {             
            for (int y=0; y<8; y++) {
                if (ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece() != null) {
                    selectPieceID = "(SELECT pieceID FROM chessPieces WHERE pieceName = '" 
                                    + ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getChessPieceName() 
                                    + "' AND pieceColor = " 
                                    + parseColorValue(ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getFigureColor()) 
                                    + ")";
                    insertFields += "INSERT INTO chessFields VALUES ("
                                    + "(SELECT MAX(chessFieldID) FROM chessFields)+1,"
                                    + x + ","
                                    + y + ","
                                    + selectPieceID + ","
                                    + Database.gameID 
                                    + ");\n";                    
                }
                else {
                    insertFields += "INSERT INTO chessFields (x, y, game) VALUES ("
                                    + x + ","
                                    + y + ","
                                    + Database.gameID 
                                    + ");\n";
                }
            }
        }
        insertNewGame += insertFields;
        Database.sqlConnection(insertNewGame, QueryType.OTHER);
//        System.out.println(insertFields);
//        System.out.println(ctr);
    }
    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_MAX_GAME_ID);
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
        s += "UPDATE games SET currentColor ="+parseColorValue(ChessBoard.getCurrentColor())+" WHERE gameID="+gameID.toString()+";";
        for (int i=0; i<8; i++) {             
            for (int j=0; j<8; j++) {
                if (ChessBoard.getChessMatrixField(i, j).getCurrentChessPiece() != null) {
                    s += "UPDATE chessFields "
                        + "SET piece="+pieceIntValue(ChessBoard.getChessMatrixField(i, j).getCurrentChessPiece()).toString()
                        +" WHERE x="+i+" AND y="+j+" AND game ="+gameID+";";
                }
                else {
                    s += "UPDATE chessFields SET piece=null WHERE x="+i+" AND y="+j+" AND game ="+gameID+";";
                }
            }
        }
        Database.sqlConnection(s, QueryType.OTHER);
    }
    private Integer getSelected(){
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
    private int parseColorValue(Color c) {
        if (c == Color.BLACK) 
            return 0;        
        else 
            return 1;
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
