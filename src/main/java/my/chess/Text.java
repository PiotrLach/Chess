package my.chess;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Text {
    gameMenu("MainFrame.gameMenu.text"),
    newGameOption("MainFrame.newGameOption.text"),
    loadGameOption("MainFrame.loadGameOption.text"),
    saveGameOption("MainFrame.saveGameOption.text"),
    aboutMenu("MainFrame.aboutMenu.text"),
    licenseOption("MainFrame.licenseOption.text");
    public final String key;
}
