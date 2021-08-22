/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import my.chess.pieces.Rook;
import my.chess.pieces.Knight;
import my.chess.pieces.King;
import my.chess.pieces.Piece;
import my.chess.pieces.Queen;
import my.chess.pieces.Bishop;
import my.chess.pieces.Pawn;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Piotr Lach
 */
public class Board extends JPanel {

    public Board() {
        a1 = 640;
        a0 = 0;
        b0 = 0;
        b1 = 640;
        squareSize = 80;
        createSquares();
    }
    /**
     * Recalculates the size and placement of each square, 
     * so that the board scales with window
     */
    public void recalculateSize() {
        int height = getHeight(), width = getWidth();
        a0 = 0;
        a1 = height;        
        while (a1 % 8 != 0) {
            a1--;
        }
        squareSize = a1 / 8;
        b0 = (width - height) / 2;
        b1 = b0 + height;
        while ((b1 - b0) % 8 != 0) {
            b1--;
        }
        for (int x = a0, row = 0; x < a1; x += squareSize, row++) {
            for (int y = b0, col = 0; y < b1; y += squareSize, col++) {
                squares[row][col].setLocation(y, x);
                squares[row][col].setSize(squareSize, squareSize);
            }
        }
        repaint();
    }

    private void createSquares() {        
        for (int x = a0, row = 0; x < a1; x += squareSize, row++) {
            for (int y = b0, col = 0; y < b1; y += squareSize, col++) {
                Square square = new Square(y, x, 80, 80);
                squares[row][col] = square;                
            }            
        }
    }

