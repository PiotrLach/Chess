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
        currentColor=Color.WHITE;
        createFields();
//        createNewDatabase("test.db");
//        setPieces();
    }
    public void createNewDatabase(String fileName) {
 
        String url = "jdbc:sqlite:/home/bruce/Dokumenty/TestDatabase/" + fileName;
 
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
    public void sqlConnection() {
        Connection c = null;

        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
              // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
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
                if (chessMatrix[i][j].isHighlighted()) {                    
                    chessMatrix[i][j].highlightChessField(g);                                            
                }    
                else {
                    chessMatrix[i][j].drawChessField(g);
                }    
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
                            nullCount+=pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                                        
                    else
                        for (int j = y1; j<=y2; j++)            
                            nullCount+=pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                        
            }
            else {
                for (int i = x1; i >= x2; i--)            
                    if (y1>y2) 
                        for (int j = y1; j>= y2; j--) 
                            nullCount+=pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                
                    else 
                        for (int j = y1; j<=y2; j++)            
                            nullCount+=pathIsFreeSubroutine(x1,y1,x2,y2,i,j);                        
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
    public void setPieces() {
        Color c = null;
        for (int i=0; i<8; i++) {            
            chessMatrix[1][i].setCurrentChessPiece(new Pawn(Color.BLACK,1,i));            
            chessMatrix[6][i].setCurrentChessPiece(new Pawn(Color.WHITE,6,i));
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
            chessMatrix[i][0].setCurrentChessPiece(new Rook(c, i, 0));
            chessMatrix[i][1].setCurrentChessPiece(new Knight(c, i, 1));
            chessMatrix[i][2].setCurrentChessPiece(new Bishop(c, i, 2));
            chessMatrix[i][3].setCurrentChessPiece(new Queen(c, i, 3));
            chessMatrix[i][4].setCurrentChessPiece(new King(c, i, 4));
            chessMatrix[i][5].setCurrentChessPiece(new Bishop(c, i, 5));                
            chessMatrix[i][6].setCurrentChessPiece(new Knight(c, i, 6));
            chessMatrix[i][7].setCurrentChessPiece(new Rook(c, i, 7));   
        }
        repaint();
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
    public static ChessField[][] chessMatrix = new ChessField[8][8];                ;  
    private int sourceI, sourceJ;    
    private ChessPiece selectedChessPiece;
    private Player playerBlack;
    private Player playerWhite;
    private Color currentColor;
    private enum Game { New, Saved };
}
