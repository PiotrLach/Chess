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

    public void test(Coord source, Coord kingCoord, Piece piece) {
        int row = source.row;
        int col = source.col;        
        if (!source.equals(kingCoord) && 
//            (row != kingCoord.row
//            || col != kingCoord.col) && 
            piece != null
            && piece.color != currentColor
            && piece instanceof Rook
            && piece.isCorrectMovement(source, kingCoord)) 
        {
            System.out.println("Path free");            
            System.out.println("Origin" + source + "king" + kingCoord);
        }
    }
    
    /**
     * Tests if check occurs
     *
     * @param kingRow
     * @param kingCol
     * @param findEnemySquares
     * @return check value
     */
    private boolean isCheck(Coord kingCoord, boolean findEnemySquares) {
        ArrayList<Coord> enemySquaresTemp = new ArrayList<>();
        int attackers = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece(); 
                Coord coord = new Coord(row, col);     
                
                Coord[] coords = {coord, kingCoord};
                
                if (!coord.equals(kingCoord)
                    && piece != null
                    && piece.color != currentColor
                    && piece.isCorrectMovement(coord, kingCoord)
                    && isPathFree(coords, piece)) 
                {                                                                  
                    attackers++;
                } else {
                    continue;
                }
                if (findEnemySquares) {
                    enemySquaresTemp.add(coord);
                    var coordinates = getPath(coords, piece);
                    enemySquaresTemp.addAll(coordinates);
                }
                
            }
        }       
        if (attackers == 0) {
            return false;
        } else if (attackers > 0 && findEnemySquares) {                        
            
            JOptionPane.showMessageDialog(this, "Szach!");
            
            enemySquares = new ArrayList<>(enemySquaresTemp);            
            kingEscapeSquares = new ArrayList<>(mate(kingCoord));            
            isCheckBlockPossible = isCheckBlockPossible(enemySquaresTemp);                                    
            isMate = !isCheckBlockPossible && kingEscapeSquares.isEmpty();
            
            if (isMate) {
                JOptionPane.showMessageDialog(this, "Mat!");
            }                        
        }        
        return true;
    }

    private ArrayList<Coord> mate(Coord kingCoord) {
        ArrayList<Coord> escapeSquares = new ArrayList<>();
        
        int kingRow = kingCoord.row, kingCol = kingCoord.col;                
        
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                
                if ((row <= 7 && row >= 0) && (col <= 7 && col >= 0)) {
                    
                    Square square = squares[row][col];                    
                    Piece piece = square.getPiece();                    
                    Coord coord = new Coord(row, col);
                    
                    if (piece == null || piece.color != currentColor) {
                        
                        if (!isCheck(coord, false)) {
                            
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
                    
                    Coord[] coords = {source, target};
                    
                    if (piece != null
                            && !(piece instanceof King)
                            && piece.color == currentColor
                            && piece.isCorrectMovement(source, target)
                            && isPathFree(coords, piece)) {                        
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
    private boolean isSelfMadeCheck(Coord target) {        
            
        var sourceSquare = squares[sourceRow][sourceCol];
        var targetSquare = squares[target.row][target.col];

        sourceSquare.setPiece(null);
        targetSquare.setPiece(selectedPiece);

        var isSelfMadeCheck = true;

        try {
            var kingCoord = findKing();                
            isSelfMadeCheck = isCheck(kingCoord, false);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        sourceSquare.setPiece(selectedPiece);
        targetSquare.setPiece(null);

        return isSelfMadeCheck;
    }
    
    private boolean isMoveable(Point dest, Square square, Coord target) {
        
        if (!square.contains(dest)) {
            return false;
        }
        
        Piece piece = square.getPiece();                        
        
        Coord source = new Coord(sourceRow, sourceCol);
        
        Coord[] coords = {source, target};
        
        if (selectedPiece == null) {
            var message = "Nie wybrano bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (square.isHighlighted()) {
            var message = "Nie można przenieść bierki w to samo miejsce!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (piece != null && !selectedPiece.isFoe(piece)) {
            var message = "Nie można zbić bierki własnego koloru!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        } 
        
        if (!selectedPiece.isCorrectMovement(source, target)) {
            var message = "Niepoprawny ruch dla wybranej bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (!isPathFree(coords, selectedPiece)) {
            var message = "Na drodze są inne bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }                           
        
        var isKingEscape = selectedPiece instanceof King 
            && kingEscapeSquares.contains(target);
        
        var isCheckBlock = !(selectedPiece instanceof King)                
            && enemySquares.contains(target); 
        
        if (isCheck && !isCheckBlock && !isKingEscape) {
            var message = "Szach! Trzeba mu zapobiec!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isSelfMadeCheck(target)) {
            var message = "Ruch skutkowałby szachem króla!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        return true; 
              
    }

    private void movePiece(Point dest) {        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                Square square = squares[row][col];

                Coord source = new Coord(sourceRow, sourceCol);
                Coord target = new Coord(row, col);                
                
                if (isMoveable(dest, square, target)) {
                    
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
                    int x = (int) square.getX();
                    int y = (int) square.getY();
                    Piece piece = square.getPiece();
                    piece.drawImage(graphics, x, y, squareSize, squareSize);
                }
            }
        }
    }
    private <T> T traverse(Coord[] coords, Piece foe, T t, Fun<Coord, T> fun) {

        if (!(foe instanceof Knight)) {
            Coord source = coords[0];
            Coord target = coords[1];

            var isSameRow = source.row == target.row;
            var isSameCol = source.col == target.col;
            var isTargetRowLower = source.row < target.row;
            var isTargetColLower = source.col < target.col;

            int vDiff, hDiff; // vertical and horizontal difference

            vDiff = isSameRow ? 0 : (isTargetRowLower ? 1 : -1);
            hDiff = isSameCol ? 0 : (isTargetColLower ? 1 : -1);

            int row = source.row + vDiff;
            int col = source.col + hDiff;
            var coord = new Coord(row, col);

            for (; !coord.equals(target); row += vDiff, col += hDiff) {

                t = fun.perform(coord, t);
                
                coord = new Coord(row, col);
            }
        }
        return t;
    }
    /**
     * Retrieves a list of coordinates between source and target coords
     * @param coords
     * @param foe
     */
    private ArrayList<Coord> getPath(Coord[] coords, Piece foe) {          
        Fun<Coord, ArrayList<Coord>> fun = (coord, path) -> {
            path.add(coord);
            return path;
        };
        
        ArrayList<Coord> path = new ArrayList<>();
                        
        return traverse(coords, foe, path, fun);
    }
    /**
     * Checks if there are pieces on the path between source and target coords
     * @param coords
     * @param foe
     */
    private boolean isPathFree(Coord[] coords, Piece foe) {               
        Fun<Coord, Integer> fun = (coord, nullCount) -> {
            
            Square square = squares[coord.row][coord.col];
            Piece piece = square.getPiece();
            
            nullCount += piece == null ? 0 : 1; 
            
            return nullCount;
        };
        
        Integer nullCount = 0;

        return traverse(coords, foe, nullCount, fun) == 0;
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
    @FunctionalInterface
    private interface Fun <T, R> {
        R perform(T arg1, R arg2);
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
    private ArrayList<Coord> enemySquares = new ArrayList<>();
    private ArrayList<Coord> kingEscapeSquares = new ArrayList<>();
    private static final Square[][] squares = new Square[8][8];
    private static Color currentColor;
    private static Color oppositeColor;
    private int sourceRow, sourceCol;
    private static Piece selectedPiece;
    private int squareSize;

}
