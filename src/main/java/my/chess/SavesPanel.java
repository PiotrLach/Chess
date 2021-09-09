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

            int colorValue = parseColorValue(Board.getCurrentColor());
            insertNewGame.append(String.format(insertGame, colorValue, date, name));
            getGameIDfromDB();
            Database.gameID++;
            HashMap<Color, Integer> points = Board.getstartingPoints();

            String insertNewStartingPositions = "insert into startingPositions(gameID,position,color) VALUES"
                    + "(" + Database.gameID + "," + points.get(Color.BLACK) + "," + 0 + ");";
            insertNewStartingPositions += "insert into startingPositions(gameID,position,color) VALUES"
                    + "(" + Database.gameID + "," + points.get(Color.WHITE) + "," + 1 + ");";

            insertNewGame.append(insertNewStartingPositions);

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square square = Board.getSquare(row, col);
                    Piece piece = square.getPiece();
                    if (piece != null) {
                        String selectPieceID = String.format(selectPiece, piece.getName(), parseColorValue(piece.color));
                        insertFields.append(String.format(insertChessFields, row, col, selectPieceID, Database.gameID));

                    } else {
                        insertFields.append(String.format(insertChessFields, row, col, "null", Database.gameID));
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
            var stringBuilder = new StringBuilder();
            var color = Board.getCurrentColor();
            stringBuilder.append(String.format(updateColor, parseColorValue(color), gameID));
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square square = Board.getSquare(row, col);
                    Piece piece = square.getPiece();
                    if (piece != null) {
                        stringBuilder.append(String.format(updatePieceValue, pieceIntValue(piece), row, col, gameID));
                    } else {
                        stringBuilder.append(String.format(updatePieceValue, "null", row, col, gameID));
                    }
                }
            }
            Database.sqlConnection(stringBuilder.toString(), QueryType.OTHER);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private int getSelectedGameId() throws Exception {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isSelected()) {
                return radioButtons.get(i).gameID;
            }
        }
        throw new Exception("Nie wybrano zapisu gry do usunięcia!");
    }

    private int parseColorValue(final Color color) throws IllegalArgumentException {
               
        if (color == Color.BLACK) {
            return 0;
        } else if (color == Color.WHITE) {
            return 1;
        } else {
            String message = "There can only be black and white colors";
            throw new IllegalArgumentException(message);
        }

    }

    private int pieceIntValue(Piece piece) throws IllegalArgumentException {
       
        var color = piece.color;       
        int i = color == Color.BLACK ? 0 : 7;
        
        return switch (piece.getName()) {
            default -> throw new IllegalArgumentException("No such figure!");
            case Pawn1 -> 0 + i;
            case Pawn6 -> 1 + i;
            case Rook -> 2 + i;
            case Bishop -> 3 + i;
            case Knight -> 4 + i;
            case Queen -> 5 + i;
            case King -> 6 + i;
        };
    }

    private class RadioButton extends JRadioButton {

        public RadioButton(int gameID, String date, String name) {
            this.gameID = gameID;
            setText(name + ", " + date);
        }

        public final int gameID;
    }
    private ArrayList<RadioButton> radioButtons;
    private ButtonGroup buttonGroup;
    private final String selectPiece = "(SELECT pieceID FROM chessPieces WHERE pieceName = '%s' AND pieceColor = %d)",
            updateColor = "UPDATE games SET currentColor = %d WHERE gameID = %d;",
            updatePieceValue = "UPDATE chessFields SET piece = %s WHERE x = %d AND y = %d AND game = %d;",
            insertChessFields = "INSERT INTO chessFields(x, y, piece,game) VALUES (%d, %d, %s, %d);",
            insertGame = "INSERT INTO games(currentColor,date,name) VALUES(%d, '%s', '%s');";

}
