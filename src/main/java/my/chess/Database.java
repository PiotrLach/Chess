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
import my.chess.pieces.ChessPiece;
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

    public static void sqlConnection(String myQuery, QueryType q) {

        games = new ArrayList();
        dates = new ArrayList();
        names = new ArrayList();
        Connection c = null;
        ResultSet rs;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:db" + File.separator + "chess.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            switch (q) {
                case OTHER:
                    statement.executeUpdate(myQuery);
                    break;
                default:
                    rs = statement.executeQuery(myQuery);
                    while (rs.next()) {
                        switch (q) {
                            case SELECT_POSITIONS:
                                try {
                                    Color col = rs.getInt("color") == 0 ? Color.BLACK : Color.WHITE;
                                    ChessBoard.setStartingPoints(col, rs.getInt("position"));
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                break;
                            case SELECT_GAMES:
                                games.add(rs.getInt("gameID"));
                                dates.add(rs.getString("date"));
                                names.add(rs.getString("name"));
                                break;
                            case SELECT_CHESS_FIELDS:
                                try {
                                    setLoadedGamePiece(rs.getString("piece"), rs.getInt("x"), rs.getInt("y"));
                                } catch (IOException | SQLException e) {
                                    System.out.println(e);
                                }
                                break;
                            case SELECT_MAX_GAME_ID:
                                gameID = rs.getInt("MAX(gameID)");                                
                                break;
                            case SELECT_GAME_COLOR:
                                ChessBoard.setCurrentColor(parseIntValue(rs.getInt("currentColor")));
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
        switch (i) {
            default:
                throw new IllegalArgumentException("Color value can only be 0 or 1");
            case 0:
                return Color.BLACK;
            case 1:
                return Color.WHITE;

        }
    }

    private static void setLoadedGamePiece(String pieceID, int x, int y) throws IOException {
        if (pieceID != null) {
            ChessBoard.setChessMatrixField(x, y, choosePiece(Integer.parseInt(pieceID)));
        }
    }

    private static ChessPiece choosePiece(int num) throws IllegalArgumentException {
        switch (num) {
            default:
                throw new IllegalArgumentException("Piece ID value has to be between 0 and 11");
            case 0:
                return new Pawn(Color.BLACK,  ChessPiece.PieceName.Pawn1);
            case 1:
                return new Pawn(Color.BLACK,  ChessPiece.PieceName.Pawn6);
            case 2:
                return new Rook(Color.BLACK);
            case 3:
                return new Bishop(Color.BLACK);
            case 4:
                return new Knight(Color.BLACK);
            case 5:
                return new Queen(Color.BLACK);
            case 6:
                return new King(Color.BLACK);
            case 7:
                return new Pawn(Color.WHITE,  ChessPiece.PieceName.Pawn1);
            case 8:
                return new Pawn(Color.WHITE,  ChessPiece.PieceName.Pawn6);
            case 9:
                return new Rook(Color.WHITE);
            case 10:
                return new Bishop(Color.WHITE);
            case 11:
                return new Knight(Color.WHITE);
            case 12:
                return new Queen(Color.WHITE);
            case 13:
                return new King(Color.WHITE);
        }
    }

    private String readSqlFile() {
        String data = "";
        try {
            File f = new File("db" + File.separator + "base.sql");
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.toString());
        }
        return data;
    }
}
