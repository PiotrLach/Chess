/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

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
import my.chess.pieces.*;

/**
 *
 * @author Piotr Lach
 */
public class ChessBoard extends JPanel {

    public ChessBoard() {
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
        int x = 0, y = 0;
        for (int i = beginHeight; i > endHeight; i -= diffVertical) {
            for (int j = beginWidth; j < endWidth; j += diffHorizontal) {
                chessMatrix[x][y].setLocation(j, i);
                chessMatrix[x][y].setSize(diffHorizontal, diffVertical);
                y++;
            }
            y = 0;
            x++;
            if (x == 8) {
                x = 0;
            }
        }
        repaint();
    }

    private void createFields() {
        int x = 0, y = 0;
        for (int i = beginHeight; i > endHeight; i -= diffVertical) {
            for (int j = beginWidth; j < endWidth; j += diffHorizontal) {
                ChessField c = new ChessField(j, i, 80, 80, x, y);
                chessMatrix[x][y] = c;
                y++;
            }
            y = 0;
            x++;
            if (x == 8) {
                x = 0;
            }
        }
    }

    public void selectAndMove(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            Point p = evt.getPoint();
            chooseBoardPiece(p);
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            Point p = evt.getPoint();
            moveBoardPiece(p);
        }
    }

    private void chooseBoardPiece(Point p) {
        loop:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessField cf = chessMatrix[i][j];
                ChessPiece cp = cf.getCurrentChessPiece();
                if (!mate
                        && cf.contains(p)
                        && !cf.isHighlighted()
                        && cp != null
                        && currentColor == cp.getFigureColor()
                        && (!check
                        || (check && !piecesToBlockCheckUnavailable)
                        || (check && cp instanceof King && piecesToBlockCheckUnavailable))) {
                    chessMatrix[sourceI][sourceJ].setHighlighted(false);
                    cf.setHighlighted(true);
                    selectedChessPiece = cp;
                    sourceI = i;
                    sourceJ = j;
                    repaint();
                    break loop;
                } else if (cf.contains(p) && cp != null
                        && currentColor != cp.getFigureColor()) {
                    String color = currentColor == Color.WHITE ? "białych" : "czarnych";
                    JOptionPane.showMessageDialog(this, "Teraz ruch " + color + "!");
                    break loop;
                }
            }
        }
    }

    private Point findKing() throws Exception {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                if (cp instanceof King && cp.getFigureColor() == currentColor) {
                    Point kingCoordinates = new Point(i, j);
                    return kingCoordinates;
                }
            }
        }
        throw new Exception("King has not been found.");
    }

    private void check() {
        int kingX = 0, kingY = 0;
        try {
            Point p = findKing();
                        kingX = (int) p.getX();
            kingY = (int) p.getY();
        } catch (Exception e) {
            System.out.println(e);
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
        ArrayList<Point> pathTemp = new ArrayList();
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessField cf = chessMatrix[i][j];
                ChessPiece cp = cf.getCurrentChessPiece();
                if (i != kingX && j != kingY) {
                    if (cp != null
                            && cp.getFigureColor() != currentColor
                            && cp.movementConditionFullfilled(i, j, kingX, kingY)
                            && pathIsFree(i, j, kingX, kingY)) {
                        if (separate) {
                            pathTemp.add(new Point(i, j));
                            pathTemp.addAll(makePath(i, j, kingX, kingY));
                        }
                        sum++;
                    }
                }
            }
        }
        if (separate) {
            path = new ArrayList(pathTemp);
        }
        if (sum == 0) {
            return false;
        } else if (sum > 0 && separate) {
            JOptionPane.showMessageDialog(this, "Szach!");
            kingPath = new ArrayList(mate(kingX, kingY));
            System.out.println(kingPath);
            if (piecesToBlockCheckUnavailable = !piecesToBlockCheckAvailable(pathTemp)) {
                if (mate = kingPath.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mat!");
                }
            }
        }
        return true;
    }

    private ArrayList<Point> mate(int x, int y) {
        ArrayList<Point> rescue = new ArrayList();
                for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i <= 7 && i >= 0) && (j <= 7 && j >= 0)) {
                    ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                    if (cp == null || cp.getFigureColor() != currentColor) {
                        System.out.println(i + " " + j);
                        if (!check(i, j, false)) {
                            rescue.add(new Point(i, j));
                        }
                    }
                }
            }
        }
        return rescue;
    }

    private boolean piecesToBlockCheckAvailable(ArrayList<Point> path) {
        for (Point p : path) {
            int x = (int) p.getX(), y = (int) p.getY();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessField cf = chessMatrix[i][j];
                    ChessPiece cp = cf.getCurrentChessPiece();
                    if (cp != null
                            && !(cp instanceof King)
                            && cp.getFigureColor() == currentColor
                            && cp.movementConditionFullfilled(i, j, x, y)
                            && pathIsFree(i, j, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean selfMadeCheck(int row, int column) {
        if (!check) {
            ChessField source = chessMatrix[sourceI][sourceJ];
            ChessField target = chessMatrix[row][column];
            source.setCurrentChessPiece(null);
            target.setCurrentChessPiece(selectedChessPiece);
            int sum = 0;
            try {
                Point p = findKing();
                                int x = (int) p.getX(), y = (int) p.getY();
                sum += !check(x, y, false) ? 0 : 1;
            } catch (Exception e) {
                            }
            if (sum > 0) {
                JOptionPane.showMessageDialog(this, "Ruch niedozwolony: skutkowałby szachem króla!\n");
            }
            source.setCurrentChessPiece(selectedChessPiece);
            target.setCurrentChessPiece(null);
            return sum > 0;
        } else {
            return false;
        }
    }
    private boolean boardPieceMovementConditions(ChessField cf, ChessPiece cp, Point p, int i, int j) {
        final boolean conditions[] = {

        };
        for (boolean condition : conditions) {
            
        }  
        return true;
    }
    private void moveBoardPiece(Point p) {
        loop:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessField cf = chessMatrix[i][j];
                ChessPiece cp = cf.getCurrentChessPiece();                                
                if (selectedChessPiece != null
                        && cf.contains(p)
                        && !cf.isHighlighted()
                        && (cp == null || selectedChessPiece.isFoe(cp))
                        && selectedChessPiece.movementConditionFullfilled(sourceI, sourceJ, i, j)
                        && pathIsFree(sourceI, sourceJ, i, j)
                        && !selfMadeCheck(i, j)
                        && (!check || (check && !(selectedChessPiece instanceof King) && path.contains(new Point(i, j)))
                        || (check && selectedChessPiece instanceof King && kingPath.contains(new Point(i, j))))
                        ) {
                    check = false;
                    System.out.format("%d %d\n", i, j);
                    cf.setCurrentChessPiece(selectedChessPiece);
                    movementLog = new Log(currentColor, sourceI, sourceJ, i, j, selectedChessPiece.getPieceName());
                    selectedChessPiece = null;
                    chessMatrix[sourceI][sourceJ].setCurrentChessPiece(null);
                    chessMatrix[sourceI][sourceJ].setHighlighted(false);
                    currentColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
                    oppositeColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
                    repaint();
                    check();
                    break loop;
                } else if (selectedChessPiece != null && chessMatrix[i][j].contains(p) && !selectedChessPiece.movementConditionFullfilled(sourceI, sourceJ, i, j)) {
                    JOptionPane.showMessageDialog(this, "Ruch niedozwolony: warunek ruchu nie spełniony!\n");
                    break loop;
                }
            }
        }
    }

    private void promote(Pawn p) {
        String[] possibilites = {"Goniec"};
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Wybierz figurę:\n",
                "Wybierz figurę",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                possibilites,
                possibilites[0]);
        System.out.println(s);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessField cf = chessMatrix[i][j];
                if (cf.isHighlighted()) {
                    cf.highlightChessField(g);
                } else {
                    cf.drawChessField(g, i, j);
                }
                if (cf.getCurrentChessPiece() != null) {
                    Double x = cf.getX();
                    Double y = cf.getY();
                    ChessPiece cp = cf.getCurrentChessPiece();
                    cp.drawImage(g, x.intValue(), y.intValue(), diffHorizontal, diffVertical);
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

    private boolean pathIsFree(int x1, int y1, int x2, int y2) {
        int verticalDifference, horizontalDifference, notNullCount = 0;
        if (!(selectedChessPiece instanceof Knight)) {
            verticalDifference = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
            horizontalDifference = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
            x1 += verticalDifference;
            y1 += horizontalDifference;
            for (int i = x1, j = y1; i != x2 || j != y2; i += verticalDifference, j += horizontalDifference) {
                ChessPiece cp = chessMatrix[i][j].getCurrentChessPiece();
                notNullCount += cp == null ? 0 : 1;
            }
        }
        return notNullCount == 0;
    }

    public void setNewGame() throws IOException {
        clearBoard();
        Color c1 = new Random().nextInt(2) == 1 ? Color.BLACK : Color.WHITE;
        Color c2 = c1 == Color.BLACK ? Color.WHITE : Color.BLACK;
        startingPoints.put(c1, 1);
        startingPoints.put(c2, 6);
        for (int i = 0; i < 8; i++) {
            chessMatrix[1][i].setCurrentChessPiece(new Pawn(c1, ChessPiece.PieceName.Pawn1));
            chessMatrix[6][i].setCurrentChessPiece(new Pawn(c2, ChessPiece.PieceName.Pawn6));
        }
        chessMatrix[0][0].setCurrentChessPiece(new Rook(c1));
        chessMatrix[0][1].setCurrentChessPiece(new Knight(c1));
        chessMatrix[0][2].setCurrentChessPiece(new Bishop(c1));
        chessMatrix[0][3].setCurrentChessPiece(new Queen(c1));
        chessMatrix[0][4].setCurrentChessPiece(new King(c1));
        chessMatrix[0][5].setCurrentChessPiece(new Bishop(c1));
        chessMatrix[0][6].setCurrentChessPiece(new Knight(c1));
        chessMatrix[0][7].setCurrentChessPiece(new Rook(c1));
        chessMatrix[7][0].setCurrentChessPiece(new Rook(c2));
        chessMatrix[7][1].setCurrentChessPiece(new Knight(c2));
        chessMatrix[7][2].setCurrentChessPiece(new Bishop(c2));
        chessMatrix[7][3].setCurrentChessPiece(new Queen(c2));
        chessMatrix[7][4].setCurrentChessPiece(new King(c2));
        chessMatrix[7][5].setCurrentChessPiece(new Bishop(c2));
        chessMatrix[7][6].setCurrentChessPiece(new Knight(c2));
        chessMatrix[7][7].setCurrentChessPiece(new Rook(c2));
        repaint();
    }

    public static void clearBoard() {
        currentColor = Color.WHITE;
        oppositeColor = Color.BLACK;
        check = false;
        selectedChessPiece = null;
        startingPoints = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessMatrix[i][j].setCurrentChessPiece(null);
            }
        }
    }

    public static void setCurrentColor(Color c) throws IllegalArgumentException {
        if (c == Color.BLACK || c == Color.WHITE) {
            currentColor = c;
        } else {
            throw new IllegalArgumentException("Current color can only be black or white");
        }
    }

    public static Color getCurrentColor() {
        return currentColor;
    }

    public static ChessField getChessMatrixField(int x, int y) throws IllegalArgumentException {
        if (x < 8 && y < 8) {
            return chessMatrix[x][y];
        } else {
            throw new IllegalArgumentException("Columns and rows indices cannot exceed 7");
        }
    }

    public static void setChessMatrixField(int x, int y, ChessPiece cp) throws IllegalArgumentException {
        if (x < 8 && y < 8) {
            chessMatrix[x][y].setCurrentChessPiece(cp);
        } else {
            throw new IllegalArgumentException("Columns and rows indices cannot exceed 7");
        }
    }

    public static void setStartingPoints(Color c, Integer i) throws Exception {
        if (startingPoints == null) {
            startingPoints = new HashMap();
        }
        startingPoints.put(c, i);
        if (startingPoints.size() > 2) {
            throw new Exception("Too many starting points!");
        }
    }

    public static HashMap<Color, Integer> getstartingPoints() {
        return startingPoints;
    }
    private static boolean check = false, mate = false, piecesToBlockCheckUnavailable = false;
    public static Log movementLog;
    private static HashMap<Color, Integer> startingPoints;
    private ArrayList<Point> path, kingPath;
    private static final ChessField[][] chessMatrix = new ChessField[8][8];
    private static Color currentColor;
    private static Color oppositeColor;
    private int sourceI, sourceJ, destI, destJ;
    private static ChessPiece selectedChessPiece;
    private int beginHeight,
            endHeight,
            beginWidth,
            endWidth,
            diffHorizontal,
            diffVertical;

}
