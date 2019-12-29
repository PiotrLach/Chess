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
import java.util.Arrays;
import java.util.HashMap;
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
        }
        else if (evt.getButton()==MouseEvent.BUTTON1){
            Point p = evt.getPoint();
            moveBoardPiece(p);            
        }
    }
    private void chooseBoardPiece(Point p){
        loop:
        for (int i=0; i<8;i++) {
            for (int j=0;j<8; j++) {                
                if (chessMatrix[i][j].contains(p) 
                    && chessMatrix[i][j].isHighlighted()==false
                    && chessMatrix[i][j].getCurrentChessPiece()!=null
                    && currentColor == chessMatrix[i][j].getCurrentChessPiece().getFigureColor()
                        )
                {                    
                    chessMatrix[sourceI][sourceJ].setHighlighted(false);
                    chessMatrix[i][j].setHighlighted(true);
                    selectedChessPiece=chessMatrix[i][j].getCurrentChessPiece();
                    sourceI=i; sourceJ=j;   
                    repaint();  
                    break loop;
                }
//                else
//                {                    
//                    chessMatrix[i][j].setHighlighted(false);
//                }            
            }
        }      
    }
    private Point findKing() throws Exception {        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {  
                ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                if (cp instanceof King && cp.getFigureColor() == currentColor) {
                    Point kingCoordinates = new Point(i,j);
                    return kingCoordinates;
                }
            }            
        }        
        throw new Exception("King has not been found.");
    }    
    private boolean piecesToBlockCheckAvailable() {
//        System.out.println("find friends /////////////////////////////////////");
        int sum = 0;
        for (Point p : path) {        
            int x = (int) p.getX(), y = (int) p.getY();
//            System.out.println(x + "," + y);
//            System.out.println(checkKnights(x,y,oppositeColor));
//            System.out.println(checkBishops(x,y,oppositeColor));
//            System.out.println(checkRooks(x,y,oppositeColor));
            sum += checkKnights(x,y,oppositeColor);
            sum += checkBishops(x,y,oppositeColor);
            sum += checkRooks(x,y,oppositeColor);
            if(chessMatrix[x][y].getCurrentChessPiece() != null)
                sum += checkPawns(x,y,oppositeColor, Type.FRIEND);
            else
                sum += checkPawnsOnUnocuppiedFields(x,y);
            if (sum > 0) {
                return true;
            }
        }
        System.out.println(sum);
//        System.out.println("find friends /////////////////////////////////////");
        return sum > 0;
    }
    private void check() {        
        try { 
            Point p = findKing();
            System.out.println(p);
            int x = (int) p.getX(), y = (int) p.getY();
//            System.out.println(checkKnights(x,y,currentColor));
//            System.out.println(checkBishops(x,y,currentColor));
//            System.out.println(checkRooks(x,y,currentColor));
//            System.out.println(checkPawns(x,y,currentColor, Type.FOE));
            int sum = 0;
            sum += checkKnights(x,y,currentColor);
            sum += checkBishops(x,y,currentColor);
            sum += checkRooks(x,y,currentColor);
            sum += checkPawns(x,y,currentColor, Type.FOE);
            System.out.println(path);
            System.out.println(sum);
            if (sum == 1) {
                JOptionPane.showMessageDialog(this, "Szach!");
                if(!piecesToBlockCheckAvailable()) {
                    mate(x,y);
                }
            }
            else if (sum > 1) {
                mate(x,y);
            }
//            if (check) {
//                System.out.println(path);
//                JOptionPane.showMessageDialog(this, "Szach!");
//            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }
    private void mate(int x, int y) {
        ArrayList<Point> rescue = new ArrayList();
        int sum = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i != x && j != y && i <=7 && i >= 0 && j <= 7 && j >= 0) {
                    ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                    if (cp != null && cp.getFigureColor() != currentColor) {
                        sum += checkKnights(i,j,currentColor);
                        sum += checkBishops(i,j,currentColor);
                        sum += checkRooks(i,j,currentColor);
                        sum += checkPawns(i,j,currentColor, Type.FOE);
                        if (sum == 0) rescue.add(new Point(i,j));
                    }
                    sum = 0;
                }
            }
        }
        System.out.println(rescue);
    }
    private int checkBishops(int kingX, int kingY, Color color) {
        int sum = 0;
        try {            
            sum += checkDiagonal(Direction.TOP_LEFT, kingX, kingY,color) ? 1 : 0;
            sum += checkDiagonal(Direction.TOP_RIGHT, kingX, kingY,color) ? 1 : 0;
            sum += checkDiagonal(Direction.BOTTOM_LEFT, kingX, kingY,color) ? 1 : 0;
            sum += checkDiagonal(Direction.BOTTOM_RIGHT, kingX, kingY,color) ? 1 : 0;
        } catch (Exception e) {
            System.out.println(e);            
        }
        return sum;
    }
    private int checkRooks(int kingX, int kingY, Color color) {
        int sum = 0;
        try {            
            sum += checkCross(Direction.HORIZONTAL_LEFT, kingX, kingY,color) ? 1 : 0;
            sum += checkCross(Direction.HORIZONTAL_RIGHT,kingX, kingY,color) ? 1 : 0;
            sum += checkCross(Direction.VERTICAL_UP, kingX, kingY,color) ? 1 : 0;
            sum += checkCross(Direction.VERTICAL_DOWN, kingX, kingY,color) ? 1 : 0;
        } catch (Exception e) {
            System.out.println(e);            
        }
        return sum;
    }
    private enum Direction {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        HORIZONTAL_LEFT, HORIZONTAL_RIGHT, VERTICAL_UP, VERTICAL_DOWN,
        VERTICAL_UP_TWO_PLACES,VERTICAL_DOWN_TWO_PLACES
    };
    private enum Type {
        FRIEND, FOE
    };
    private int checkPawnsOnUnocuppiedFields (int kingX, int kingY) {
        int sum = 0;
        try {
            if (startingPoints.get(currentColor) == 1) {
                sum += checkPawn(Direction.VERTICAL_DOWN, kingX, kingY, oppositeColor) ? 1 : 0;
                if (kingX == 3) {
                    sum += checkPawn(Direction.VERTICAL_DOWN_TWO_PLACES, kingX, kingY, oppositeColor) ? 1 : 0;
                }
            }
            else {
                sum += checkPawn(Direction.VERTICAL_UP, kingX, kingY, oppositeColor) ? 1 : 0;
                if (kingX == 4) {
                    sum += checkPawn(Direction.VERTICAL_UP_TWO_PLACES, kingX, kingY, oppositeColor) ? 1 : 0;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);      
        }
        return sum;
    }
    private int checkPawns(int kingX, int kingY, Color color, Type t) {
        int sum = 0;
        try {
            if (startingPoints.get(color) == 1) {
                if (t == Type.FOE) {
                    sum += checkPawn(Direction.TOP_LEFT, kingX, kingY, color) ? 1 : 0;
                    sum += checkPawn(Direction.TOP_RIGHT, kingX, kingY, color) ? 1 : 0;
                }
                else {
                    sum += checkPawn(Direction.BOTTOM_LEFT, kingX, kingY, color) ? 1 : 0;
                    sum += checkPawn(Direction.BOTTOM_RIGHT, kingX, kingY, color) ? 1 : 0;
                }
            }
            else {
                if (t == Type.FOE) {
                    sum += checkPawn(Direction.BOTTOM_LEFT, kingX, kingY, color) ? 1 : 0;
                    sum += checkPawn(Direction.BOTTOM_RIGHT, kingX, kingY, color) ? 1 : 0;
                }
                else{
                    sum += checkPawn(Direction.TOP_LEFT, kingX, kingY, color) ? 1 : 0;
                    sum += checkPawn(Direction.TOP_RIGHT, kingX, kingY, color) ? 1 : 0;
                }
            }
        } catch (Exception e) {
            System.out.println(e);            
        }        
        return sum;
    }

    private boolean checkPawn(Direction d, int kingX, int kingY, Color color) throws Exception {        
        int limitX, diffX, limitY, diffY, x = kingX, y = kingY;
        ArrayList<Point> pathTemp = new ArrayList();
        ChessPiece cp;
        switch (d) {
            case TOP_LEFT:
                limitX = 8; diffX = 1; limitY = -1; diffY = -1; 
                break;
            case TOP_RIGHT:                
                limitX = 8; diffX = 1; limitY = 8; diffY = 1;
                break;
            case BOTTOM_LEFT:                
                limitX = -1; diffX = -1; limitY = -1; diffY = -1;
                break;
            case BOTTOM_RIGHT:   
                limitX = -1; diffX = -1; limitY = 8; diffY = 1;
                break;
            case VERTICAL_UP:   
                limitX = 8; diffX = 1; limitY = 0; diffY = 0;
                break;
            case VERTICAL_DOWN:   
                limitX = -1; diffX = -1; limitY = 0; diffY = 0;
                break;
            case VERTICAL_UP_TWO_PLACES:   
                limitX = 8; diffX = 2; limitY = 0; diffY = 0;
                break;
            case VERTICAL_DOWN_TWO_PLACES:   
                limitX = -1; diffX = -2; limitY = 0; diffY = 0;
                break;
            default: 
                throw new Exception("Incorrect direction specified!");            
        }
        x += diffX;
        y += diffY;
        if (x != limitX && y != limitY) {
            cp = chessMatrix[x][y].getCurrentChessPiece();            
            if (cp instanceof Pawn && cp.getFigureColor() != color) {
                pathTemp.add(new Point(x,y));
                if (color == currentColor) path = new ArrayList(pathTemp);
                return true;                
            }                
        }  
        return false;
    }
    private boolean checkDiagonal(Direction d, int kingX, int kingY, Color color) throws Exception {
        int limitX, diffX, limitY, diffY, x = kingX, y = kingY;
        ArrayList<Point> pathTemp = new ArrayList();
        switch (d) {            
            case TOP_LEFT:
                limitX = 8; diffX = 1; limitY = -1; diffY = -1; 
                break;
            case TOP_RIGHT:                
                limitX = 8; diffX = 1; limitY = 8; diffY = 1;
                break;
            case BOTTOM_LEFT:                
                limitX = -1; diffX = -1; limitY = -1; diffY = -1;
                break;
            case BOTTOM_RIGHT:   
                limitX = -1; diffX = -1; limitY = 8; diffY = 1; 
                break;
            default:
                throw new Exception("Incorrect direction specified!");
        }
        x += diffX;
        y += diffY;        
        for (int i = x, j = y; i != limitX && j != limitY; i += diffX, j += diffY) {
            ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
//            System.out.println(i + "," + j);
            pathTemp.add(new Point(i,j));
            if (cp != null)                            
            {
                if (cp.getFigureColor() != color && (cp instanceof Bishop || cp instanceof Queen)) {
                    if (color == currentColor) path = new ArrayList(pathTemp);
                    return true;               
                }   
                else {
                    return false;
                }
            }            
        }
        return false;
    }
    private boolean checkCross(Direction d, int kingX, int kingY, Color color) throws Exception {
        int limitX, diffX, limitY, diffY, x = kingX, y = kingY;
        ArrayList<Point> pathTemp = new ArrayList();
        switch (d) {            
            case HORIZONTAL_LEFT:
                limitX = x; diffX = 0; limitY = -1; diffY = -1;
                break;
            case HORIZONTAL_RIGHT:                
                limitX = x; diffX = 0; limitY = 8; diffY = 1;
                break;
            case VERTICAL_DOWN:                
                limitX = -1; diffX = -1; limitY = y; diffY = 0;
                break;
            case VERTICAL_UP:   
                limitX = 8; diffX = 1; limitY = y; diffY = 0;
                break;
            default:
                throw new Exception("Incorrect direction specified!");
        }
        x += diffX; y += diffY;        
        for (int i = x, j = y; i != limitX || j != limitY; i += diffX, j += diffY) {
            ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
//            System.out.println(i + "," + j);
            pathTemp.add(new Point(i,j));
            if (cp != null )                             
            {                
                if (cp.getFigureColor() != color && (cp instanceof Rook || cp instanceof Queen)) {
                    if (color == currentColor) path = new ArrayList(pathTemp);
                    return true;               
                }   
                else {
                    return false;
                }
            }            
        }
        return false;
    }
    private int checkKnights(int kingX, int kingY, Color color) {        
        Point[] points =  { new Point(1,2), new Point(-1,2),
                          new Point(-2,1), new Point(-2,-1),
                          new Point(1,-2), new Point(-1,-2),
                          new Point(2,-1), new Point(2,1) };
        int x, y, checkCount = 0; ChessPiece cp;
        ArrayList<Point> pathTemp = new ArrayList();
        for (Point p : points) {
            x = kingX + (int) p.getX();
            y = kingY + (int) p.getY();            
            if (x <= 7 && x >= 0 && y <= 7 && y >= 0) {                
                cp = chessMatrix[x][y].getCurrentChessPiece();
                if (cp instanceof Knight && cp.getFigureColor() != color) {
//                    System.out.println(x + " " + y);
                    pathTemp.add(new Point(x,y));
                    if (color == currentColor) path = new ArrayList(pathTemp);
                    checkCount++;            
                }
            }
        }
        return checkCount;
    }

    private void moveBoardPiece(Point p){
//        boolean flag=false;
        loop:
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
                    chessMatrix[sourceI][sourceJ].setCurrentChessPiece(null);
                    chessMatrix[sourceI][sourceJ].setHighlighted(false);
                    if (currentColor == Color.WHITE) {
                        currentColor = Color.BLACK;
                        oppositeColor = Color.WHITE;
                    }
                    else {
                        currentColor = Color.WHITE;                    
                        oppositeColor = Color.BLACK;
                    }
                    repaint();
                    check();
                    break loop;                                        
                }
            }
        }        
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
    //                    Double width = chessMatrix[i][j].getWidth(); 
    //                    Double height = chessMatrix[i][j].getHeight();
    //                    chessMatrix[i][j].getCurrentChessPiece().drawPieceSymbol(g, x.intValue(),y.intValue());
                    chessMatrix[i][j].getCurrentChessPiece().drawImage(g, x.intValue(), y.intValue(), diffHorizontal,diffVertical);
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
            if (notNullCount > 0) {
                JOptionPane.showMessageDialog(this, "Ruch niedozwolony: po drodze są inne figury!");
            }            
        }
        return notNullCount == 0;
    }
    
    public void setNewGame() throws IOException {
                        
        clearBoard();                               
        Color c1;
        Color c2;
        if (new Random().nextInt(2) == 1) {
            c1 = Color.BLACK;
            c2 = Color.WHITE;
            startingPoints.put(Color.BLACK, 1);
            startingPoints.put(Color.WHITE, 6);
        }
        else {
            c1 = Color.WHITE;
            c2 = Color.BLACK;
            startingPoints.put(Color.WHITE, 1);
            startingPoints.put(Color.BLACK, 6);
        }
        for (int i=0; i<8; i++) {            
            chessMatrix[1][i].setCurrentChessPiece(new Pawn(c1,Images.getPAWN(c1),1));            
            chessMatrix[6][i].setCurrentChessPiece(new Pawn(c2,Images.getPAWN(c2),6));
        }                
        chessMatrix[0][0].setCurrentChessPiece(new Rook(c1,Images.getROOK(c1)));
        chessMatrix[0][1].setCurrentChessPiece(new Knight(c1,Images.getKNIGHT(c1)));
        chessMatrix[0][2].setCurrentChessPiece(new Bishop(c1,Images.getBISHOP(c1)));
        chessMatrix[0][3].setCurrentChessPiece(new Queen(c1,Images.getQUEEN(c1)));
        chessMatrix[0][4].setCurrentChessPiece(new King(c1,Images.getKING(c1)));
        chessMatrix[0][5].setCurrentChessPiece(new Bishop(c1,Images.getBISHOP(c1)));                
        chessMatrix[0][6].setCurrentChessPiece(new Knight(c1,Images.getKNIGHT(c1)));
        chessMatrix[0][7].setCurrentChessPiece(new Rook(c1,Images.getROOK(c1)));
        chessMatrix[7][0].setCurrentChessPiece(new Rook(c2,Images.getROOK(c2)));
        chessMatrix[7][1].setCurrentChessPiece(new Knight(c2,Images.getKNIGHT(c2)));
        chessMatrix[7][2].setCurrentChessPiece(new Bishop(c2,Images.getBISHOP(c2)));
        chessMatrix[7][3].setCurrentChessPiece(new Queen(c2,Images.getQUEEN(c2)));
        chessMatrix[7][4].setCurrentChessPiece(new King(c2,Images.getKING(c2)));
        chessMatrix[7][5].setCurrentChessPiece(new Bishop(c2,Images.getBISHOP(c2)));                
        chessMatrix[7][6].setCurrentChessPiece(new Knight(c2,Images.getKNIGHT(c2)));
        chessMatrix[7][7].setCurrentChessPiece(new Rook(c2,Images.getROOK(c2)));   
        repaint();
    } 
    private void clearBoard() {
        currentColor=Color.WHITE;
        startingPoints = new HashMap<>();
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
    private HashMap<Color, Integer> startingPoints;
    private ArrayList<Point> path;
    private static final ChessField[][] chessMatrix = new ChessField[8][8];    
    private static Color currentColor;            
    private Color oppositeColor;
    private int sourceI, sourceJ, destI, destJ;  
    private ChessPiece selectedChessPiece;
    private int beginHeight,
                endHeight,
                beginWidth,
                endWidth,
                diffHorizontal,
                diffVertical;
    
}
