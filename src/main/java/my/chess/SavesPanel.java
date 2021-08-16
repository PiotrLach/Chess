/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import my.chess.Database.QueryType;
import my.chess.pieces.Piece;

/**
 *
 * @author Piotr Lach
 */
public class SavesPanel extends JPanel {

    public SavesPanel() {
        setLayout(new GridLayout(0, 1, 0, 0));
        initUI();
    }

    private void initUI() {
        radioButtons = new ArrayList();
        buttonGroup = new ButtonGroup();
        final String selectGames = "SELECT gameID, name, date FROM games;";
        Database.sqlConnection(selectGames, QueryType.SELECT_GAMES);
        for (Integer i = 0; i < Database.games.size(); i++) {
            radioButtons.add(new RadioButton(Database.games.get(i), Database.dates.get(i), Database.names.get(i)));
            buttonGroup.add(radioButtons.get(i));
            add(radioButtons.get(i));
        }
    }

    public void loadSavedGame() {
        try {
            Integer i = getSelectedGameId();
            Board.clearBoard();
            getGameColorFromDB(i);
            final String selectChessFields = "SELECT x, y, piece FROM chessFields WHERE game = %d;";
            final String selectStartingPositions = "SELECT color, position FROM startingpositions WHERE gameid = %d;";
            Database.sqlConnection(String.format(selectChessFields, i), QueryType.SELECT_CHESS_FIELDS);
            Database.sqlConnection(String.format(selectStartingPositions, i), QueryType.SELECT_POSITIONS);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getGameColorFromDB(Integer i) {
        String selectMaxGameID = "SELECT currentColor FROM games WHERE gameID = " + i + ";";
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_COLOR);
    }

    public void saveNewGame() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = myDateObj.format(myFormatObj);
        String name = JOptionPane.showInputDialog(this, "Wpisz nazwę save'a:");
        if (name != null) {
            StringBuilder insertFields = new StringBuilder();
            StringBuilder insertNewGame = new StringBuilder();

            int cv = parseColorValue(Board.getCurrentColor());
            insertNewGame.append(String.format(insertGame, cv, date, name));
            getGameIDfromDB();
            Database.gameID++;
            HashMap<Color, Integer> points = Board.getstartingPoints();

            String insertNewStartingPositions = "insert into startingPositions(gameID,position,color) VALUES"
                    + "(" + Database.gameID + "," + points.get(Color.BLACK) + "," + 0 + ");";
            insertNewStartingPositions += "insert into startingPositions(gameID,position,color) VALUES"
                    + "(" + Database.gameID + "," + points.get(Color.WHITE) + "," + 1 + ");";

            insertNewGame.append(insertNewStartingPositions);

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Square cf = Board.getSquare(x, y);
                    Piece cp = cf.getPiece();
                    if (cp != null) {
                        String selectPieceID = String.format(selectPiece, cp.getPieceName(), parseColorValue(cp.getFigureColor()));
                        insertFields.append(String.format(insertChessFields, x, y, selectPieceID, Database.gameID));

                    } else {
                        insertFields.append(String.format(insertChessFields, x, y, "null", Database.gameID));
                    }
                }
            }
            insertNewGame.append(insertFields);
            Database.sqlConnection(insertNewGame.toString(), QueryType.OTHER);
            RadioButton rb = new RadioButton(Database.gameID, date, name);
            radioButtons.add(rb);
            buttonGroup.add(rb);
            add(rb);
            updateUI();
        }
    }

    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;";
        Database.sqlConnection(selectMaxGameID, QueryType.SELECT_MAX_GAME_ID);
    }

    public void deleteDatabaseRecord() {
        try {
            int gameId = getSelectedGameId();
            String deleteQuery = "DELETE FROM chessFields WHERE game =" + gameId + ";\n";
            deleteQuery += "DELETE FROM games WHERE gameID =" + gameId + ";";
            Database.sqlConnection(deleteQuery, QueryType.OTHER);
            for (int i = 0; i < radioButtons.size(); i++) {
                if (radioButtons.get(i).isSelected()) {
                    remove(radioButtons.get(i));
                    buttonGroup.remove(radioButtons.get(i));
                    radioButtons.remove(i);
                    break;
                }
            }
            updateUI();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateDatabaseRecord() {
        try {
            int gameID = getSelectedGameId();
            StringBuilder sb = new StringBuilder();
            Color c = Board.getCurrentColor();
            sb.append(String.format(updateColor, parseColorValue(c), gameID));
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Square square = Board.getSquare(i, j);
                    Piece piece = square.getPiece();
                    if (piece != null) {
                        sb.append(String.format(updatePieceValue, pieceIntValue(piece), i, j, gameID));
                    } else {
                        sb.append(String.format(updatePieceValue, "null", i, j, gameID));
                    }
                }
            }
            Database.sqlConnection(sb.toString(), QueryType.OTHER);
        } catch (Exception e) {
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
        if (c == Color.BLACK) {
            return 0;
        } else if (c == Color.WHITE) {
            return 1;
        } else {
            throw new IllegalArgumentException("There can only be black and white colors");
        }

    }

    private int pieceIntValue(Piece cp) throws IllegalArgumentException {
        int i;
        if (cp.getFigureColor() == Color.BLACK) {
            i = 0;
        } else {
            i = 7;
        }
        switch (cp.getPieceName()) {
            default:
                throw new IllegalArgumentException("No such figure!");
            case Pawn1:
                return 0 + i;
            case Pawn6:
                return 1 + i;
            case Rook:
                return 2 + i;
            case Bishop:
                return 3 + i;
            case Knight:
                return 4 + i;
            case Queen:
                return 5 + i;
            case King:
                return 6 + i;
        }
    }

    private class RadioButton extends JRadioButton {

        public RadioButton(int gameID, String date, String name) {
            this.gameID = gameID;
            setText(name + ", " + date);
        }

        public int getGameID() {
            return gameID;
        }

        private final int gameID;
    }
    private ArrayList<RadioButton> radioButtons;
    private ButtonGroup buttonGroup;
    private final String selectPiece = "(SELECT pieceID FROM chessPieces WHERE pieceName = '%s' AND pieceColor = %d)",
            updateColor = "UPDATE games SET currentColor = %d WHERE gameID = %d;",
            updatePieceValue = "UPDATE chessFields SET piece = %s WHERE x = %d AND y = %d AND game = %d;",
            insertChessFields = "INSERT INTO chessFields(x, y, piece,game) VALUES (%d, %d, %s, %d);",
            insertGame = "INSERT INTO games(currentColor,date,name) VALUES(%d, '%s', '%s');";

}
