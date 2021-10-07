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

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Save {

    private final Board board;

    public void loadGame(String filename) {
        var deque = readObject(filename);

        if (deque.isEmpty()) {
            return;
        }

        board.loadGame(deque);
    }

    public void saveGame(String fileName) {

        var moves = board.getMoves();

        writeObject(moves, fileName);

    }

    @SuppressWarnings("unchecked")
    private Deque<Move> readObject(String filename) {
        try {

            var fileInputStream = new FileInputStream(filename);
            var objectInputStream = new ObjectInputStream(fileInputStream);

            var object = objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return (Deque<Move>) object;

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(board, exception.getMessage());
            exception.printStackTrace();
        }
        return new LinkedList<>();
    }

    private <T> void writeObject(T t, String filename) {
        try {

            var fileOutputStream = new FileOutputStream(filename);
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            objectOutputStream.close();

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(board, exception.getMessage());
            exception.printStackTrace();
        }
    }

}
