/*
 * Copyright (C) 2022 Piotr Lach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package my.chess;

import lombok.Getter;

import java.util.*;

public class MockBoard implements Board {

    @Getter
    private final Deque<Move> moves = new LinkedList<>();
    private final List<Square> squares = new ArrayList<>();
    @Getter
    private final Logic logic = new Logic(this, squares, moves);

    public MockBoard() {
        createSquares();
    }

    public void createSquares() {
        for (int idx = 0; idx < 64; idx++) {
            var coord = new Coord(idx);
            var square = new Square(coord);
            squares.add(square);
        }
    }

    @Override
    public int getPromotionChoice() {
        return 0;
    }

    @Override
    public void setGame(String[] layout) {
        logic.setLayout(layout);
    }

    @Override
    public void changeCurrentColor() {
        logic.changeCurrentColor();
    }

    @Override
    public void setOptionalSourceEmpty() {

    }

    @Override
    public void setDefaultGame() {

    }

    @Override
    public void repaint() {

    }

    @Override
    public void displayMessage(Message message) {

    }

}
