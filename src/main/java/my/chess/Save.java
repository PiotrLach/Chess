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

import java.io.*;
import java.util.Deque;
import javax.swing.JOptionPane;
import lombok.RequiredArgsConstructor;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Save {

    private final Board board;
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("my/chess/Bundle");

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

        } catch (IOException exception) {

            var message = resourceBundle.getString("Save.loadError");
            JOptionPane.showMessageDialog(board, message);

        } catch (ClassNotFoundException exception) {

            var message = resourceBundle.getString("Save.wrongFormat");
            JOptionPane.showMessageDialog(board, message);

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

        } catch (IOException exception) {

            var message = resourceBundle.getString("Save.saveError");
            JOptionPane.showMessageDialog(board, message);
            
        }
    }

}
