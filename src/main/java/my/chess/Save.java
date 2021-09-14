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
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import lombok.RequiredArgsConstructor;
import my.chess.pieces.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Save {
        
    
    public void loadGame(String filename) {
        var optional = readObject(filename);
        
        if (!optional.isPresent()) {
            return;
        }
        
        var colorListPair = optional.get();
        
        board.clearBoard();
                
        board.setCurrentColor(colorListPair.left);
        
        var list = colorListPair.right;
        
        for (var pair : list) {
            var coord = pair.left;
            var piece = pair.right;
            board.setPiece(coord, piece);
        }
        
        board.repaint();
        
    }
    
    public void saveGame(String fileName) {
        var coordPiecePairList = board.getSquares()
                .stream()                
                .map(square -> square.getPair())
                .collect(Collectors.toList());
        
        var color = board.getCurrentColor();
        
        var pair = new ImmutablePair<>(color, coordPiecePairList);
        
        writeObject(pair, fileName);
                
    }   
    
    @SuppressWarnings("unchecked")
    private Optional<ImmutablePair<Color, List<ImmutablePair<Coord,Piece>>>> readObject(String filename) {
        try {

            var fileInputStream = new FileInputStream(filename);
            var objectInputStream = new ObjectInputStream(fileInputStream);
            
            var object = objectInputStream.readObject();
                        
            objectInputStream.close();
            fileInputStream.close();
            
            var pair = (ImmutablePair<Color, List<ImmutablePair<Coord,Piece>>>) object;
            
            return Optional.of(pair);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(board, exception.getMessage());
            exception.printStackTrace();
        }
        return Optional.empty();
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
    private final Board board;
    
}
