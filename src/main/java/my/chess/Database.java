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
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import my.chess.pieces.Bishop;
import my.chess.pieces.Piece;
import my.chess.pieces.King;
import my.chess.pieces.Knight;
import my.chess.pieces.Pawn;
import my.chess.pieces.Queen;
import my.chess.pieces.Rook;

/**
 *
 * @author Piotr Lach
 */
public class Database {

    public static enum QueryType {
        OTHER, 
        SELECT_CHESS_FIELDS,
        SELECT_MAX_GAME_ID,
        SELECT_GAME_COLOR,
        SELECT_GAMES,
        SELECT_POSITIONS
    };
    public static ArrayList<Integer> games;
    public static ArrayList<String> dates;
    public static ArrayList<String> names;
    public static int gameID = 1;


    private static void select(QueryType queryType, ResultSet resultSet) throws Exception {
        while (resultSet.next()) {
            switch (queryType) {
                case SELECT_POSITIONS -> {
                    int colorIntValue = resultSet.getInt("color");
                    int position = resultSet.getInt("position");
                    Color color = parseIntToColor(colorIntValue);
                    Board.setStartingPoints(color, position);
                }
                case SELECT_GAMES -> {
                    games.add(resultSet.getInt("gameID"));
                    dates.add(resultSet.getString("date"));
                    names.add(resultSet.getString("name"));
                }
                case SELECT_CHESS_FIELDS -> {
                    int row = resultSet.getInt("x");
                    int col = resultSet.getInt("y");
                    String pieceID = resultSet.getString("piece");
                    setLoadedPiece(pieceID, row, col);                    
                }
                case SELECT_MAX_GAME_ID ->
                    gameID = resultSet.getInt("MAX(gameID)");                    
                case SELECT_GAME_COLOR -> {
                    int intColorValue = resultSet.getInt("currentColor");
                    Color color = parseIntToColor(intColorValue);
                    Board.setCurrentColor(color);                    
                }
            }
        }
    }

    public static void sqlConnection(String myQuery, QueryType queryType) {

        games = new ArrayList();
        dates = new ArrayList();
        names = new ArrayList();
        ResultSet resultSet;
        Connection connection = null;
        try {
            String dbName = "jdbc:sqlite:db" + File.separator + "chess.db";
            connection = DriverManager.getConnection(dbName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            switch (queryType) {
                case OTHER -> statement.executeUpdate(myQuery);                    
                default -> {
                    resultSet = statement.executeQuery(myQuery);
                    select(queryType, resultSet);
                }
            }
        } catch (Exception exception) {
            System.err.println(exception);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                System.err.println(exception);
            }
        }        
    }

    private static Color parseIntToColor(int integer) {
        return switch (integer) {
            default -> {
                String message = "Color value can only be 0 or 1";
                throw new IllegalArgumentException(message);
            }
            case 0 -> Color.BLACK;
            case 1 -> Color.WHITE;
        };
    }

    private static void setLoadedPiece(String pieceID, int row, int col) throws IOException {
        if (pieceID != null) {
            int pieceIntValue = Integer.parseInt(pieceID);
            Piece piece = parseIntToPiece(pieceIntValue);
            Board.setPiece(row, col, piece);
        }
    }

    private static Piece parseIntToPiece(int num) throws IllegalArgumentException {        
        return switch (num) {
            default -> {
                String message = "Piece ID value has to be between 0 and 11";
                throw new IllegalArgumentException(message);
            }
            case 0 -> new Pawn(Color.BLACK, Piece.PieceName.Pawn1);
            case 1 -> new Pawn(Color.BLACK, Piece.PieceName.Pawn6);
            case 2 -> new Rook(Color.BLACK);
            case 3 -> new Bishop(Color.BLACK);
            case 4 -> new Knight(Color.BLACK);
            case 5 -> new Queen(Color.BLACK);
            case 6 -> new King(Color.BLACK);
            case 7 -> new Pawn(Color.WHITE, Piece.PieceName.Pawn1);
            case 8 -> new Pawn(Color.WHITE, Piece.PieceName.Pawn6);
            case 9 -> new Rook(Color.WHITE);
            case 10 -> new Bishop(Color.WHITE);
            case 11 -> new Knight(Color.WHITE);
            case 12 -> new Queen(Color.WHITE);
            case 13 -> new King(Color.WHITE);
        };
    }

}
