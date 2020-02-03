/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.chess.Database.QueryType;
import static my.chess.ChessBoard.setChessMatrixField;
import my.chess.pieces.ChessPiece;
import my.chess.pieces.King;
//import my.chess.Database.*;
/**
 *
 * @author Piotr Lach
 */
public class SavesPanel extends JPanel {
//    private JRadioButton myRadio;    
    public SavesPanel() {
        setLayout(new GridLayout(0, 1, 0, 0));
        initUI();
    }  
    private void initUI() {                
        radioButtons =  new ArrayList();
        buttonGroup = new ButtonGroup();
        String selectGames = "SELECT gameID,name,date FROM games;";
        Database.sqlConnection(selectGames, QueryType.SELECT_GAMES);
        for (Integer i = 0; i<Database.games.size(); i++) {           
            radioButtons.add(new RadioButton(Database.games.get(i), Database.dates.get(i),Database.names.get(i)));            
            buttonGroup.add(radioButtons.get(i));            
            add(radioButtons.get(i));
        }        
    }
    public void loadSavedGame() {
        try {
            Integer i = getSelectedGameId();
            ChessBoard.clearBoard();
            getGameColorFromDB(i);
            String selectChessFields = "SELECT x, y, piece FROM chessFields WHERE game="+i+";";
            String selectStartingPositions = "select color,position from startingpositions where gameid = "+i+";";
            Database.sqlConnection(selectChessFields, QueryType.SELECT_CHESS_FIELDS); 
            Database.sqlConnection(selectStartingPositions, QueryType.SELECT_POSITIONS); 
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    private void getGameColorFromDB(Integer i) {
        String selectMaxGameID = "SELECT currentColor FROM games WHERE gameID = "+i+";"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_COLOR);
    }
    public void saveNewGame() {     
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = myDateObj.format(myFormatObj);
        String name = JOptionPane.showInputDialog(this, "Wpisz nazwę save'a:");
        if (name != null) {                   
            StringBuilder selectPieceID;
            StringBuilder insertFields = new StringBuilder("");
            StringBuilder insertNewGame = new StringBuilder("INSERT INTO games(currentColor,date,name) VALUES(");//(SELECT MAX(gameID) FROM games)+1,");
                                insertNewGame.append(parseColorValue(ChessBoard.getCurrentColor()));
                                insertNewGame.append(",'");
                                insertNewGame.append(date);
                                insertNewGame.append("','");
                                insertNewGame.append(name);
                                insertNewGame.append("');");        
            getGameIDfromDB();
            Database.gameID++;
            HashMap<Color, Integer> points = ChessBoard.getstartingPoints();
            String insertNewStartingPositions = "insert into startingPositions(gameID,position,color) VALUES" +
                                                "("+Database.gameID+","+points.get(Color.BLACK)+","+ 0 +");";
            insertNewStartingPositions += "insert into startingPositions(gameID,position,color) VALUES" +
                                                "("+Database.gameID+","+points.get(Color.WHITE)+","+ 1 +");";
            insertNewGame.append(insertNewStartingPositions);
            for (int x=0; x<8; x++) {             
                for (int y=0; y<8; y++) {
                    if (ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece() != null) {
                        selectPieceID = new StringBuilder("(SELECT pieceID FROM chessPieces WHERE pieceName = '");
                        selectPieceID.append(ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getPieceName());                    
                        selectPieceID.append("' AND pieceColor = ");
                        selectPieceID.append(parseColorValue(ChessBoard.getChessMatrixField(x, y).getCurrentChessPiece().getFigureColor()));
                        selectPieceID.append(")");
                        insertFields.append("INSERT INTO chessFields(x,y,piece,game) VALUES (");
    //                    insertFields.append("(SELECT MAX(chessFieldID) FROM chessFields)+1,");
                        insertFields.append(x);
                        insertFields.append(",");
                        insertFields.append(y);
                        insertFields.append(",");
                        insertFields.append(selectPieceID);
                        insertFields.append(",");
                        insertFields.append(Database.gameID);
                        insertFields.append(");\n");

                    }
                    else {
                        insertFields.append( "INSERT INTO chessFields (x, y, game) VALUES (");
                        insertFields.append( x);
                        insertFields.append( ",");
                        insertFields.append( y);
                        insertFields.append( ",");
                        insertFields.append( Database.gameID);
                        insertFields.append( ");\n");
                    }
                }
            }
            insertNewGame.append(insertFields);
    //        System.out.println(insertFields.toString());
            Database.sqlConnection(insertNewGame.toString(), QueryType.OTHER);
            RadioButton rb = new RadioButton(Database.gameID,date,name);
            radioButtons.add(rb);            
            buttonGroup.add(rb);
            add(rb);
            updateUI();
        }
//        System.out.println(insertFields);
//        System.out.println(ctr);
    }
    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;"; 
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_MAX_GAME_ID);
    }
    public void deleteDatabaseRecord(){
        try {
            int gameId = getSelectedGameId();
            String deleteQuery = "DELETE FROM chessFields WHERE game ="+gameId+";\n";
            deleteQuery += "DELETE FROM games WHERE gameID ="+gameId+";";
            Database.sqlConnection(deleteQuery, QueryType.OTHER);
//            Comparator<JRadioButton> c = (JRadioButton b1, JRadioButton b2) -> b1.getText().compareTo(b2.getText());             
//            JRadioButton jb = new JRadioButton(gameID.toString());
//            Collections.sort(radioButtons, c);
    //        System.out.println(Collections.binarySearch(radioButtons, jb, c));
//            int idx = Collections.binarySearch(radioButtons, jb, c);
//            int size = 
            for (int i = 0; i < radioButtons.size(); i++) {
                if (radioButtons.get(i).isSelected()) {
                    remove(radioButtons.get(i));        
                    buttonGroup.remove(radioButtons.get(i));
                    radioButtons.remove(i);
                    break;
                }
            }                        
            updateUI();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    public void updateDatabaseRecord(){
        try {
            int gameID = getSelectedGameId();
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
                        sb.append(pieceIntValue(ChessBoard.getChessMatrixField(i, j).getCurrentChessPiece()));
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
        catch (Exception e) {
            System.out.println(e);
        }
    }
    private int getSelectedGameId() throws Exception {            
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isSelected()) {                
                return radioButtons.get(i).getGameID();
            }
        }
        throw new Exception("Nie wybrano zapisu gry do usunięcia!");
    }     
    private int parseColorValue(Color c) throws IllegalArgumentException {
        if (c == Color.BLACK) 
            return 0;        
        else if (c == Color.WHITE) 
            return 1;
        else
            throw new IllegalArgumentException("There can only be black and white colors");
            
    }        
    private int pieceIntValue(ChessPiece cp) throws IllegalArgumentException{
        int i;
        if (cp.getFigureColor() == Color.BLACK)
            i = 0;
        else
            i = 7;            
        switch (cp.getPieceName()) {
            default:
                throw new IllegalArgumentException("No such figure!");
            case Pawn1:
                return 0+i;
            case Pawn6:
                return 1+i;      
            case Rook:
                return 2+i;            
            case Bishop:
                return 3+i;            
            case Knight:
                return 4+i;            
            case Queen:
                return 5+i;            
            case King:
                return 6+i;
        }
    }
    private class RadioButton extends JRadioButton { 

        public RadioButton(int gameID, String date, String name) {
            this.gameID = gameID;
            this.date = date;
            this.name = name;
            setText(name + ", " + date);
        }

        public int getGameID() {
            return gameID;
        }
        
        private int gameID;
        private String date;
        private String name;
    }
    private ArrayList<RadioButton> radioButtons;
    private ButtonGroup buttonGroup;
}