    public void selectAndMove(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            Point point = mouseEvent.getPoint();
            choosePiece(point);
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            Point point = mouseEvent.getPoint();
            movePiece(point);
        }
    }

    private boolean isChoosable(Square square, Point input) {
        Piece piece = square.getPiece();
        boolean check = !isCheck
            || (isCheck && isCheckBlockPossible)
            || (isCheck && piece instanceof King && !isCheckBlockPossible);
        return !isMate
            && square.contains(input)
            && !square.isHighlighted()
            && piece != null
            && currentColor == piece.color
            && check;
    }
    
    private void choosePiece(Point input) {        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                Square square = squares[row][col];
                Piece piece = square.getPiece();
                
                if (isChoosable(square, input)) {
                    
                    squares[sourceRow][sourceCol].setHighlighted(false);
                    square.setHighlighted(true);                    
                    selectedPiece = piece;
                    sourceRow = row;
                    sourceCol = col;
                    repaint();
                    return;                               
                } else if (square.contains(input) 
                        && piece != null
                        && currentColor != piece.color) {
                    
                    boolean isWhite = currentColor == Color.WHITE;
                    String colorName = isWhite ? "białych" : "czarnych";
                    String message = "Teraz ruch " + colorName + "!";
                    JOptionPane.showMessageDialog(this, message);
                    return;
                }
            }
        }
    }
    /**
     * Finds coordinates of current player's king
     * @return
     * @throws Exception 
     */
    private Point findKing() throws Exception {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col].getPiece();
                if (piece instanceof King && piece.color == currentColor) {
                    Point kingCoordinates = new Point(row, col);
                    return kingCoordinates;
                }
            }
        }
        throw new Exception("King has not been found.");
    }

    private void check() {
        int kingX = 0, kingY = 0;
        try {
            Point point = findKing();
            kingX = (int) point.getX();
            kingY = (int) point.getY();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        isCheck = isCheck(kingX, kingY, true);
    }

    /**
     * Tests if check occurs
     *
     * @param kingRow
     * @param kingCol
     * @param separate
     * @return check value
     */
    private boolean isCheck(int kingRow, int kingCol, boolean separate) {
        ArrayList<Point> enemySquaresTemp = new ArrayList();
        int sum = 0;
        boolean isKnight;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece();                                
                if (row != kingRow
                        && col != kingCol
                        && piece != null
                        && piece.color != currentColor
                        && piece.isCorrectMovement(row, col, kingRow, kingCol)
                        && (isKnight = piece instanceof Knight 
                            || isPathFree(row, col, kingRow, kingCol))) 
                { // ???                          
                    if (!isKnight && separate) {
                        enemySquaresTemp.add(new Point(row, col));
                        var coordinates = getPath(row, col, kingRow, kingCol);
                        enemySquaresTemp.addAll(coordinates);
                    }
                    sum++;
                }                
            }
        }
        if (separate) {
            enemySquares = new ArrayList(enemySquaresTemp);
        }
        if (sum == 0) {
            return false;
        } else if (sum > 0 && separate) {
            JOptionPane.showMessageDialog(this, "Szach!");
            kingEscapeSquares = new ArrayList(mate(kingRow, kingCol));
            System.out.println(kingEscapeSquares);
            isCheckBlockPossible = isCheckBlockPossible(enemySquaresTemp);
            if (!isCheckBlockPossible) {
                if (isMate = kingEscapeSquares.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mat!");
                }
            }
        }
        return true;
    }

    private ArrayList<Point> mate(int kingRow, int kingCol) {
        ArrayList<Point> escapeSquares = new ArrayList();
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                if ((row <= 7 && row >= 0) && (col <= 7 && col >= 0)) {
                    Piece piece = squares[row][col].getPiece();
                    if (piece == null || piece.color != currentColor) {
                        System.out.println(row + " " + col);
                        if (!isCheck(row, col, false)) {
                            escapeSquares.add(new Point(row, col));
                        }
                    }
                }
            }
        }
        return escapeSquares;
    }

    private boolean isCheckBlockPossible(ArrayList<Point> enemySquares) {
        for (Point point : enemySquares) {
            int x = (int) point.getX(), y = (int) point.getY();
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square square = squares[row][col];
                    Piece piece = square.getPiece();
                    if (piece != null
                            && !(piece instanceof King)
                            && piece.color == currentColor
                            && piece.isCorrectMovement(row, col, x, y)
                            && isPathFree(row, col, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Tests if user's move will result in a check
     * @param row
     * @param column     
     */
    private boolean isSelfMadeCheck(int row, int column) {
        if (!isCheck) {
            Square source = squares[sourceRow][sourceCol];
            Square target = squares[row][column];
            source.setPiece(null);
            target.setPiece(selectedPiece);
            int sum = 0;
            try {
                Point point = findKing();
                int x = (int) point.getX(), y = (int) point.getY();
                sum += !isCheck(x, y, false) ? 0 : 1;
            } catch (Exception e) {
                System.out.println(e);
            }
            if (sum > 0) {
                var message = "Ruch niedozwolony: skutkowałby szachem króla!\n";
                JOptionPane.showMessageDialog(this, message);
            }
            source.setPiece(selectedPiece);
            target.setPiece(null);
            return sum > 0;
        } else {
            return false;
        }
    }
    
    private boolean isMoveable(Point dest, Square square, int row, int col) {
        Piece piece = square.getPiece();
        Point current = new Point(row, col);
        
        var kingEscape = isCheck 
                && selectedPiece instanceof King 
                && kingEscapeSquares.contains(current);
        
        var checkBlock = isCheck 
                && !(selectedPiece instanceof King)                
                && enemySquares.contains(current);       
        
        return selectedPiece != null
            && square.contains(dest)
            && !square.isHighlighted()
            && (piece == null || selectedPiece.isFoe(piece))
            && selectedPiece.isCorrectMovement(sourceRow, sourceCol, row, col)
            && isPathFree(sourceRow, sourceCol, row, col)
            && !isSelfMadeCheck(row, col)
            && (!isCheck || checkBlock || kingEscape);        
    }

    private void movePiece(Point dest) {        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                Square square = squares[row][col];                
                
                if (isMoveable(dest, square, row, col)) {
                    
                    isCheck = false;
                    square.setPiece(selectedPiece);
                    selectedPiece = null;
                    squares[sourceRow][sourceCol].setPiece(null);
                    squares[sourceRow][sourceCol].setHighlighted(false);
                    var isWhite = currentColor == Color.WHITE;
                    currentColor = isWhite ? Color.BLACK : Color.WHITE;
                    oppositeColor = !isWhite ? Color.BLACK : Color.WHITE;                    
                    repaint();                    
                    check();                   
                    return;
                    
                } else if (selectedPiece != null
                        && square.contains(dest)
                        && !selectedPiece.isCorrectMovement(sourceRow, sourceCol, row, col)) {
                    JOptionPane.showMessageDialog(this, "Ruch niedozwolony!\n");
                    return;
                }
            }
        }
    }

    private void promote(Pawn pawn) {
        String[] possibilites = {"Goniec"};
        String string = (String) JOptionPane.showInputDialog(
                this,
                "Wybierz figurę:\n",
                "Wybierz figurę",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                possibilites,
                possibilites[0]);
        System.out.println(string);

    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                if (square.isHighlighted()) {
                    square.highlightSquare(graphics);
                } else {
                    square.drawSquare(graphics, row, col);
                }
                if (square.getPiece() != null) {                                        
                    Double x = square.getX();
                    Double y = square.getY();
                    Piece piece = square.getPiece();
                    piece.drawImage(graphics,
                            x.intValue(),
                            y.intValue(),
                            squareSize,
                            squareSize
                    );
                }
            }
        }
    }
    /**
     * Retrieves a list of coordinates between point (x1, y1) and (x2, y2)
     * @param x1
     * @param y1
     * @param x2
     * @param y2      
     */
    private ArrayList<Point> getPath(int x1, int y1, int x2, int y2) {
        ArrayList<Point> coordinates = new ArrayList();
        int verticalDifference, horizontalDifference;
        verticalDifference = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
        horizontalDifference = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
        x1 += verticalDifference;
        y1 += horizontalDifference;
        for (int i = x1, j = y1; i != x2 || j != y2; i += verticalDifference, j += horizontalDifference) {
            coordinates.add(new Point(i, j));
        }
        return coordinates;
    }
    /**
     * Checks if there are pieces on the path between point (x1, y1)
     * and point (x2, y2)
     * @param x1
     * @param y1
     * @param x2
     * @param y2     
     */
    private boolean isPathFree(int x1, int y1, int x2, int y2) {
        System.out.format("%d %d %d %d\n", x1, y1, x2, y2);
        int verticalDifference, horizontalDifference, notNullCount = 0;
        if (!(selectedPiece instanceof Knight)) {            
            verticalDifference = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
            horizontalDifference = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);           
            x1 += verticalDifference;
            y1 += horizontalDifference;
            for (int i = x1, j = y1; i != x2 || j != y2; i += verticalDifference, j += horizontalDifference) {
                System.out.format("%d %d\n", i, j);
                Piece piece = squares[i][j].getPiece();
                notNullCount += piece == null ? 0 : 1;
            }
        }
        return notNullCount == 0;
    }

    public void setNewGame() throws IOException {
        clearBoard();
        int draw = new Random().nextInt(2);
        Color color1 = draw == 1 ? Color.BLACK : Color.WHITE;
        Color color2 = color1 == Color.BLACK ? Color.WHITE : Color.BLACK;
        startingPoints.put(color1, 1);
        startingPoints.put(color2, 6);
        for (int col = 0; col < 8; col++) {
            squares[1][col].setPiece(new Pawn(color1, Piece.PieceName.Pawn1));
            squares[6][col].setPiece(new Pawn(color2, Piece.PieceName.Pawn6));
        }
        for (int row = 0; row <= 7; row += 7) {
            Color color = row == 0 ? color1 : color2;
            squares[row][0].setPiece(new Rook(color));
            squares[row][1].setPiece(new Knight(color));
            squares[row][2].setPiece(new Bishop(color));
            squares[row][3].setPiece(new Queen(color));
            squares[row][4].setPiece(new King(color));
            squares[row][5].setPiece(new Bishop(color));
            squares[row][6].setPiece(new Knight(color));
            squares[row][7].setPiece(new Rook(color));
        }
        repaint();
    }

    public static void clearBoard() {
        currentColor = Color.WHITE;
        oppositeColor = Color.BLACK;
        isCheck = false;
        selectedPiece = null;
        startingPoints = new HashMap<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPiece(null);
            }
        }
    }

    public static void setCurrentColor(Color color) throws IllegalArgumentException {
        if (color == Color.BLACK || color == Color.WHITE) {
            currentColor = color;
        } else {
            String message = "Current color can only be black or white";
            throw new IllegalArgumentException(message);
        }
    }

    public static Color getCurrentColor() {
        return currentColor;
    }

    public static Square getSquare(int row, int col) throws IllegalArgumentException {
        if (row < 8 && col < 8) {
            return squares[row][col];
        } else {
            String message = "Columns and rows indices cannot exceed 7";
            throw new IllegalArgumentException(message);
        }
    }

    public static void setPiece(int row, int col, Piece piece) throws IllegalArgumentException {
        if (row < 8 && col < 8) {
            squares[row][col].setPiece(piece);
        } else {
            String message = "Columns and rows indices cannot exceed 7";
            throw new IllegalArgumentException(message);
        }
    }

    public static void setStartingPoints(Color color, Integer integer) throws Exception {
        if (startingPoints == null) {
            startingPoints = new HashMap();
        }
        startingPoints.put(color, integer);
        if (startingPoints.size() > 2) {
            throw new Exception("Too many starting points!");
        }
    }

    public static HashMap<Color, Integer> getstartingPoints() {
        return startingPoints;
    }
    private static boolean isCheck = false, 
            isMate = false, 
            isCheckBlockPossible = false;
    private static HashMap<Color, Integer> startingPoints;
    private ArrayList<Point> enemySquares, kingEscapeSquares;
    private static final Square[][] squares = new Square[8][8];
    private static Color currentColor;
    private static Color oppositeColor;
    private int sourceRow, sourceCol;
    private static Piece selectedPiece;
    private int a1,
            a0,
            b0,
            b1,
            squareSize;

}
