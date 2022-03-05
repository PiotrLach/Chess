package my.chess;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class ActionPerformer {

    private final ResourceBundle bundle = ResourceBundle.getBundle("my/chess/Bundle");
    private final MainFrame mainFrame;
    private final GameBoard board;

    public void newGame(ActionEvent evt) {
        board.setDefaultGame();
    }

    public void loadGame(ActionEvent evt) {
        var fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(mainFrame);

        if (!(result == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        var file = fileChooser.getSelectedFile();
        var fileName = file.getAbsolutePath();

        board.loadGame(fileName);
    }

    public void saveGame(ActionEvent evt) {

        var fileChooser = new JFileChooser();
        int saveOption = fileChooser.showSaveDialog(mainFrame);

        if (!(saveOption == JFileChooser.APPROVE_OPTION)) {
            return;
        }

        var file = fileChooser.getSelectedFile();
        int confirmationOption = 0;

        if (file.exists()) {
            var message = bundle.getString("MainFrame.fileExists.text");
            var formattedMessage = String.format(message, file.getName());
            confirmationOption = JOptionPane.showConfirmDialog(fileChooser, formattedMessage);
        }

        if (confirmationOption != JOptionPane.OK_OPTION) {
            return;
        }

        var fileName = file.getAbsolutePath();
        board.saveGame(fileName);
    }

    public void displayLicense(ActionEvent evt) {

        var fileName = bundle.getString("MainFrame.licenseFile");

        var classLoader = mainFrame.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);
        var stringBuilder = new StringBuilder();

        try {
            var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            int char1 = 0;
            while ((char1 = inputStreamReader.read()) > -1) {
                stringBuilder.append((char) char1);
            }
        } catch (IOException exception) {
            var errorMessage = bundle.getString("MainFrame.errorReadingLicense");
            JOptionPane.showMessageDialog(mainFrame, errorMessage);
            return;
        }

        JOptionPane.showMessageDialog(mainFrame, stringBuilder.toString());
    }
}