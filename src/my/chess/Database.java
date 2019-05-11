/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import my.chess.ChessBoard.QueryType;
//import my.chess.ChessBoard.C;
import my.chess.pieces.Bishop;
import my.chess.pieces.ChessPiece;
import my.chess.pieces.King;
import my.chess.pieces.Knight;
import my.chess.pieces.Pawn;
import my.chess.pieces.Queen;
import my.chess.pieces.Rook;
/**
 *
 * @author bruce
 */
public class Database {
    public static ArrayList<Integer> games;
    public static ArrayList<ChessField> fields;
    public static int gameID = 1;    
    public static void createNewDatabase() {
 
        String url = "jdbc:sqlite:db/chess.db";
 
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
        Connection c = null;        
        ResultSet rs = null;
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:db/chess.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            switch (q) {
                case OTHER:
                    statement.executeUpdate(myQuery);
                    break;
                case SELECT_GAMES:
                    rs = statement.executeQuery(myQuery);
                    while(rs.next())
                    {
                        games.add(rs.getInt("gameID"));
                    }
                    break;                
                case SELECT_CHESS_FIELDS:
                    rs = statement.executeQuery(myQuery);
                    while(rs.next())
                    {                        
                        setLoadedGamePieces(rs.getString("piece"),rs.getInt("x"),rs.getInt("y"));
                    }
                    break;
                case SELECT_MAX_GAME_ID:
                    rs = statement.executeQuery(myQuery);
                    while(rs.next())
                    {                        
                        gameID = rs.getInt("MAX(gameID)");                        
                    }                    
                    break;
                case SELECT_GAME_COLOR:
//                    System.out.println(myQuery);
                    rs = statement.executeQuery(myQuery);
//                    ResultSetMetaData rsmd = rs.getMetaData();
//                    String name = rsmd.getColumnName(1);
//                    System.out.println(name);
                    while(rs.next())
                    {                                                
                        ChessBoard.currentColor = parseIntValue(rs.getInt("currentColor"));
                        System.out.println(rs.getInt("currentColor"));
                    }                    
                    break;
            }            
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                connection.close();
            }
            catch(SQLException e)
            {
              // connection close failed.
                System.err.println(e);
            }
        }
    }
    private static Color parseIntValue(int i) {
        if (i == 0) 
            return Color.BLACK;
        else 
            return Color.WHITE;
    }
    private static void setLoadedGamePieces(String pieceID, int x, int y) {        
        if(pieceID != null) {
//            System.out.println(x+" "+ y +" " +Integer.parseInt(pieceID));
            ChessBoard.chessMatrix[x][y].setCurrentChessPiece(choosePiece(Integer.parseInt(pieceID)));
            ChessBoard.chessMatrix[x][y].setHighlighted(false);
        }
    }
    private static ChessPiece choosePiece(int num) {
        switch (num) {
            default:
                return null;
            case 0:
                return new Pawn(Color.BLACK);
            case 1:
                return new Rook(Color.BLACK);
            case 2:
                return new Bishop(Color.BLACK);
            case 3:
                return new Knight(Color.BLACK);
            case 4:
                return new Queen(Color.BLACK);
            case 5:
                return new King(Color.BLACK);
            case 6:
                return new Pawn(Color.WHITE);
            case 7:
                return new Rook(Color.WHITE);
            case 8:
                return new Bishop(Color.WHITE);
            case 9:
                return new Knight(Color.WHITE);
            case 10:
                return new Queen(Color.WHITE);
            case 11:
                return new King(Color.WHITE);                
        }
    }
}
