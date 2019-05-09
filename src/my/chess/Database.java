/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import my.chess.ChessBoard.QueryType;
/**
 *
 * @author bruce
 */
public class Database {
    public static ArrayList<Integer> games;
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
                /*
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
                        currentColor = parseIntValue(rs.getInt("currentColor"));                        
                    }                    
                    break;*/
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
}
