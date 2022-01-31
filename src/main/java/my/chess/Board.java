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

import my.chess.pieces.Knight;
import my.chess.pieces.King;
import my.chess.pieces.Piece;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.Getter;
import my.chess.pieces.Empty;
import my.chess.pieces.PieceFactory;

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
    private Color currentColor = Color.WHITE;
    private Optional<Square> optionalSourceSquare = Optional.empty();
    private int squareSize = 100;
    private final PieceFactory pieceFactory = new PieceFactory(this);

    public Board() {
        createSquares();
        setDefaultGame();

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

    public void setGame(String[] layout) {

        clearBoard();

        setLayout(layout);

        repaint();
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

    private void setLayout(String[] layout) {

        if (layout.length != 64) {
            throw new IllegalArgumentException("Layout lacks squares!");
        }

        for (int row1 = 7, row2 = 0; row1 >= 0; row1--, row2++) {
            for (int col = 0; col < 8; col++) {

                var layoutCoord = new Coord(row1, col);

                var string = layout[layoutCoord.index];
                var piece = pieceFactory.getPiece(string);

                var squareCoord = new Coord(row2, col);

                var square = squares.get(squareCoord.index);
                square.setPiece(piece);
            }
        }
    }

    private void clearBoard() {
        currentColor = Color.WHITE;
        optionalSourceSquare = Optional.empty();

        for (var square : squares) {
            square.setPiece(Empty.INSTANCE);
            square.setHighlighted(false);
        }

        moves.clear();
    }

    public void loadGame(Deque<Move> moves) {

        setDefaultGame();

        for (var move : moves) {
            Coord from = move.source;
            Coord to = move.target;

            Square source = squares.get(from.index);
            Square target = squares.get(to.index);

            Piece piece;
            /* Necessary for promoted pawns */
            if (!((piece = move.getPromotedPiece()) instanceof Empty)) {
                piece.setBoard(this);
                piece.setImage();

                source.setPiece(Empty.INSTANCE);
                target.setPiece(piece);

                changeCurrentColor();
            } else {
                piece = source.getPiece();
                piece.move(source, target);
            }
        }

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

        if (piece.isFoe(currentColor)) {
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

    public boolean isValidMove(Square source, Square target) {

        if (isMate()) {
            displayMessage(Message.isMate);
            return false;
        }

        var sPiece = source.getPiece();
        var tPiece = target.getPiece();

        if (!sPiece.isFoe(tPiece)) {
            return false;
        }

        if (!sPiece.isCorrectMovement(source, target)) {
            displayMessage(Message.wrongMove);
            return false;
        }

        if (!isPathFree(source, target)) {
            displayMessage(Message.pathBlocked);
            return false;
        }

        if (isCheck() && !isCheckBlock(source, target) && !isKingEscape(source, target)) {
            displayMessage(Message.getOutOfCheck);
            return false;
        }

        if (isSelfMadeCheck(source, target)) {
            displayMessage(Message.selfMadeCheck);
            return false;
        }

        return true;
    }

    private void displayMessage(Message message) {
        var messageText = resourceBundle.getString(message.string);
        JOptionPane.showMessageDialog(this, messageText);
    }

    public boolean isAttacked(Square square) {
        return getAttackingSquares(square).size() > 0;
    }

    private boolean isCheck() {
        var kingSquare = findKing();
        return getAttackingSquares(kingSquare).size() > 0;
    }

    private boolean isMate() {

        if (!isCheck()) {
            return false;
        }

        var escapeSquares = getEscapeSquares();

        return !isCheckBlockPossible() && escapeSquares.isEmpty();
    }

    private Square findKing() throws IllegalStateException {

        for (var square : squares) {

            var piece = square.getPiece();

            var isKing = piece instanceof King;

            if (isKing && !piece.isFoe(currentColor)) {
                return square;
            }
        }
        throw new IllegalStateException("King has not been found.");
    }

    /**
     * Retrieves a list of squares for each enemy piece that attacks the square
     * passed as the parameter. Each list consists of the square containing the
     * enemy piece and the path that leads to the target square.
     */
    private List<List<Square>> getAttackingSquares(Square target) {
        var allSquaresLists = new ArrayList<List<Square>>();

        for (var source : squares) {

            if (isAttack(source, target)) {
                var singleSquaresList = new ArrayList<Square>();
                var path = getPath(source, target);

                singleSquaresList.add(source);
                singleSquaresList.addAll(path);

                allSquaresLists.add(singleSquaresList);
            }
        }
        return allSquaresLists;
    }

    private boolean isAttack(Square source, Square target) {
        var piece = source.getPiece();

        return !source.equals(target)
                && piece.isFoe(currentColor)
                && piece.isCorrectMovement(source, target)
                && isPathFree(source, target);
    }

    /**
     * Finds squares for king to escape, in order to get out of check.
     */
    private List<Square> getEscapeSquares() {

        var kingSquare = findKing();
        var king = kingSquare.getPiece();

        return squares.stream()
            .filter(square -> king.isCorrectMovement(kingSquare, square))
            .filter(square -> square.getPiece().isFoe(currentColor))
            .filter(square -> !isAttacked(square))
            .collect(Collectors.toList());
    }

    private boolean isCheckBlockPossible() {

        var kingSquare = findKing();
        var allSquaresLists = getAttackingSquares(kingSquare);

        if (allSquaresLists.size() > 1) {
            return false; /* This means an unblockable double check. */
        }

        var singleSquaresList = allSquaresLists.get(0);

        for (var target : singleSquaresList) {
            for (var source : squares) {

                if (isBlock(source, target))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBlock(Square source, Square target) {
        var piece = source.getPiece();

        return !(piece instanceof King)
                && !piece.isFoe(currentColor)
                && piece.isCorrectMovement(source, target)
                && isPathFree(source, target);
    }

    private boolean isSelfMadeCheck(Square source, Square target) {

        var sourcePiece = source.getPiece();

        source.setPiece(Empty.INSTANCE);
        target.setPiece(sourcePiece);

        var isSelfMadeCheck = isCheck();

        source.setPiece(sourcePiece);
        target.setPiece(Empty.INSTANCE);

        return isSelfMadeCheck;
    }

    private boolean isKingEscape(Square source, Square target) {
        var escapeSquares = getEscapeSquares();

        return source.getPiece() instanceof King && escapeSquares.contains(target);
    }

    private boolean isCheckBlock(Square source, Square target) {
        var kingSquare = findKing();
        var allSquaresLists = getAttackingSquares(kingSquare);

        if (allSquaresLists.size() > 1) {
            return false; /* This means an unblockable double check. */
        }

        var singleSquaresList = allSquaresLists.get(0);

        var isKing = source.getPiece() instanceof King;

        return !isKing && singleSquaresList.contains(target);
    }

    /**
     * Checks if there are pieces on the path between source and target squares
     */
    private boolean isPathFree(Square source, Square target) {

        var path = getPath(source, target);

        return path.stream()
            .filter(this::isNotEmptySquare)
            .count() == 0;
    }

    private boolean isNotEmptySquare(Square square) {
        return !(square.getPiece() instanceof Empty);
    }

    /**
     * Traverses the board in particular direction, inferred from differences
     * between source and target squares. Retrieves a list of squares in
     * straight line between source and target squares.
     */
    private List<Square> getPath(Square source, Square target) {

        var piece = source.getPiece();

        if (piece instanceof Knight) {
            return Collections.emptyList();
        }

        int verticalDiff = calcVerticalDiff(source, target);
        int horizontalDiff = calcHorizontalDiff(source, target);

        int row = source.coord.row + verticalDiff;
        int col = source.coord.col + horizontalDiff;
        var coord = new Coord(row, col);

        List<Square> path = new ArrayList<>();

        while (!coord.equals(target.coord)) {

            path.add(squares.get(coord.index));

            row += verticalDiff;
            col += horizontalDiff;

            coord = new Coord(row, col);
        }

        return path;
    }

    private int calcVerticalDiff(Square source, Square target) {

        var isTargetSameRow = source.coord.row == target.coord.row;
        var isTargetRowLower = source.coord.row < target.coord.row;

        return isTargetSameRow ? 0 : (isTargetRowLower ? 1 : -1);
    }

    private int calcHorizontalDiff(Square source, Square target) {

        var isTargetSameCol = source.coord.col == target.coord.col;
        var isTargetColLower = source.coord.col < target.coord.col;

        return isTargetSameCol ? 0 : (isTargetColLower ? 1 : -1);
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

    public void changeCurrentColor() {
        var isWhite = currentColor.equals(Color.WHITE);
        currentColor = isWhite ? Color.BLACK : Color.WHITE;
    }

    public void addMove(Move move) {
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

        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        var source = squares.get(from.index);
        var target = squares.get(to.index);

        var attacker = source.getPiece();

        attacker.move(source, target);
    }

    public boolean isCorrectMovement(int row1, int col1, int row2, int col2) {

        var from = new Coord(row1, col1);
        var to = new Coord(row2, col2);

        return isCorrectMovement(from, to);
    }

    public boolean isCorrectMovement(Coord from, Coord to) {

        var source = squares.get(from.index);
        var target = squares.get(to.index);

        var attacker = source.getPiece();

        return attacker.isCorrectMovement(source, target);
    }
}
