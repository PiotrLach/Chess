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
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.val;

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
                
                val idx = row * 8 + col;
                
                squares.get(idx).setLocation(y, x);
                squares.get(idx).setSize(squareSize, squareSize);
            }
        }
        repaint();
    }

    private void createSquares() { 
        squareSize = 80;
        int a1 = 640, a0 = 0, b0 = 0, b1 = 640;
        for (int x = a0, row = 0; x < a1; x += squareSize, row++) {
            for (int y = b0, col = 0; y < b1; y += squareSize, col++) {
                
                Coord coord = new Coord(row, col);                
                Square square = new Square(y, x, 80, 80, coord);
                squares.add(square);
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

    private boolean isChoosableAi(Square square, Point input) {
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
    
    private boolean isChoosable(Square square, Point input) {
        
        if (isMate) {
            var message = "Mat! Koniec gry!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (!square.contains(input)) {
            return false;
        }        
        
        Piece piece = square.getPiece();
        
        if (piece == null) {
            var message = "Pole nie zawiera bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (!(currentColor == piece.color)) {
            var message = "Nie można wybrać bierki przeciwnika!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (square.isHighlighted()) {
            var message = "Bierka jest już wybrana!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isCheck && !isCheckBlockPossible && !(piece instanceof King)) {
            var message = "Trzeba zapobiec szachowi!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        return true;
    }
    
    private void choosePiece(Point input) {                
                
        Consumer<Square> action = square -> {
            
            Piece piece = square.getPiece();
            
            if (isChoosable(square, input)) {
                if (sourceSquare != null) {
                    sourceSquare.setHighlighted(false);
                }
                square.setHighlighted(true);                    
                selectedPiece = piece;

                sourceSquare = square;

                repaint();                          
            } 
            
        };
        squares.forEach(action);
    }
    
    /**
     * Finds square holding current player's king 
     * @return king's row and col coordinates
     * @throws Exception 
     */
    private Square findKing() throws Exception {     
        
        for (var square : squares) {
            
            Piece piece = square.getPiece();
            
            if (piece == null) {
                continue;
            }
            
            var isKing = piece instanceof King;
            var isCurrentColor = piece.color == currentColor;
            
            if (isKing && isCurrentColor) {
                return square;
            }
        }                                          
        throw new Exception("King has not been found.");
    }

    private void check() {        
        try {
            var kingSquare = findKing();            
            isCheck = isCheck(kingSquare, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }        
    }

    
    /**
     * Tests if check occurs
     *
     * @param kingCoord
     * @param findEnemySquares
     * @return check value
     */
    private boolean isCheck(Square kingSquare, boolean findEnemySquares) {
        ArrayList<Coord> enemySquaresTemp = new ArrayList<>();
        int attackers = 0;
        
        for (var square : squares) {
            Piece piece = square.getPiece(); 
            Coord coord = square.coord;

            Coord[] coords = {coord, kingSquare.coord};

            if (!coord.equals(kingSquare.coord)
                && piece != null
                && piece.color != currentColor
                && piece.isCorrectMovement(coord, kingSquare.coord)
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
        if (attackers == 0) {
            return false;
        } else if (attackers > 0 && findEnemySquares) {                        
            
            JOptionPane.showMessageDialog(this, "Szach!");
            
            enemySquares = new ArrayList<>(enemySquaresTemp);            
            kingEscapeSquares = findEscapeSquares(kingSquare.coord);            
            isCheckBlockPossible = isCheckBlockPossible(enemySquaresTemp);                                    
            isMate = !isCheckBlockPossible && kingEscapeSquares.isEmpty();
            
            if (isMate) {
                JOptionPane.showMessageDialog(this, "Mat!");
            }                        
        }        
        return true;
    }
    
    
    /**
     * Finds squares for where king can escape to avoid check
     * @param kingCoord
     * @return 
     */
    private ArrayList<Coord> findEscapeSquares(Coord kingCoord) {
        ArrayList<Coord> escapeSquares = new ArrayList<>();
        
        int kingRow = kingCoord.row, kingCol = kingCoord.col;                
        
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                
                Coord coord = new Coord(row, col);
                
                if (coord.isOutOfBounds()) {
                    continue;
                } 
                    
                int idx = row * 8 + col;
                    
                var square = squares.get(idx);                    
                var piece = square.getPiece();                                        
                    
                if (piece != null && piece.color == currentColor) {
                    continue;              
                }   
                
                if (!isCheck(square, false)) {
                            
                    escapeSquares.add(coord);
                }
            }
        }
        return escapeSquares;
    }

    private boolean isCheckBlockPossible(ArrayList<Coord> enemySquares) {
        for (Coord target : enemySquares) {                                    
            for (Square square : squares) {                    
                Piece piece = square.getPiece();

                Coord source = square.coord;

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
        return false;
    }
    
    /**
     * Tests if user's move will result in a check
     * @param target   
     */
    private boolean isSelfMadeCheck(Square targetSquare) {                            
        
        sourceSquare.setPiece(null);
        targetSquare.setPiece(selectedPiece);

        var isSelfMadeCheck = true;

        try {
            var kingCoord = findKing();                
            isSelfMadeCheck = isCheck(kingCoord, false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        sourceSquare.setPiece(selectedPiece);
        targetSquare.setPiece(null);

        return isSelfMadeCheck;
    }
    
    private boolean isMoveable(Point dest, Square square) {
        
        if (!square.contains(dest)) {
            return false;
        }
        
        Piece piece = square.getPiece();                        
        
        Coord source = sourceSquare.coord;
        
        Coord[] coords = {source, square.coord};
        
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
            var message = "Nie można zbić własnej bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        } 
        
        if (!selectedPiece.isCorrectMovement(source, square.coord)) {
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
            && kingEscapeSquares.contains(square.coord);
        
        var isCheckBlock = !(selectedPiece instanceof King)                
            && enemySquares.contains(square.coord); 
        
        if (isCheck && !isCheckBlock && !isKingEscape) {
            var message = "Szach! Trzeba mu zapobiec!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isSelfMadeCheck(square)) {
            var message = "Ruch skutkowałby szachem króla!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        return true;               
    }

    private void movePiece(Point dest) {        

        for (var targetSquare : squares) {                                               

            if (isMoveable(dest, targetSquare)) {

                isCheck = false;
                targetSquare.setPiece(selectedPiece);
                selectedPiece = null;                    
                sourceSquare.setPiece(null);
                sourceSquare.setHighlighted(false);

                var isWhite = currentColor == Color.WHITE;
                currentColor = isWhite ? Color.BLACK : Color.WHITE;
                oppositeColor = !isWhite ? Color.BLACK : Color.WHITE;

                repaint();                    
                check();                   

                return;                    
            } 
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        
        Consumer<Square> action = square -> {
            if (square.isHighlighted()) {
                square.highlightSquare(graphics);
            } else {
                square.draw(graphics);
            }
            if (square.getPiece() != null) {                                        
                int x = (int) square.getX();
                int y = (int) square.getY();
                Piece piece = square.getPiece();
                piece.drawImage(graphics, x, y, squareSize, squareSize);
            }
        };
        
        squares.forEach(action);
                                                    
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
            
            int idx = coord.row * 8 + coord.col;
            
            Square square = squares.get(idx);
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
            var topPawn = new Pawn(color1, Piece.PieceName.Pawn1);
            var bottomPawn = new Pawn(color2, Piece.PieceName.Pawn6);
            
            squares.get(1 * 8 + col).setPiece(topPawn);
            squares.get(6 * 8 + col).setPiece(bottomPawn);
        }
        
        for (int row = 0; row <= 7; row += 7) {
            
            Color color = row == 0 ? color1 : color2;
            
            val idx = row * 8;
            
            squares.get(idx + 0).setPiece(new Rook(color));
            squares.get(idx + 1).setPiece(new Knight(color));
            squares.get(idx + 2).setPiece(new Bishop(color));
            squares.get(idx + 3).setPiece(new Queen(color));
            squares.get(idx + 4).setPiece(new King(color));
            squares.get(idx + 5).setPiece(new Bishop(color));
            squares.get(idx + 6).setPiece(new Knight(color));
            squares.get(idx + 7).setPiece(new Rook(color));
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
        
        Consumer<Square> action = square -> {
            square.setPiece(null);
        };
        
        squares.forEach(action);
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
            int idx = row * 8 + col;
            return squares.get(idx);
        } else {
            String message = "Columns and rows indices cannot exceed 7";
            throw new IllegalArgumentException(message);
        }
    }

    public static void setPiece(int row, int col, Piece piece) throws IllegalArgumentException {
        if (row < 8 && col < 8) {
            int idx = row * 8 + col;
            var square = squares.get(idx);
            square.setPiece(piece);
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
    private static final ArrayList<Square> squares = new ArrayList<>();
    private static Color currentColor;
    private static Color oppositeColor;
    private Square sourceSquare;
    private static Piece selectedPiece;
    private int squareSize;

}
