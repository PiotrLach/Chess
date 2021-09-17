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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;

/**
 *
 * @author Piotr Lach
 */
public class Board extends JPanel {

    public Board() {        
        createSquares();
        setNewGame();
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
                
                var coord = new Coord(row, col);                
                var square = new Square(y, x, 80, 80, coord);
                squares.add(square);
            }            
        }        
    }

    public void selectAndMove(MouseEvent mouseEvent) {
               
        var button = mouseEvent.getButton();        
        if (!(button == MouseEvent.BUTTON1)) {
            return;
        }
        
        var point = mouseEvent.getPoint();                                
        var optional = squares.stream()
                .filter(square -> square.contains(point))
                .findAny();        
        if (optional.isEmpty()) {
            return;
        }
        
        var square = optional.get();
        var piece = square.getPiece();  
        
        if (piece != null && !piece.isFoe(currentColor)) {
            choosePiece(point);
        } else {
            movePiece(point);
        }     
    }
   
    private boolean isChoosable(Square square, Point input) {       
        
        if (!square.contains(input)) {
            return false;
        }        
        
        if (isMate()) {
            var message = bundle.getString("Board.isMate.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        var piece = square.getPiece();
        
        if (piece == null) {
            var message = bundle.getString("Board.squareIsEmpty.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (piece.isFoe(currentColor)) {
            var message = bundle.getString("Board.enemyNotChoosable.text");            
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (square.isHighlighted()) {
            var message = bundle.getString("Board.pieceAlreadyChosen.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isCheck() && !isCheckBlockPossible() && !(piece instanceof King)) {
            var message = bundle.getString("Board.pieceGetOutOfCheck.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        return true;
    }
    
    private void choosePiece(Point input) {                
                
        for (var square : squares) {            
            
            var piece = square.getPiece();
            
            if (isChoosable(square, input)) {                
                
                sourceSquare.setHighlighted(false);                
                square.setHighlighted(true);                    
                selectedPiece = piece;

                sourceSquare = square;

                repaint();  
                return;
            } 
            
        }       
    }
    
    /**
     * Finds square holding current player's king 
     * @return square holding current player's king
     * @throws IllegalStateException 
     */
    private Square findKing() throws IllegalStateException {     
        
        for (var square : squares) {
            
            var piece = square.getPiece();
            
            if (piece == null) {
                continue;
            }
            
            var isKing = piece instanceof King;            
            
            if (isKing && !piece.isFoe(currentColor)) {
                return square;
            }
        }                                          
        throw new IllegalStateException("King has not been found.");
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
    
    private boolean isCheck() {                
        var kingSquare = findKing();
        return findEnemySquares(kingSquare).size() > 0;                                    
    }
    
    private boolean isMate() {
        
        if (!isCheck()) {
            return false;
        }
        
        var kingSquare = findKing();
        var escapeSquares = findEscapeSquares(kingSquare);
        
        return !isCheckBlockPossible() && escapeSquares.isEmpty();        
    }
    
        
    private ArrayList<Square> findEnemySquares(Square kingSquare) {
        ArrayList<Square> enemySquares = new ArrayList<>();        
        
        for (var source : squares) {
            var piece = source.getPiece();             

            if (!source.coord.equals(kingSquare.coord)
                && piece != null
                && piece.isFoe(currentColor)
                && piece.isCorrectMovement(source, kingSquare)
                && isPathFree(source, kingSquare)) 
            {                                                                  
                enemySquares.add(source);
                var path = getPath(source, kingSquare);
                enemySquares.addAll(path);
            }                                        
        }      
        return enemySquares;                       
    }
        
    /**
     * Finds squares where king can escape to avoid check
     * @param kingSquare
     * @return list of escape squares
     */
    private ArrayList<Square> findEscapeSquares(Square kingSquare) {
        ArrayList<Square> escapeSquares = new ArrayList<>();
        
        int kingRow = kingSquare.coord.row, kingCol = kingSquare.coord.col;                
        
        for (int row = kingRow - 1; row <= kingRow + 1; row++) {
            
            for (int col = kingCol - 1; col <= kingCol + 1; col++) {
                
                var coord = new Coord(row, col);
                
                if (coord.isOutOfBounds()) {
                    continue;
                } 
                                       
                var square = squares.get(coord.index);                    
                var piece = square.getPiece();                                        
                    
                if (piece != null && !piece.isFoe(currentColor)) {
                    continue;              
                }   
                
                if (!isCheck(square)) {                            
                    escapeSquares.add(square);
                }
            }
        }
        return escapeSquares;
    }

    private boolean isCheckBlockPossible() {
        
        var kingSquare = findKing();
        
        var enemySquares = findEnemySquares(kingSquare);        
        
        for (Square target : enemySquares) {
            for (Square source : squares) {                    
                var piece = source.getPiece();                

                if (piece != null
                    && !(piece instanceof King)
                    && !piece.isFoe(currentColor)
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

        var isSelfMadeCheck = isCheck();        

        sourceSquare.setPiece(selectedPiece);
        targetSquare.setPiece(null);

        return isSelfMadeCheck;
    }
    
    private boolean isPlaceable(Square target, Point dest) {
        
        if (!target.contains(dest)) {
            return false;
        }
                                                             
        if (selectedPiece == null) {
            var message = bundle.getString("Board.noSelectedPiece.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (target.isHighlighted()) {
            var message = bundle.getString("Board.sameSquareMove.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        var piece = target.getPiece();   
        
        if (target.getPiece() != null && !selectedPiece.isFoe(piece)) {
            var message = bundle.getString("Board.ownPieceCapture.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        } 
        
        if (!selectedPiece.isCorrectMovement(sourceSquare, target)) {
            var message = bundle.getString("Board.wrongMove.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (!isPathFree(sourceSquare, target)) {
            var message = bundle.getString("Board.pathBlocked.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }                           
               
        if (isCheck() && !isCheckBlock(target) && !isKingEscape(target)) {
            var message = bundle.getString("Board.pieceGetOutOfCheck.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        if (isSelfMadeCheck(target)) {
            var message = bundle.getString("Board.selfMadeCheck.text");
            JOptionPane.showMessageDialog(this, message);
            return false;
        }
        
        return true;               
    }
    
    private boolean isKingEscape(Square target) {
        var kingSquare = findKing();
        var escapeSquares = findEscapeSquares(kingSquare);
        
        return selectedPiece instanceof King && escapeSquares.contains(target);
    }
    
    private boolean isCheckBlock(Square target) {
        var kingSquare = findKing();
        var enemySquares = findEnemySquares(kingSquare);
        
        return !(selectedPiece instanceof King) && enemySquares.contains(target);
    }

    private void movePiece(Point dest) {        

        for (var square : squares) {                                               

            if (isPlaceable(square, dest)) {

                square.setPiece(selectedPiece);
                selectedPiece = null;                    
                sourceSquare.setPiece(null);
                sourceSquare.setHighlighted(false);

                var isWhite = currentColor.equals(Color.WHITE);
                currentColor = isWhite ? Color.BLACK : Color.WHITE;

                repaint();                                      

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
   
    private <T> T traverse(Square source, Square target, T t, Function<Coord, T> fun) {

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
     */    
    private ArrayList<Square> getPath(Square source, Square target) {          
        Function<Coord, ArrayList<Square>> fun = (coord, path) -> {                        
                        
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
     */
    private boolean isPathFree(Square source, Square target) {               
        Function<Coord, Integer> fun = (coord, nullCount) -> {
                                    
            Square square = squares.get(coord.index);
            Piece piece = square.getPiece();
            
            nullCount += piece == null ? 0 : 1; 
            
            return nullCount;
        };
        
        Integer nullCount = 0;

        return traverse(source, target, nullCount, fun) == 0;
    }

    public void setNewGame()  {
        
        clearBoard();
        
        int draw = new Random().nextInt(2);       
        
        Color color1 = draw == 1 ? Color.BLACK : Color.WHITE;
        Color color2 = color1.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
               
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

    public void clearBoard() {
        currentColor = Color.WHITE;
        selectedPiece = null;
        
        for (var square : squares) {
            square.setPiece(null);
            square.setHighlighted(false);
        }                
    }
                      
    @FunctionalInterface
    private interface Function <T, R> {
        R perform(T arg1, R arg2);
    }
    
    public void setPiece(@NonNull Coord coord, @NonNull Piece piece) {                
        var square = squares.get(coord.index);        
        piece.setImage();        
        square.setPiece(piece);
    }
    
    private final ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle");
    @Getter
    private final ArrayList<Square> squares = new ArrayList<>();
    @Getter
    @Setter
    private Color currentColor = Color.WHITE;
    private Square sourceSquare = new Square(0, 0, 0, 0, new Coord(-1, -1));
    private Piece selectedPiece;
    private int squareSize = 80;

}
