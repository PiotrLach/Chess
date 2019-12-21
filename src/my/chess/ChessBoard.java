/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import my.chess.pieces.*;


/**
 *
 * @author bruce
 */
public class ChessBoard extends JPanel {
    public ChessBoard() {
//        calculateSize();
        beginHeight = 640;
        endHeight = 0;
        beginWidth = 0;
        endWidth = 640;
        diffHorizontal = 80;
        diffVertical = 80;
        createFields();           
//        createNewDatabase("test.db");
//        setPieces();
    }
    public void calculateSize() {        
//        System.out.println(getWidth() + " " + getHeight()); 
        int height = getHeight(),
                width = getWidth();
//        System.out.println(width + " " + height);
        beginHeight = (int) height;
        endHeight = (int) 0;
//        beginHeight = height;        
        while ( (beginHeight - endHeight) % 8 != 0) {            
                beginHeight--;            
        }
        diffVertical = (beginHeight - endHeight) / 8;
        beginHeight -= diffVertical;
        endHeight -= diffVertical;
        beginWidth =  (int) (width - height) / 2;
        endWidth = (int) beginWidth + height;      
        while ( (endWidth - beginWidth) % 8 != 0) {
//            System.out.println(endWidth + " " + beginWidth);
            endWidth--;
        }   
        diffHorizontal = (endWidth - beginWidth) / 8;
//        System.out.println(beginHeight + " " + endHeight + " " + beginWidth + " " + endWidth);
        int x=0,y=0;
//        System.out.println(diffHorizontal + " " + diffVertical);
        for (int i = beginHeight; i > endHeight; i -= diffVertical) {         
            for (int j = beginWidth; j < endWidth; j += diffHorizontal)  {
                chessMatrix[x][y].setLocation(j, i);
                chessMatrix[x][y].setSize(diffHorizontal,diffVertical);
                y++;                
            }
            y=0;            
            x++;
            if (x==8) 
                x=0;               
        }
        repaint();
    }
    private void createFields(){
        int x=0,y=0;
        for (int i = beginHeight; i > endHeight; i -= diffVertical) {         
            for (int j = beginWidth; j < endWidth; j += diffHorizontal)  {
//        for (int i = 640; i > 0; i -= 80) {         
//            for (int j = 0; j < 640; j += 80)  {
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
    public void selectAndMove(MouseEvent evt){
        if (evt.getButton()==MouseEvent.BUTTON3){
            Point p = evt.getPoint();
            chooseBoardPiece(p);
            check();
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
    private Point findKing() throws Exception {
        Point king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {  
                ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                if (cp instanceof King && cp.getFigureColor() == currentColor) {
                    king = new Point(i,j);
                    break;
                }
            }            
        }
        if (king != null)
            return king;
        else 
            throw new Exception("King has not been found.");
    }
    private void check() {        
        try { 
            Point p = findKing();
            System.out.println(p);
            System.out.println(checkKnights( (int) p.getX(), (int) p.getY()));
            checkBishops( (int) p.getX(), (int) p.getY());
        } 
        catch (Exception e) {System.out.println(e);};
//        int checkCount = 0;        
//        checkCount += checkKnights(0,0);
        /*
        checkOthers();
        checkPawns();
        */
//        return checkCount != 0;
    }  
    private void checkBishops(int kingX, int kingY) {
        try {            
            checkQuarter(Direction.TOP_LEFT, kingX, kingY);
            checkQuarter(Direction.TOP_RIGHT, kingX, kingY);
            checkQuarter(Direction.BOTTOM_LEFT, kingX, kingY);
            checkQuarter(Direction.BOTTOM_RIGHT, kingX, kingY);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private enum Direction {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    private void checkQuarter(Direction d, int kingX, int kingY) throws Exception {
        ChessPiece king = chessMatrix[kingX][kingY].getCurrentChessPiece();
        int limitX, diffX, limitY, diffY, x, y;
        switch (d) {            
            case TOP_LEFT:
                limitX = 8; diffX = 1; limitY = -1; diffY = -1; x = kingX + 1; y = kingY - 1;
                break;
            case TOP_RIGHT:                
                limitX = 8; diffX = 1; limitY = 8; diffY = 1; x = kingX + 1; y = kingY + 1;
                break;
            case BOTTOM_LEFT:                
                limitX = -1; diffX = -1; limitY = -1; diffY = -1; x = kingX - 1; y = kingY - 1;
                break;
            case BOTTOM_RIGHT:   
                limitX = -1; diffX = -1; limitY = 8; diffY = 1; x = kingX - 1; y = kingY + 1;
                break;
            default:
                throw new Exception("Incorrect direction specified!");
        }
        boolean isFoeOccupied = false;
        for (int i = x, j = y; i != limitX && j != limitY; i += diffX, j += diffY) {
            ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
            System.out.println(i + "," + j);
            if (cp != null)                             
            {
                isFoeOccupied = cp.isFoe(king) && (cp instanceof Bishop || cp instanceof Queen);
                break;
            }            
        }
        System.out.println(isFoeOccupied);
    }
    private void checkRooks(int kingX, int kingY) {
        /*
        for (int x = kingX; x < ; )
            for(int y = kingY; y < ; )
        */
    }
    private int checkKnights(int kingX, int kingY) {
        Point[] points =  { new Point(1,2), new Point(-1,2),
                          new Point(-2,1), new Point(-2,-1),
                          new Point(1,-2), new Point(-1,-2),
                          new Point(2,-1), new Point(2,1) };
        int x, y, checkCount = 0;
        for (Point p : points) {
            x = kingX + (int) p.getX();
            y = kingY + (int) p.getY();
            if (x <= 7 && x >= 0 && y <= 7 && y >= 0)
                if (chessMatrix[x][y].getCurrentChessPiece() instanceof Knight)
                    checkCount++;            
        }
        return checkCount;
    }
    private boolean mate() {
        int mateCount = 0;
        return mateCount != 0;
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
                    chessMatrix[i][j].drawChessField(g,i,j);                
                if (chessMatrix[i][j].getCurrentChessPiece()!=null) {
                    Double x = chessMatrix[i][j].getX();
                    Double y = chessMatrix[i][j].getY();
                    Double width = chessMatrix[i][j].getWidth(); 
                    Double height = chessMatrix[i][j].getHeight();
//                    chessMatrix[i][j].getCurrentChessPiece().drawPieceSymbol(g, x.intValue(),y.intValue());
                    chessMatrix[i][j].getCurrentChessPiece().drawImage(g, x.intValue(), y.intValue(), width.intValue(), height.intValue());
                }
            }
        }
//        if (highlightOnResize) {
//            highlightAll(g);
//        }
    }
    private boolean pathIsFree(int x1, int y1, int x2, int y2) {
        int verticalDifference, horizontalDifference, notNullCount = 0;
        if (!(selectedChessPiece instanceof Knight)) {
            verticalDifference = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);                        
            horizontalDifference = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
            x1 += verticalDifference;
            y1 += horizontalDifference;
            for (int i = x1, j = y1; i != x2 || j != y2; i += verticalDifference, j+= horizontalDifference) {
                ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                notNullCount += cp == null ? 0 : 1;
            }
            if (notNullCount == 0) {
                return true;
            }
            else {
                JOptionPane.showMessageDialog(this, "Ruch niedozwolony: po drodze są inne figury!");
                return false;
            }                        
        }
        else {
            return true;
        }
    } 
    public void setNewGame() throws IOException {
        
        Color c = null;
        clearBoard();                               
        for (int i=0; i<8; i++) {            
            chessMatrix[1][i].setCurrentChessPiece(new Pawn(Color.BLACK,Images.getPAWN_BLACK()));            
            chessMatrix[6][i].setCurrentChessPiece(new Pawn(Color.WHITE,Images.getPAWN_WHITE()));
        }                
        chessMatrix[0][0].setCurrentChessPiece(new Rook(Color.BLACK,Images.getROOK_BLACK()));
        chessMatrix[0][1].setCurrentChessPiece(new Knight(Color.BLACK,Images.getKNIGHT_BLACK()));
        chessMatrix[0][2].setCurrentChessPiece(new Bishop(Color.BLACK,Images.getBISHOP_BLACK()));
        chessMatrix[0][3].setCurrentChessPiece(new Queen(Color.BLACK,Images.getQUEEN_BLACK()));
        chessMatrix[0][4].setCurrentChessPiece(new King(Color.BLACK,Images.getKING_BLACK()));
        chessMatrix[0][5].setCurrentChessPiece(new Bishop(Color.BLACK,Images.getBISHOP_BLACK()));                
        chessMatrix[0][6].setCurrentChessPiece(new Knight(Color.BLACK,Images.getKNIGHT_BLACK()));
        chessMatrix[0][7].setCurrentChessPiece(new Rook(Color.BLACK,Images.getROOK_BLACK()));
        chessMatrix[7][0].setCurrentChessPiece(new Rook(Color.WHITE,Images.getROOK_WHITE()));
        chessMatrix[7][1].setCurrentChessPiece(new Knight(Color.WHITE,Images.getKNIGHT_WHITE()));
        chessMatrix[7][2].setCurrentChessPiece(new Bishop(Color.WHITE,Images.getBISHOP_WHITE()));
        chessMatrix[7][3].setCurrentChessPiece(new Queen(Color.WHITE,Images.getQUEEN_WHITE()));
        chessMatrix[7][4].setCurrentChessPiece(new King(Color.WHITE,Images.getKING_WHITE()));
        chessMatrix[7][5].setCurrentChessPiece(new Bishop(Color.WHITE,Images.getBISHOP_WHITE()));                
        chessMatrix[7][6].setCurrentChessPiece(new Knight(Color.WHITE,Images.getKNIGHT_WHITE()));
        chessMatrix[7][7].setCurrentChessPiece(new Rook(Color.WHITE,Images.getROOK_WHITE()));   
        repaint();
    } 
    private void clearBoard() {
        currentColor=Color.WHITE;
        for (int i=0; i<8; i++)             
            for (int j=0; j<8; j++) 
                chessMatrix[i][j].setCurrentChessPiece(null);
    }
    public static void setCurrentColor(Color c) throws IllegalArgumentException {
        if (c == Color.BLACK || c == Color.WHITE)
            currentColor = c;
        else 
            throw new IllegalArgumentException("Current color can only be black or white");
    }
    public static Color getCurrentColor()  {
        return currentColor;
    }
    public static ChessField getChessMatrixField(int x, int y) throws IllegalArgumentException {
        if (x < 8 && y < 8) 
            return chessMatrix[x][y];
        else 
            throw new IllegalArgumentException("Columns and rows indices cannot exceed 7");
    }
    public static void setChessMatrixField(int x, int y, ChessPiece cp) throws IllegalArgumentException {
        if (x < 8 && y < 8) 
            chessMatrix[x][y].setCurrentChessPiece(cp);
        else 
            throw new IllegalArgumentException("Columns and rows indices cannot exceed 7");
    }
    private static final ChessField[][] chessMatrix = new ChessField[8][8];
    private static Color currentColor;            
    private int sourceI, sourceJ;    
    private ChessPiece selectedChessPiece;
    private int beginHeight,
                endHeight,
                beginWidth,
                endWidth,
                diffHorizontal,
                diffVertical;
    
}
