/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import my.chess.pieces.*;
import java.sql.*;

/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel{
    
    public ChessBoard() {                      
        createFields();
//        createNewDatabase("test.db");
//        setPieces();
    }
    public void createNewDatabase() {
 
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
    private void sqlConnection(String myQuery, QueryType q) {
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
                case SELECT_CHESS_FIELDS:
                    rs = statement.executeQuery(myQuery);
                    while(rs.next())
                    {
                        setLoadedGamePieces(rs.getString("piece"),rs.getInt("x"),rs.getInt("y"));
//                        if(rs.getString("piece") != null)
//                            chessMatrix[rs.getInt("x")][rs.getInt("y")].setCurrentChessPiece(choosePiece(rs.getInt("piece")));
//                        read the result set                        
//                        System.out.println("x = " + rs.getInt("x"));
//                        System.out.println("y = " + rs.getInt("y"));
//                        System.out.println("piece = " + rs.getString("piece"));
//                        System.out.println("game = " + rs.getInt("game"));
                    }
                    break;
                case SELECT_GAME_ID:
//                    System.out.println(myQuery);
                    rs = statement.executeQuery(myQuery);
//                    ResultSetMetaData rsmd = rs.getMetaData();
//                    String name = rsmd.getColumnName(1);
//                    System.out.println(name);
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
    public void selectAndMove(MouseEvent evt){
        if (evt.getButton()==MouseEvent.BUTTON3){
            Point p = evt.getPoint();
            chooseBoardPiece(p);
        }
        else if (evt.getButton()==MouseEvent.BUTTON1){
            Point p = evt.getPoint();
            moveBoardPiece(p);
        }
    }
    private void chooseBoardPiece(Point p){        
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()!=null
                    && currentColor == chessMatrix[i][j].getCurrentChessPiece().getFigureColor()
                        )
                {                    
                    chessMatrix[i][j].setHighlighted(true);
                    selectedChessPiece=chessMatrix[i][j].getCurrentChessPiece();
                    sourceI=i; sourceJ=j;
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }
        repaint();        
    }
    private void moveBoardPiece(Point p){
        boolean flag=false;
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (selectedChessPiece!=null
                    && chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false                    
                    && 
                      (
                        chessMatrix[i][j].getCurrentChessPiece()==null 
                                || 
                                (chessMatrix[i][j].getCurrentChessPiece()!=null
                                    && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece() ) 
                                )
                      )                    
                    && selectedChessPiece.movementConditionFullfilled(sourceI, sourceJ, i, j)
                    && pathIsFree(sourceI, sourceJ, i,j)
                    )
                {   
                    chessMatrix[i][j].setCurrentChessPiece(selectedChessPiece);
                    selectedChessPiece=null;                    
                    flag=true;                    
                }
                else
                {                    
                    chessMatrix[i][j].setHighlighted(false);
                }            
            }
        }        
        if (flag==true) {
            if (currentColor==Color.WHITE) {
                currentColor=Color.BLACK;                        
            }
            else {
                currentColor=Color.WHITE;                    
            }
            chessMatrix[sourceI][sourceJ].setCurrentChessPiece(null);            
        }
        repaint();
    }
    @Override                 
    public void paint(Graphics g) {
        super.paint(g);         
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {
                if (chessMatrix[i][j].isHighlighted())                     
                    chessMatrix[i][j].highlightChessField(g);                                                                                
                else                     
                    chessMatrix[i][j].drawChessField(g);                
                if (chessMatrix[i][j].getCurrentChessPiece()!=null) {
                    Double x = chessMatrix[i][j].getX();
                    Double y = chessMatrix[i][j].getY();
                    chessMatrix[i][j].getCurrentChessPiece().drawPieceSymbol(g, x.intValue(),y.intValue());
                }
            }
        }   
    }
    //zrealizować w klasie
    private boolean pathIsFree(int x1, int y1, int x2, int y2){
//        System.out.println("X1: "+x1+
//                           " Y1: "+y1+
//                           " X2: "+x2+
//                           " Y2: "+y2);
//        System.out.println("Yes");
        int nullCount=0;        
        if (!(selectedChessPiece instanceof Knight)) {                    
            if (x1<x2) {
                for (int i = x1; i <= x2; i++)                       
                    if (y1>y2)
                        for (int j = y1; j>= y2; j--)
                            nullCount += pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                                        
                    else
                        for (int j = y1; j<=y2; j++)            
                            nullCount += pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                        
            }
            else {
                for (int i = x1; i >= x2; i--)            
                    if (y1>y2) 
                        for (int j = y1; j>= y2; j--) 
                            nullCount += pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                
                    else 
                        for (int j = y1; j<=y2; j++)            
                            nullCount += pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                        
            }
        }
//        System.out.println("/////////////////////");
        return nullCount == 0;
    }
    private void createFields(){
        int x=0,y=0;
        for (int i=610; i>-30; i-=80)
        {            
            for (int j=172; j<812; j+=80)
            {                
                ChessField c = new ChessField(j,i,80,80,x,y);
                chessMatrix[x][y] = c;                                
//                System.out.println(chessMatrix[x][y].toString());                
//                System.out.println(c+" "+this.getWidth()+" "+this.getHeight());
                y++;                
            }
            y=0;            
            x++;
            if (x==8) 
                x=0;
        }                
    }
    private void clearBoard() {
        currentColor=Color.WHITE;
        for (int i=0; i<8; i++)             
            for (int j=0; j<8; j++) 
                chessMatrix[i][j].setCurrentChessPiece(null);
    }
    public void setPieces(GameState g) {
        
        Color c = null;
        clearBoard();
        switch(g) {
            case NEW:                
                for (int i=0; i<8; i++) {            
                    chessMatrix[1][i].setCurrentChessPiece(new Pawn(Color.BLACK));            
                    chessMatrix[6][i].setCurrentChessPiece(new Pawn(Color.WHITE));
                }                
                for (int i=0; i<=7; i+=7) {
                    switch(i) {
                        case 0:
                            c=Color.BLACK;
                            break;
                        case 7:
                            c=Color.WHITE;
                            break;
                    }
                    chessMatrix[i][0].setCurrentChessPiece(new Rook(c));
                    chessMatrix[i][1].setCurrentChessPiece(new Knight(c));
                    chessMatrix[i][2].setCurrentChessPiece(new Bishop(c));
                    chessMatrix[i][3].setCurrentChessPiece(new Queen(c));
                    chessMatrix[i][4].setCurrentChessPiece(new King(c));
                    chessMatrix[i][5].setCurrentChessPiece(new Bishop(c));                
                    chessMatrix[i][6].setCurrentChessPiece(new Knight(c));
                    chessMatrix[i][7].setCurrentChessPiece(new Rook(c));   
                }
                break;
        }                
        repaint();
    }
    private int parseColorValue(Color c) {
        if (c == Color.BLACK) 
            return 0;        
        else 
            return 1;
    }
    private Color parseIntValue(int i) {
        if (i == 0) 
            return Color.BLACK;
        else 
            return Color.WHITE;
    }
    private void setLoadedGamePieces(String pieceID, int x, int y) {        
        if(pieceID != null) {
//            System.out.println(x+" "+ y +" " +Integer.parseInt(pieceID));
            chessMatrix[x][y].setCurrentChessPiece(choosePiece(Integer.parseInt(pieceID)));
        }
    }
    public void loadGame() {
        clearBoard();
        getGameIDfromDB();
        getGameColorFromDB();
        String selectChessFields = "SELECT x, y, piece FROM chessFields WHERE game="+gameID+";";
        sqlConnection(selectChessFields, QueryType.SELECT_CHESS_FIELDS);        
    }
    private void getGameIDfromDB() {
        String selectMaxGameID = "SELECT MAX(gameID) FROM games;"; 
        sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_ID);
    }
    private void getGameColorFromDB() {
        String selectMaxGameID = "SELECT currentColor FROM games WHERE gameID = "+gameID+";"; 
        sqlConnection(selectMaxGameID, QueryType.SELECT_GAME_COLOR);
    }
    public void saveGame() {        
        String selectPieceID; 
        String insertFields = "";
        String insertNewGame = "INSERT INTO games VALUES((SELECT MAX(gameID) FROM games)+1,"+parseColorValue(currentColor)+");";        
        getGameIDfromDB();
        gameID++;
        for (int x=0; x<8; x++) {             
            for (int y=0; y<8; y++) {
                if (chessMatrix[x][y].getCurrentChessPiece() != null) {
                    selectPieceID = "(SELECT pieceID FROM chessPieces WHERE pieceName = '" 
                                    + chessMatrix[x][y].getCurrentChessPiece().getChessPieceName() 
                                    + "' AND pieceColor = " 
                                    + parseColorValue(chessMatrix[x][y].getCurrentChessPiece().getFigureColor()) 
                                    + ")";
                    insertFields += "INSERT INTO chessFields VALUES ("
                                    + "(SELECT MAX(chessFieldID) FROM chessFields)+1,"
                                    + x + ","
                                    + y + ","
                                    + selectPieceID + ","
                                    + gameID 
                                    + ");\n";                    
                }
                else {
                    insertFields += "INSERT INTO chessFields (x, y, game) VALUES ("
                                    + x + ","
                                    + y + ","
                                    + gameID 
                                    + ");\n";
                }
            }
        }
        insertNewGame += insertFields;
        sqlConnection(insertNewGame, QueryType.OTHER);
//        System.out.println(insertFields);
//        System.out.println(ctr);
    }
    private int pathIsFreeSubroutine(int x1,int y1, int x2, int y2, int i, int j) {
        int nullCount=0;
        if (selectedChessPiece.movementConditionFullfilled(x1,y1,i,j)
            && (i!=x1 && y1==y2) 
                || (j!=y1 && x1==x2) 
                || (i!=x1 && j!=y1 && Math.abs(y1-j) == Math.abs(x1-i))
            )  {
            boolean result=chessMatrix[i][j].getCurrentChessPiece()!=null;                           
            if (result && !(i==x2 && j==y2 && selectedChessPiece.isFoe(chessMatrix[i][j].getCurrentChessPiece())))
                nullCount++;
//            System.out.println(result+" "+i+" "+j+" "+nullCount);
        }
        return nullCount;
    }
    private ChessPiece choosePiece(int num) {
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
    public static ChessField[][] chessMatrix = new ChessField[8][8];                ;  
    private int sourceI, sourceJ;    
    private ChessPiece selectedChessPiece;
    private Player playerBlack;
    private Player playerWhite;
    private Color currentColor;
    private int gameID = 1;
    public static enum GameState { NEW, SAVED };
    public static enum QueryType { OTHER, SELECT_CHESS_FIELDS, SELECT_GAME_ID, SELECT_GAME_COLOR };
}
