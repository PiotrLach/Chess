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
        beginHeight = 640;
        endHeight = 0;
        beginWidth = 0;
        endWidth = 640;
        diffHorizontal = 80;
        diffVertical = 80;
        createFields();
    }

    public void calculateSize() {
        int height = getHeight(),
                width = getWidth();
        beginHeight = (int) height;
        endHeight = (int) 0;
        while ((beginHeight - endHeight) % 8 != 0) {
            beginHeight--;
        }
        diffVertical = (beginHeight - endHeight) / 8;
        beginHeight -= diffVertical;
        endHeight -= diffVertical;
        beginWidth = (int) (width - height) / 2;
        endWidth = (int) beginWidth + height;
        while ((endWidth - beginWidth) % 8 != 0) {
            endWidth--;
        }
        diffHorizontal = (endWidth - beginWidth) / 8;
        int row = 0, col = 0;
        for (int x = beginHeight; x > endHeight; x -= diffVertical) {
            for (int y = beginWidth; y < endWidth; y += diffHorizontal) {
                squares[row][col].setLocation(y, x);
                squares[row][col].setSize(diffHorizontal, diffVertical);
                col++;
            }
            col = 0;
            row = row == 8 ? 0 : ++row;
        }
        repaint();
    }

    private void createFields() {
        int row = 0, col = 0;
        for (int x = beginHeight; x > endHeight; x -= diffVertical) {
            for (int y = beginWidth; y < endWidth; y += diffHorizontal) {
                Square square = new Square(y, x, 80, 80);
                squares[row][col] = square;
                col++;
            }
            col = 0;
            row = row == 8 ? 0 : ++row;
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

    private void choosePiece(Point point) {
        loop:
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece();
                if (!mate
                        && square.contains(point)
                        && !square.isHighlighted()
                        && piece != null
                        && currentColor == piece.color
                        && (!check
                        || (check && !piecesToBlockCheckUnavailable)
                        || (check && piece instanceof King && piecesToBlockCheckUnavailable))) {
                    squares[sourceRow][sourceCol].setHighlighted(false);
                    square.setHighlighted(true);
                    selectedPiece = piece;
                    sourceRow = row;
                    sourceCol = col;
                    repaint();
                    break loop;
                } else if (square.contains(point) && piece != null
                        && currentColor != piece.color) {
                    String color = currentColor == Color.WHITE ? "białych" : "czarnych";
                    JOptionPane.showMessageDialog(this, "Teraz ruch " + color + "!");
                    break loop;
                }
            }
        }
    }

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
        check = check(kingX, kingY, true);
    }

    /**
     * Tests if check occurs
     *
     * @param kingX
     * @param kingY
     * @param separate
     * @return check value
     */
    private boolean check(int kingX, int kingY, boolean separate) {
        ArrayList<Point> blockSquaresTemp = new ArrayList();
        int sum = 0;
        boolean isKnight;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[row][col];
                Piece piece = square.getPiece();                
                if (row != kingX && col != kingY) {                    
                    if (piece != null
                            && piece.color != currentColor
                            && piece.isCorrectMovement(row, col, kingX, kingY)
//                            && pathIsFree(i, j, kingX, kingY) ) 
                            && (isKnight = piece instanceof Knight
                            || isPathFree(row, col, kingX, kingY))) 
                    { // ???                        
                        if (!isKnight && separate) {
                            blockSquaresTemp.add(new Point(row, col));
                            blockSquaresTemp.addAll(makePath(row, col, kingX, kingY));
                        }
                        sum++;
                    }
                }
            }
        }
        if (separate) {
            blockSquares = new ArrayList(blockSquaresTemp);
        }
        if (sum == 0) {
            return false;
        } else if (sum > 0 && separate) {
            JOptionPane.showMessageDialog(this, "Szach!");
            kingEscapeSquares = new ArrayList(mate(kingX, kingY));
            System.out.println(kingEscapeSquares);
            if (piecesToBlockCheckUnavailable = !piecesToBlockCheckAvailable(blockSquaresTemp)) {
                if (mate = kingEscapeSquares.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mat!");
                }
            }
        }
        return true;
    }

    private ArrayList<Point> mate(int x, int y) {
        ArrayList<Point> escapeSquares = new ArrayList();
        for (int row = x - 1; row <= x + 1; row++) {
            for (int col = y - 1; col <= y + 1; col++) {
                if ((row <= 7 && row >= 0) && (col <= 7 && col >= 0)) {
                    Piece piece = squares[row][col].getPiece();
                    if (piece == null || piece.color != currentColor) {
                        System.out.println(row + " " + col);
                        if (!check(row, col, false)) {
                            escapeSquares.add(new Point(row, col));
                        }
                    }
                }
            }
        }
        return escapeSquares;
    }

    private boolean piecesToBlockCheckAvailable(ArrayList<Point> blockSquares) {
        for (Point point : blockSquares) {
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
        if (!check) {
            Square source = squares[sourceRow][sourceCol];
            Square target = squares[row][column];
            source.setPiece(null);
            target.setPiece(selectedPiece);
            int sum = 0;
            try {
                Point point = findKing();
                int x = (int) point.getX(), y = (int) point.getY();
                sum += !check(x, y, false) ? 0 : 1;
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
    
    private boolean isAcceptableMove(Point dest, Square square, int row, int col) {
        Piece piece = square.getPiece();
        Point current = new Point(row, col);
        
        var kingEscape = check 
                && selectedPiece instanceof King 
                && kingEscapeSquares.contains(current);
        
        var checkBlock = check 
                && !(selectedPiece instanceof King)                
                && blockSquares.contains(current);       
        
        return selectedPiece != null
            && square.contains(dest)
            && !square.isHighlighted()
            && (piece == null || selectedPiece.isFoe(piece))
            && selectedPiece.isCorrectMovement(sourceRow, sourceCol, row, col)
            && isPathFree(sourceRow, sourceCol, row, col)
            && !isSelfMadeCheck(row, col)
            && (!check || checkBlock || kingEscape);        
    }

    private void movePiece(Point dest) {
        loop:
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                
                Square square = squares[row][col];                
                
                if (isAcceptableMove(dest, square, row, col)) {
                    
                    check = false;
                    square.setPiece(selectedPiece);
                    selectedPiece = null;
                    squares[sourceRow][sourceCol].setPiece(null);
                    squares[sourceRow][sourceCol].setHighlighted(false);
                    var isWhite = currentColor == Color.WHITE;
                    currentColor = isWhite ? Color.BLACK : Color.WHITE;
                    oppositeColor = isWhite ? Color.BLACK : Color.WHITE;
                    repaint();                    
                    check();                   
                    break loop;
                    
                } else if (selectedPiece != null
                        && squares[row][col].contains(dest)
                        && !selectedPiece.isCorrectMovement(sourceRow, sourceCol, row, col)) {
                    JOptionPane.showMessageDialog(this, "Ruch niedozwolony!\n");
                    break loop;
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
                            diffHorizontal,
                            diffVertical
                    );
                }
            }
        }
    }

    private ArrayList<Point> makePath(int x1, int y1, int x2, int y2) {
        ArrayList<Point> pathTemp = new ArrayList();
        int verticalDifference, horizontalDifference;
        verticalDifference = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
        horizontalDifference = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
        x1 += verticalDifference;
        y1 += horizontalDifference;
        for (int i = x1, j = y1; i != x2 || j != y2; i += verticalDifference, j += horizontalDifference) {
            pathTemp.add(new Point(i, j));
        }
        return pathTemp;
    }
      
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
        check = false;
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
    private static boolean check = false, mate = false, piecesToBlockCheckUnavailable = false;
    private static HashMap<Color, Integer> startingPoints;
    private ArrayList<Point> blockSquares, kingEscapeSquares;
    private static final Square[][] squares = new Square[8][8];
    private static Color currentColor;
    private static Color oppositeColor;
    private int sourceRow, sourceCol;
    private static Piece selectedPiece;
    private int beginHeight,
            endHeight,
            beginWidth,
            endWidth,
            diffHorizontal,
            diffVertical;

}
