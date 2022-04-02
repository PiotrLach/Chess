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
package my.chess.logic.pieces;

import lombok.*;
import my.chess.logic.Logic;
import my.chess.logic.square.Square;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Piotr Lach
 */
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
abstract public class Piece implements Serializable {

    @AllArgsConstructor
    public enum Name {
        Pawn1   (0, "L", 0),
        Pawn6   (0, "H", 1),
        Bishop  (1, "B", 2),
        Knight  (2, "N", 3),
        Rook    (3, "R", 4),
        King    (4, "K", 5),
        Queen   (5, "Q", 6),
        Empty   (6, " ", 7) ;
        public final int imageId;
        public final String symbol;
        public final int pieceId;
    }

    @Setter
    protected transient Logic logic;
    public final Color color;
    protected transient Image image;
    protected Name name;
    @Getter
    @ToString.Include
    protected boolean isOnStartPosition = true;

    static final PieceImageLoader imageLoader = PieceImageLoader.INSTANCE;
    @Serial
    private static final long serialVersionUID = 4232331441720820159L;

    public Piece(Name name, Color color, Logic logic) {
        this.name = name;
        this.color = color;
        this.logic = logic;
        this.image = imageLoader.getImage(name, color);
    }

    public void move(Square source, Square target) {
        if (!(source.getPiece() == this)) {
            return;
        }

        if (!logic.isValidMove(source, target)) {
            return;
        }

        target.setPiece(this);
        source.setPiece(Empty.INSTANCE);
        source.setSelected(false);
        isOnStartPosition = false;

        logic.addMove(source, target);

        logic.changeCurrentColor();
        logic.setOptionalSourceEmpty();
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
