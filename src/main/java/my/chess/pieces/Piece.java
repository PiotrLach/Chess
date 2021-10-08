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
package my.chess.pieces;

import java.awt.Color;
import my.chess.Square;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import my.chess.Board;
import my.chess.Move;

/**
 *
 * @author Piotr Lach
 */
@ToString(onlyExplicitlyIncluded = true)
abstract public class Piece implements Serializable {

    private static final long serialVersionUID = 4232331441720820159L;

    public Piece(PieceName pieceName, Color color, Image image, Board board) {
        this.name = pieceName;
        this.color = color;
        this.image = image;
        this.board = board;
    }

    public void movePiece(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        target.setPiece(this);
        source.setPiece(Empty.INSTANCE);
        source.setHighlighted(false);
        isOnStartPosition = false;

        var move = new Move(source.coord, target.coord);
        board.getMoves().add(move);

        board.changeCurrentColor();
    }

    public void drawImage(Graphics graphics, int x, int y, int size) {
        graphics.drawImage(image, x, y, size, size, null);
    }

    public enum PieceName {
        Pawn1, Pawn6, Bishop, Knight, Rook, King, Queen, Empty
    }

    public boolean isFoe(Piece piece) {
        return !piece.color.equals(this.color);
    }

    public boolean isFoe(Color color) {
        return !this.color.equals(color);
    }

    /**
     * Must be called for any deserialized piece, since images are not
     * saved.
     */
    abstract public void setImage();

    abstract public boolean isCorrectMovement(Square source, Square target);

    @Setter
    protected transient Board board;
    public final Color color;
    protected transient Image image;
    protected PieceName name;
    @Getter
    @ToString.Include
    protected boolean isOnStartPosition = true;
    static final PieceImageLoader imageLoader = PieceImageLoader.INSTANCE;
}
