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
        createSquares();
    }
    /**
     * Recalculates the size and location for each square, 
     * so that the board scales with window
     */
    public void recalculateSize() {
        int height = getHeight(), width = getWidth();
        int a1, a0, b0, b1;
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
        squareSize = 80;
        int a1 = 640, a0 = 0, b0 = 0, b1 = 640;
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
                    
                    var isWhite = currentColor == Color.WHITE;
                    var colorName = isWhite ? "białych" : "czarnych";
                    var message = "Teraz ruch " + colorName + "!";
                    
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
    private Coord findKing() throws Exception {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col].getPiece();
                if (piece instanceof King && piece.color == currentColor) {
                    var kingCoord = new Coord(row, col);
                    return kingCoord;
                }
            }
        }
        throw new Exception("King has not been found.");
    }

    private void check() {        
        try {
            var kingCoord = findKing();            
            isCheck = isCheck(kingCoord, true);
        } catch (Exception exception) {
            System.out.println(exception);
        }        
    }

    public void test(Coord origin, Coord kingCoord, Piece piece) {
        int row = origin.row;
        int col = origin.col;
        if (row != kingCoord.row
        && col != kingCoord.col
        && piece != null
        && piece.color != currentColor
        && piece.isCorrectMovement(origin, kingCoord)) {
            System.out.println("Path free");
            System.out.println(isPathFree(origin, kingCoord));
            System.out.println("Correct movement");
            System.out.println(piece.isCorrectMovement(origin, kingCoord));
        }
    }
    
    /**
     * Tests if check occurs
     *
     * @param kingRow
     * @param kingCol
     * @param separate
     * @return check value
     */
    private boolean isCheck(Coord kingCoord, boolean separate) {
        ArrayList<Coord> enemySquaresTemp = new ArrayList();
        int sum = 0;
        boolean isKnight;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece(); 
                Coord origin = new Coord(row, col);
                
//                test(origin, kingCoord, piece);
                
                if (row != kingCoord.row
                    && col != kingCoord.col
                    && piece != null
                    && piece.color != currentColor
                    && piece.isCorrectMovement(origin, kingCoord)
                    && (isKnight = piece instanceof Knight 
                        || isPathFree(origin, kingCoord))) 
                { // ???                          
                    if (!isKnight && separate) {
                        enemySquaresTemp.add(new Coord(row, col));
                        var coordinates = getPath(origin, kingCoord);
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
            kingEscapeSquares = new ArrayList(mate(kingCoord));
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

    private ArrayList<Coord> mate(Coord kingCoord) {
        ArrayList<Coord> escapeSquares = new ArrayList();
        
        int kingRow = kingCoord.row, kingCol = kingCoord.col;                
        
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                
                if ((row <= 7 && row >= 0) && (col <= 7 && col >= 0)) {
                    
                    Square square = squares[row][col];                    
                    Piece piece = square.getPiece();
                    
                    Coord coord = new Coord(row, col);
                    
                    if (piece == null || piece.color != currentColor) {
                        
                        if (!isCheck(kingCoord, false)) {
                            
                            escapeSquares.add(coord);
                        }
                    }
                }
            }
        }
        return escapeSquares;
    }

    private boolean isCheckBlockPossible(ArrayList<Coord> enemySquares) {
        for (Coord target : enemySquares) {                        
            
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square square = squares[row][col];
                    Piece piece = square.getPiece();
                    
                    Coord source = new Coord(row, col);
                    
                    if (piece != null
                            && !(piece instanceof King)
                            && piece.color == currentColor
                            && piece.isCorrectMovement(source, target)
                            && isPathFree(source, target)) {
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
     * @param col     
     */
    private boolean isSelfMadeCheck(int row, int col) {
        if (!isCheck) {
            var sourceSquare = squares[sourceRow][sourceCol];
            var targetSquare = squares[row][col];
            sourceSquare.setPiece(null);
            targetSquare.setPiece(selectedPiece);
            int sum = 0;
            try {
                var kingCoord = findKing();                
                sum += !isCheck(kingCoord, false) ? 0 : 1;
            } catch (Exception e) {
                System.out.println(e);
            }
            if (sum > 0) {
                var message = "Ruch niedozwolony: skutkowałby szachem króla!\n";
                JOptionPane.showMessageDialog(this, message);
            }
            sourceSquare.setPiece(selectedPiece);
            targetSquare.setPiece(null);
            return sum > 0;
        } else {
            return false;
        }
    }
    
    private boolean isMoveable(Point dest, Square square, int row, int col) {
        Piece piece = square.getPiece();
        Point current = new Point(row, col);
        
        var isKingEscape = isCheck 
                && selectedPiece instanceof King 
                && kingEscapeSquares.contains(current);
        
        var isCheckBlock = isCheck 
                && !(selectedPiece instanceof King)                
                && enemySquares.contains(current); 
        
        Coord source = new Coord(sourceRow, sourceCol);
        Coord target = new Coord(row, col);
        
        return selectedPiece != null
            && square.contains(dest)
            && !square.isHighlighted()
            && (piece == null || selectedPiece.isFoe(piece))
            && selectedPiece.isCorrectMovement(source, target)
            && isPathFree(source, target)
            && !isSelfMadeCheck(row, col)
            && (!isCheck || isCheckBlock || isKingEscape);        
    }

    private void movePiece(Point dest) {        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                Square square = squares[row][col];

                Coord source = new Coord(sourceRow, sourceCol);
                Coord target = new Coord(row, col);                
                
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
                        && !selectedPiece.isCorrectMovement(source, target)) {
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
     * Retrieves a list of coordinates between source and target
     * @param source
     * @param target
     */
    private ArrayList<Coord> getPath(Coord source, Coord target) {  
        
        ArrayList<Coord> coords = new ArrayList();
                           
        var isSameRow = source.row == target.row;
        var isSameCol = source.col == target.col;
        var isTargetRowLower = source.row < target.row;
        var isTargetColLower = source.col < target.col;
        
        int vDiff, hDiff; // vertical and horizontal difference

        vDiff = isSameRow ? 0 : (isTargetRowLower ? 1 : -1);
        hDiff = isSameCol ? 0 : (isTargetColLower ? 1 : -1);   

        int row = source.row + vDiff;
        int col = source.col + hDiff;
        var isTargetReached = false;

        for (; isTargetReached; row += vDiff, col += hDiff) {           

            var pathCoord = new Coord(row, col);
            coords.add(pathCoord);
            
            isTargetReached = row != target.row || col != target.col;
        }        
        return coords;
    }
    /**
     * Checks if there are pieces on the path between source and target coords
     * @param source
     * @param target
     */
    private boolean isPathFree(Coord source, Coord target) {       
        int vDiff, hDiff; // vertical and horizontal difference
        int notNullCount = 0;
        
        if (!(selectedPiece instanceof Knight)) { 
            
            var isSameRow = source.row == target.row;
            var isSameCol = source.col == target.col;
            var isTargetRowLower = source.row < target.row;
            var isTargetColLower = source.col < target.col;
            
            vDiff = isSameRow ? 0 : (isTargetRowLower ? 1 : -1);
            hDiff = isSameCol ? 0 : (isTargetColLower ? 1 : -1);   
                        
            int row = source.row + vDiff;
            int col = source.col + hDiff;
            var isTargetReached = false;
            
            for (; isTargetReached; row += vDiff, col += hDiff) {
                                                
                Piece piece = squares[row][col].getPiece();
                notNullCount += piece == null ? 0 : 1;
                
                isTargetReached = row != target.row || col != target.col;
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
        isMate = false;
        isCheckBlockPossible = false;
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
    private int squareSize;

}
