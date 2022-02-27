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

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.Getter;
import my.chess.pieces.Piece;

/**
 *
 * @author Piotr Lach
 */
public class Board extends JPanel {

    @Getter
    private final Deque<Move> moves = new LinkedList<>();
    @Getter
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("my/chess/Bundle");
    @Getter
    private final List<Square> squares = new ArrayList<>();
    private final List<Drawable> drawables = new ArrayList<>();
    private final Logic logic = new Logic(this, squares);
    private Optional<Square> optionalSourceSquare = Optional.empty();
    private int squareSize = 100;
    private final Save save = new Save(this, squares);

    public Board() {
        setListeners();
        createSquares();
        setDefaultGame();
    }

    private void setListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                resizeBoard();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                chooseOrMove(evt);
            }
        });
    }

    private void createSquares() {
        squareSize = 100;
        int squareCounter = 0;
        int indexCounter = 0;

        for (int y = 1000; y > 0; y -= squareSize) {
            for (int x = 0; x < 1000; x += squareSize) {

                Drawable drawable;
                if (isInBorder(x, y)) {
                    drawable = new Index(x, y, squareSize, indexCounter);
                    indexCounter++;
                } else {
                    var coord = new Coord(squareCounter);
                    var square = new Square(x, y, squareSize, coord);
                    squares.add(square);
                    drawable = square;
                    squareCounter++;
                }
                drawables.add(drawable);
            }
        }
    }

    private boolean isInBorder(int x, int y) {
        return x == 0 || y == 1000 || x == 900 || y == 100;
    }

    public int showPromoteDialog() {

        var parentComponent = this;
        var message = resourceBundle.getString("Board.PromoteMessage");
        var title = resourceBundle.getString("Board.PromoteMessageTitle");
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType = JOptionPane.INFORMATION_MESSAGE;
        Icon icon = null;
        String[] options = {
                resourceBundle.getString("Board.QueenName"),
                resourceBundle.getString("Board.KnightName"),
                resourceBundle.getString("Board.RookName"),
                resourceBundle.getString("Board.BishopName")
        };
        var initialValue = resourceBundle.getString("Board.QueenName");

        int choice = JOptionPane.showOptionDialog(parentComponent,
                message,
                title,
                optionType,
                messageType,
                icon,
                options,
                initialValue
        );
        return choice;
    }

    public void displayMessage(Message message) {
        var messageText = resourceBundle.getString(message.key);
        JOptionPane.showMessageDialog(this, messageText);
    }

    public final void setDefaultGame() {

        String[] layout = {
            "R;B","N;B","B;B","Q;B","K;B","B;B","N;B","R;B",
            "H;B","H;B","H;B","H;B","H;B","H;B","H;B","H;B",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            " ; "," ; "," ; "," ; "," ; "," ; "," ; "," ; ",
            "L;W","L;W","L;W","L;W","L;W","L;W","L;W","L;W",
            "R;W","N;W","B;W","Q;W","K;W","B;W","N;W","R;W"
        };

        setGame(layout);
    }


    public void setGame(String[] layout) {

        logic.setLayout(layout);

        repaint();
    }

    /**
     * Finds the square clicked on with the LMB and sets it as either
     * source or target, depending on the piece it contains.
     */
    private void chooseOrMove(MouseEvent mouseEvent) {

        var point = mouseEvent.getPoint();
        var optional = squares.stream()
                .filter(square -> square.contains(point))
                .findAny();
        if (optional.isEmpty()) {
            return;
        }

        var selectedSquare = optional.get();

        if (isValidSource(selectedSquare)) {

            selectedSquare.setHighlighted(true);
            optionalSourceSquare = Optional.of(selectedSquare);

        } else if (isValidTarget(selectedSquare)) {

            var source = optionalSourceSquare.get();
            var target = selectedSquare;

            var selectedPiece = source.getPiece();
            selectedPiece.move(source, target);
        }

        repaint();
    }

    private boolean isValidSource(Square square) {

        var piece = square.getPiece();

        if (piece.isFoe(logic.getCurrentColor())) {
            return false;
        }

        if (square.isHighlighted()) {
            return false;
        }

        if (optionalSourceSquare.isPresent()) {
            optionalSourceSquare.get().setHighlighted(false);
        }

        return true;
    }

    private boolean isValidTarget(Square square) {

        if (square.isHighlighted()) {
            return false;
        }

        if (optionalSourceSquare.isEmpty()) {
            displayMessage(Message.noSelectedPiece);
            return false;
        }

        return true;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        for (var drawable : drawables) {
            drawable.draw(graphics);
        }

    }

    /**
     * Resets the size and location for each square, so that board
     * scales with window.
     */
    private void resizeBoard() {

        var dimensions = recalculateDimensions();

        int minHeight = dimensions.get(0) - squareSize;
        int maxHeight = dimensions.get(1) - squareSize;
        int minWidth  = dimensions.get(2);
        int maxWidth  = dimensions.get(3);

        int index = 0;

        for (int y = maxHeight; y > minHeight; y -= squareSize) {
            for (int x = minWidth; x < maxWidth; x += squareSize) {

                var drawable = drawables.get(index);

                drawable.setPosition(x, y);
                drawable.setDimension(squareSize);

                index++;
            }
        }
        repaint();
    }

    /**
     * Calculates minimal and maximal height and width (start point and end point)
     * for the board, such that when applied, it remains a square, centered in the window.
     * @return list of dimensions
     */
    private List<Integer> recalculateDimensions() {

        int width = getWidth(), height = getHeight();

        int dim1 = Math.min(width, height);
        int dim2 = Math.max(width, height);

        while (dim1 % 10 != 0) {
            dim1--;
        }

        squareSize = dim1 / 10;

        int minDim1 = 0;
        int maxDim1 = dim1;
        int minDim2 = (dim2 - dim1) / 2;
        int maxDim2 = minDim2 + dim1;

        if (height < width) {
            return List.of(minDim1, maxDim1, minDim2, maxDim2);
        } else {
            return List.of(minDim2, maxDim2, minDim1, maxDim1);
        }
    }

    public Optional<Move> getLastMove() {
        return Optional.ofNullable(moves.peekLast());
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void addMove(Square source, Square target) {
        var move = new Move(source.coord, target.coord);
        moves.add(move);
    }

    public void addMove(Square source, Square target, Piece piece) {
        var move = new Move(source.coord, target.coord, piece);
        moves.add(move);
    }

    public Square getSquare(Coord coord) {
        return squares.get(coord.index);
    }

    public Square getSquare(int row, int col) {
        var coord = new Coord(row, col);
        return squares.get(coord.index);
    }

    public void setOptionalSourceEmpty() {
        optionalSourceSquare = Optional.empty();
    }

    public void movePiece(int row1, int col1, int row2, int col2) {
        logic.movePiece(row1, col1, row2, col2);
    }

    public void setPiece(Coord coord, Piece piece) {
        var square = squares.get(coord.index);
        square.setPiece(piece);
    }

    public boolean isCorrectMovement(int row1, int col1, int row2, int col2) {
        return logic.isCorrectMovement(row1, col1, row2, col2);
    }

    public boolean isCorrectMovement(Coord from, Coord to) {
        return logic.isCorrectMovement(from, to);
    }

    public void clearMoves() {
        moves.clear();
    }

    public boolean isValidMove(int row1, int col1, int row2, int col2) {
        return logic.isValidMove(row1, col1, row2, col2);
    }

    public boolean isValidMove(Square source, Square target) {
        return logic.isValidMove(source, target);
    }

    public void changeCurrentColor() {
        logic.changeCurrentColor();
    }

    public boolean isAttacked(Square square) {
        return logic.isAttacked(square);
    }

    public void loadGame(String filename) {
        save.loadGame(filename);
    }

    public void saveGame(String filename) {
        save.saveGame(filename);
    }
}
