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
import javax.imageio.ImageIO;
import my.chess.pieces.Bishop;
import my.chess.pieces.ChessPiece;
import my.chess.pieces.Images;
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
    public static enum QueryType { OTHER, SELECT_CHESS_FIELDS, SELECT_MAX_GAME_ID, SELECT_GAME_COLOR, SELECT_GAMES};
    public static ArrayList<Integer> games;    
    public static int gameID = 1;    
    public static void createNewDatabase() {
 
        String url = "jdbc:sqlite:db"+File.separator+"chess.db";
 
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
    private String readSqlFile() {
        String data = "";
        try {
            File f = new File("db"+File.separator+"base.sql");
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
    //            System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.toString());            
        } 
        return data;
    }
    public static void sqlConnection(String myQuery, QueryType q) {
        
        games = new ArrayList();    
        Connection c = null;        
        ResultSet rs;
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:db"+File.separator+"chess.db");
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
                        try {
                            setLoadedGamePieces(rs.getString("piece"),rs.getInt("x"),rs.getInt("y"));
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
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
                        ChessBoard.setCurrentColor(parseIntValue(rs.getInt("currentColor")));
//                        System.out.println(rs.getInt("currentColor"));
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
        switch(i) {
            default: 
                throw new IllegalArgumentException("Color value can only be 0 or 1");
            case 0:
                return Color.BLACK;
            case 1:
                return Color.WHITE;
                
        }            
    }
    private static void setLoadedGamePieces(String pieceID, int x, int y) throws IOException {        
        if(pieceID != null) {
//            System.out.println(x+" "+ y +" " +Integer.parseInt(pieceID));
            ChessBoard.setChessMatrixField(x, y, choosePiece(Integer.parseInt(pieceID)));            
        }
        ChessBoard.getChessMatrixField(x, y).setHighlighted(false);
    }
    private static ChessPiece choosePiece(int num) throws IllegalArgumentException {
        switch (num) {
            default:
                throw new IllegalArgumentException("Piece ID value has to be between 0 and 11");
            case 0:
                return new Pawn(Color.BLACK,Images.getPAWN(Color.BLACK),1);
            case 1:
                return new Rook(Color.BLACK,Images.getROOK(Color.BLACK));
            case 2:
                return new Bishop(Color.BLACK,Images.getBISHOP(Color.BLACK));
            case 3:
                return new Knight(Color.BLACK,Images.getKNIGHT(Color.BLACK));
            case 4:
                return new Queen(Color.BLACK,Images.getQUEEN(Color.BLACK));
            case 5:
                return new King(Color.BLACK,Images.getKING(Color.BLACK));
            case 6:
                return new Pawn(Color.WHITE,Images.getPAWN(Color.WHITE),6);
            case 7:
                return new Rook(Color.WHITE,Images.getROOK(Color.WHITE));
            case 8:
                return new Bishop(Color.WHITE,Images.getBISHOP(Color.WHITE));
            case 9:
                return new Knight(Color.WHITE,Images.getKING(Color.WHITE));
            case 10:
                return new Queen(Color.WHITE,Images.getQUEEN(Color.WHITE));
            case 11:
                return new King(Color.WHITE,Images.getKING(Color.WHITE));                
        }
    }
}
