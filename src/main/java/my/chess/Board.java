/* 
 * Java chess game implementation
 * Copyright (C) 2021 Piotr Lach
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
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
        
        var point = mouseEvent.getPoint();        
        
        switch (mouseEvent.getButton()) {
            
            case MouseEvent.BUTTON3 -> choosePiece(point);            
            
            case MouseEvent.BUTTON1 -> movePiece(point);            
            
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
        
        if (!square.contains(input)) {
            return false;
        }        
        
        if (isMate) {
            var message = "Mat! Koniec gry!";
            JOptionPane.showMessageDialog(this, message);
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
                
        for (var square : squares) {            
            Piece piece = square.getPiece();
            
            if (isChoosable(square, input)) {                
                sourceSquare.setHighlighted(false);                
                square.setHighlighted(true);                    
                selectedPiece = piece;

                sourceSquare = square;

                repaint();                          
            } 
            
        }       
    }
    
    /**
     * Finds square holding current player's king 
     * @return square holding current player's king
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

    private void checkAndMate() {        
        try {
            
            var kingSquare = findKing();            
            isCheck = isCheck(kingSquare);
            
            if (isCheck) {
                
                JOptionPane.showMessageDialog(this, "Szach!");
    
                enemySquares = findEnemySquares(kingSquare);            
                kingEscapeSquares = findEscapeSquares(kingSquare);            
                isCheckBlockPossible = isCheckBlockPossible(enemySquares);                                    
                isMate = !isCheckBlockPossible && kingEscapeSquares.isEmpty();        
            }
            
            if (isMate) {
                JOptionPane.showMessageDialog(this, "Mat!");
            }   
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }        
    }
    
    /**
     * Tests if check occurs
     *
     * @param kingSquare     
     * @return check value
     */
    private boolean isCheck(Square kingSquare) {
        return findEnemySquares(kingSquare).size() > 0;
    }
        
    private ArrayList<Square> findEnemySquares(Square kingSquare) {
        ArrayList<Square> enemySquaresTemp = new ArrayList<>();        
        
        for (var source : squares) {
            Piece piece = source.getPiece();             

            if (!source.coord.equals(kingSquare.coord)
                && piece != null
                && piece.color != currentColor
                && piece.isCorrectMovement(source, kingSquare)
                && isPathFree(source, kingSquare)) 
            {                                                                  
                enemySquaresTemp.add(source);
                var path = getPath(source, kingSquare);
                enemySquaresTemp.addAll(path);
            }                                        
        }      
        return enemySquaresTemp;                       
    }
        
    /**
     * Finds squares for where king can escape to avoid check
     * @param kingSquare
     * @return list of escape squares
     */
    private ArrayList<Square> findEscapeSquares(Square kingSquare) {
        ArrayList<Square> escapeSquares = new ArrayList<>();
        
        int kingRow = kingSquare.coord.row, kingCol = kingSquare.coord.col;                
        
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                
                Coord coord = new Coord(row, col);
                
                if (coord.isOutOfBounds()) {
                    continue;
                } 
                                       
                var square = squares.get(coord.index);                    
                var piece = square.getPiece();                                        
                    
                if (piece != null && piece.color == currentColor) {
                    continue;              
                }   
                
                if (!isCheck(square)) {                            
                    escapeSquares.add(square);
                }
            }
        }
        return escapeSquares;
    }

    private boolean isCheckBlockPossible(ArrayList<Square> enemySquares) {
        for (Square target : enemySquares) {                                    
            for (Square source : squares) {                    
                Piece piece = source.getPiece();                

                if (piece != null
                    && !(piece instanceof King)
                    && piece.color == currentColor
                    && piece.isCorrectMovement(source, target)
                    && isPathFree(source, target)) 
                {                        
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
            isSelfMadeCheck = isCheck(kingCoord);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        sourceSquare.setPiece(selectedPiece);
        targetSquare.setPiece(null);

        return isSelfMadeCheck;
    }
    
    private boolean isMoveable(Point dest, Square targetSquare) {
        
        if (!targetSquare.contains(dest)) {
            return false;
        }
                                                             
        if (selectedPiece == null) {
            var message = "Nie wybrano bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (targetSquare.isHighlighted()) {
            var message = "Nie można przenieść bierki w to samo miejsce!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        Piece piece = targetSquare.getPiece();   
        
        if (targetSquare.getPiece() != null && !selectedPiece.isFoe(piece)) {
            var message = "Nie można zbić własnej bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        } 
        
        if (!selectedPiece.isCorrectMovement(sourceSquare, targetSquare)) {
            var message = "Niepoprawny ruch dla wybranej bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (!isPathFree(sourceSquare, targetSquare)) {
            var message = "Na drodze są inne bierki!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }                           
        
        var isKingEscape = selectedPiece instanceof King 
            && kingEscapeSquares.contains(targetSquare);
        
        var isCheckBlock = !(selectedPiece instanceof King)                
            && enemySquares.contains(targetSquare); 
        
        if (isCheck && !isCheckBlock && !isKingEscape) {
            var message = "Szach! Trzeba mu zapobiec!";
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isSelfMadeCheck(targetSquare)) {
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

                repaint();                    
                checkAndMate();                   

                return;                    
            } 
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        
        for (var square : squares) {
            
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
        }                
                                                    
    }
   
    private <T> T traverse(Square source, Square target, T t, Fun<Coord, T> fun) {

        if (!(source.getPiece() instanceof Knight)) {

            var isTargetSameRow = source.coord.row == target.coord.row;
            var isTargetSameCol = source.coord.col == target.coord.col;
            var isTargetRowLower = source.coord.row < target.coord.row;
            var isTargetColLower = source.coord.col < target.coord.col;

            int vDiff, hDiff; // vertical and horizontal difference

            vDiff = isTargetSameRow ? 0 : (isTargetRowLower ? 1 : -1);
            hDiff = isTargetSameCol ? 0 : (isTargetColLower ? 1 : -1);

            int row = source.coord.row + vDiff;
            int col = source.coord.col + hDiff;
            var coord = new Coord(row, col);

            for (; !coord.equals(target.coord); row += vDiff, col += hDiff) {

                t = fun.perform(coord, t);
                
                coord = new Coord(row, col);
            }
        }
        return t;
    }
    
    /**
     * Retrieves a list of squares in straight line between source 
     * and target squares
     * @param source
     * @param target
     * @param foe
     */    
    private ArrayList<Square> getPath(Square source, Square target) {          
        Fun<Coord, ArrayList<Square>> fun = (coord, path) -> {                        
                        
            path.add(squares.get(coord.index));
            
            return path;
        };
        
        ArrayList<Square> path = new ArrayList<>();
                        
        return traverse(source, target, path, fun);
    }
    
    /**
     * Checks if there are pieces on the path between source and target squares
     * @param source
     * @param target
     * @param foe
     */
    private boolean isPathFree(Square source, Square target) {               
        Fun<Coord, Integer> fun = (coord, nullCount) -> {
                                    
            Square square = squares.get(coord.index);
            Piece piece = square.getPiece();
            
            nullCount += piece == null ? 0 : 1; 
            
            return nullCount;
        };
        
        Integer nullCount = 0;

        return traverse(source, target, nullCount, fun) == 0;
    }

    public void setNewGame() throws IOException {
        
        clearBoard();
        
        int draw = new Random().nextInt(2);       
        
        Color color1 = draw == 1 ? Color.BLACK : Color.WHITE;
        Color color2 = color1 == Color.BLACK ? Color.WHITE : Color.BLACK;
               
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
        isCheck = false;
        isMate = false;
        isCheckBlockPossible = false;
        selectedPiece = null;
        
        for (var square : squares) {
            square.setPiece(null);
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

    public static Square getSquare(Coord coord) throws IllegalArgumentException {
        if (!coord.isOutOfBounds()) {            
            return squares.get(coord.index);
        } else {
            String message = "Columns and rows indices cannot exceed 7";
            throw new IllegalArgumentException(message);
        }
    }

    public static void setPiece(Coord coord, Piece piece) throws IllegalArgumentException {
        if (!coord.isOutOfBounds()) {            
            var square = squares.get(coord.index);
            square.setPiece(piece);
        } else {
            String message = "Columns and rows indices cannot exceed 7";
            throw new IllegalArgumentException(message);
        }
    }

    private static boolean isCheck = false; 
    private static boolean isMate = false; 
    private static boolean isCheckBlockPossible = false;
    private ArrayList<Square> enemySquares = new ArrayList<>();
    private ArrayList<Square> kingEscapeSquares = new ArrayList<>();
    private static final ArrayList<Square> squares = new ArrayList<>();
    private static Color currentColor;
    private Square sourceSquare = new Square(0, 0, 0, 0, new Coord(-1, -1));;
    private static Piece selectedPiece;
    private int squareSize = 80;

}
