/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
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
        OTHER, SELECT_CHESS_FIELDS, SELECT_MAX_GAME_ID, SELECT_GAME_COLOR, SELECT_GAMES, SELECT_POSITIONS
    };
    public static ArrayList<Integer> games;
    public static ArrayList<String> dates;
    public static ArrayList<String> names;
    public static int gameID = 1;

    public static void createNewDatabase() {

        String url = "jdbc:sqlite:db" + File.separator + "chess.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sqlConnection(String myQuery, QueryType queryType) {

        games = new ArrayList();
        dates = new ArrayList();
        names = new ArrayList();        
        ResultSet resultSet;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:db" + File.separator + "chess.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            switch (queryType) {
                case OTHER:
                    statement.executeUpdate(myQuery);
                    break;
                default:
                    resultSet = statement.executeQuery(myQuery);
                    while (resultSet.next()) {
                        switch (queryType) {
                            case SELECT_POSITIONS:
                                try {
                                    Color color = resultSet.getInt("color") == 0 ? Color.BLACK : Color.WHITE;
                                    Board.setStartingPoints(color, resultSet.getInt("position"));
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            case SELECT_GAMES:
                                games.add(resultSet.getInt("gameID"));
                                dates.add(resultSet.getString("date"));
                                names.add(resultSet.getString("name"));
                                break;
                            case SELECT_CHESS_FIELDS:
                                try {
                                    setLoadedGamePiece(resultSet.getString("piece"),
                                            resultSet.getInt("x"),
                                            resultSet.getInt("y")
                                    );
                                } catch (IOException | SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case SELECT_MAX_GAME_ID:
                                gameID = resultSet.getInt("MAX(gameID)");                                
                                break;
                            case SELECT_GAME_COLOR:
                                Board.setCurrentColor(parseIntValue(resultSet.getInt("currentColor")));
                                break;
                        }
                    }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    private static Color parseIntValue(int i) {
        return switch (i) {
            default -> throw new IllegalArgumentException("Color value can only be 0 or 1");
            case 0 -> Color.BLACK;
            case 1 -> Color.WHITE;
        };
    }

    private static void setLoadedGamePiece(String pieceID, int x, int y) throws IOException {
        if (pieceID != null) {
            Board.setSquare(x, y, choosePiece(Integer.parseInt(pieceID)));
        }
    }

    private static Piece choosePiece(int num) throws IllegalArgumentException {
        return switch (num) {
            default -> throw new IllegalArgumentException("Piece ID value has to be between 0 and 11");
            case 0 -> new Pawn(Color.BLACK,  Piece.PieceName.Pawn1);
            case 1 -> new Pawn(Color.BLACK,  Piece.PieceName.Pawn6);
            case 2 -> new Rook(Color.BLACK);
            case 3 -> new Bishop(Color.BLACK);
            case 4 -> new Knight(Color.BLACK);
            case 5 -> new Queen(Color.BLACK);
            case 6 -> new King(Color.BLACK);
            case 7 -> new Pawn(Color.WHITE,  Piece.PieceName.Pawn1);
            case 8 -> new Pawn(Color.WHITE,  Piece.PieceName.Pawn6);
            case 9 -> new Rook(Color.WHITE);
            case 10 -> new Bishop(Color.WHITE);
            case 11 -> new Knight(Color.WHITE);
            case 12 -> new Queen(Color.WHITE);
            case 13 -> new King(Color.WHITE);
        };
    }

    private String readSqlFile() {
        String data = "";
        try {
            File file = new File("db" + File.separator + "base.sql");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data += scanner.nextLine() + "\n";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.toString());
        }
        return data;
    }
}
