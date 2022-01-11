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
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import my.chess.Board;
import my.chess.Move;

/**
 * @author Piotr Lach
 */
@ToString(onlyExplicitlyIncluded = true)
abstract public class Piece implements Serializable {

    @AllArgsConstructor
    public enum Name {
        Pawn1(0),
        Pawn6(0),
        Bishop(1),
        Knight(2),
        Rook(3),
        King(4),
        Queen(5),
        Empty(6);
        public final int id;
    }

    @Setter
    protected transient Board board;
    public final Color color;
    protected transient Image image;
    protected Name name;
    @Getter
    @ToString.Include
    protected boolean isOnStartPosition = true;

    static final PieceImageLoader imageLoader = PieceImageLoader.INSTANCE;
    @Serial
    private static final long serialVersionUID = 4232331441720820159L;

    public Piece(Name name, Color color, Board board) {
        this.name = name;
        this.color = color;
        this.board = board;
        this.image = imageLoader.getImage(name, color);
    }

    public void move(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        if (!board.isValidMove(source, target)) {
            return;
        }

        target.setPiece(this);
        source.setPiece(Empty.INSTANCE);
        source.setHighlighted(false);
        isOnStartPosition = false;

        var move = new Move(source.coord, target.coord);
        board.addMove(move);

        board.changeCurrentColor();
        board.setOptionalSourceEmpty();
    }

    public void drawImage(Graphics graphics, int x, int y, int size) {
        graphics.drawImage(image, x, y, size, size, null);
    }

    public boolean isFoe(Piece piece) {
        return !piece.color.equals(this.color);
    }

    public boolean isFoe(Color color) {
        return !this.color.equals(color);
    }

    /**
     * Must be called for any deserialized piece, since images are not serialized.
     */
    public void setImage() {
        image = imageLoader.getImage(name, color);
    }

    abstract public boolean isCorrectMovement(Square source, Square target);
}
