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
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.val;
import my.chess.Database.QueryType;
import my.chess.pieces.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

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
        
        val selectGames = "SELECT gameID, name, date FROM games;";
        
        radioButtons = Database.sqlQuery(selectGames, QueryType.SELECT_GAMES, radioButtons);
        for (var radioButton : radioButtons) {
                                    
            buttonGroup.add(radioButton);
            add(radioButton);
            
        }
    }

    public void loadSavedGame() throws Exception {        
        val id = getSelectedGameId();
        
        Board.clearBoard();
        getGameColorFromDB(id);
        
        var selectSquares = "SELECT x, y, piece FROM chessFields WHERE game = %d;";
        
        selectSquares = String.format(selectSquares, id);
        
        ArrayList<ImmutablePair<Coord, Piece>> pairs = new ArrayList<>();
        
        pairs = Database.sqlQuery(selectSquares, QueryType.SELECT_SQUARES, pairs);  
        
        for (var pair : pairs) {
            Board.setPiece(pair.left, pair.right);
        }        
        
    }

    private void getGameColorFromDB(Integer gameID) {
        
        var selectCurrentColor = "SELECT currentColor FROM games WHERE gameID = %d;";
        selectCurrentColor = String.format(selectCurrentColor, gameID);
        
        ArrayList<Color> colors = new ArrayList<>();
        
        Database.sqlQuery(selectCurrentColor, QueryType.SELECT_GAME_COLOR, colors);
        
    }

    public void saveNewGame() {
        var localDateTime = LocalDateTime.now();
        var dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        var saveDate = localDateTime.format(dateTimeFormatter);
        var saveName = JOptionPane.showInputDialog(this, "Wpisz nazwę save'a:");
        
        if (saveName == null) {
            var message = "Nie wpisano nazwy save'a!";
            JOptionPane.showMessageDialog(this, message);
            return;
        }
        
        StringBuilder insertFields = new StringBuilder();
        StringBuilder insertNewGame = new StringBuilder();

        int colorValue = parseColorValue(Board.getCurrentColor());
        insertNewGame.append(String.format(insertGame, colorValue, saveDate, saveName));
        getGameIDfromDB();

        for (int index = 0; index < 64; index++) {
            
            var coord = new Coord(index);
            
            var square = Board.getSquare(coord);
            var piece = square.getPiece();
            
            var pieceIntValue = piece == null ? null : pieceIntValue(piece);
                                    
            insertFields.append(String.format(insertChessFields, coord.row, coord.col, pieceIntValue, maxId));                                         
        }
        insertNewGame.append(insertFields);
        
        Database.sqlQuery(insertNewGame.toString(), QueryType.OTHER, null);
        
        var radioButton = new RadioButton(maxId, saveDate, saveName);
        
        radioButtons.add(radioButton);
        buttonGroup.add(radioButton);
        
        add(radioButton);
        updateUI();        
    }

    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;";
        ArrayList<Integer> number = new ArrayList<>();
        number = Database.sqlQuery(selectMaxGameID, QueryType.SELECT_MAX_GAME_ID, number);
        maxId = number.get(0);
        maxId++;
    }

    public void deleteDatabaseRecord() throws Exception {        
        int gameId = getSelectedGameId();
        String deleteQuery = "DELETE FROM chessFields WHERE game =" + gameId + ";\n";
        deleteQuery += "DELETE FROM games WHERE gameID =" + gameId + ";";        
        Database.sqlQuery(deleteQuery, QueryType.OTHER, null);
        
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

    public void updateDatabaseRecord() throws Exception {        
        
        int gameID = getSelectedGameId();               
        var stringBuilder = new StringBuilder();
        var color = Board.getCurrentColor();
        
        stringBuilder.append(String.format(updateColor, parseColorValue(color), gameID));
        for (int index = 0; index < 64; index++) {            
            
            var coord = new Coord(index);
            var square = Board.getSquare(coord);
            var piece = square.getPiece();

            var pieceValue = piece != null ? pieceIntValue(piece) : "null";

            stringBuilder.append(String.format(updatePieceValue, pieceValue, coord.row, coord.col, gameID));                            
        }
        Database.sqlQuery(stringBuilder.toString(), QueryType.OTHER, null);        
    }

    private int getSelectedGameId() throws Exception {
        for (var radioButton : radioButtons) {
            if (radioButton.isSelected()) {
                return radioButton.gameID;
            }
        }
        throw new Exception("Nie wybrano zapisu gry do usunięcia!");
    }

    private int parseColorValue(Color color) throws IllegalArgumentException {
               
        if (color == Color.BLACK) {
            return 0;
        } else if (color == Color.WHITE) {
            return 1;
        } else {
            String message = "There can only be black and white colors";
            throw new IllegalArgumentException(message);
        }

    }

    private Integer pieceIntValue(Piece piece) throws IllegalArgumentException {
       
        var color = piece.color;       
        int colorIntValue = color == Color.BLACK ? 0 : 7;
        
        return switch (piece.getName()) {
            default -> throw new IllegalArgumentException("No such figure!");
            case Pawn1 -> 0 + colorIntValue;
            case Pawn6 -> 1 + colorIntValue;
            case Rook -> 2 + colorIntValue;
            case Bishop -> 3 + colorIntValue;
            case Knight -> 4 + colorIntValue;
            case Queen -> 5 + colorIntValue;
            case King -> 6 + colorIntValue;
        };
    }
    
    private int maxId = 0;
    private ArrayList<RadioButton> radioButtons;
    private ButtonGroup buttonGroup;
    private final String updateColor = "UPDATE games SET currentColor = %d WHERE gameID = %d;",
            updatePieceValue = "UPDATE chessFields SET piece = %s WHERE x = %d AND y = %d AND game = %d;",
            insertChessFields = "INSERT INTO chessFields(x, y, piece,game) VALUES (%d, %d, %s, %d);",
            insertGame = "INSERT INTO games(currentColor,date,name) VALUES(%d, '%s', '%s');";

}
