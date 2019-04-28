/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.util.HashMap;
import my.chess.pieces.Bishop;
import my.chess.pieces.ChessPiece;
import my.chess.pieces.King;
import my.chess.pieces.Knight;
import my.chess.pieces.Pawn;
import my.chess.pieces.Queen;
import my.chess.pieces.Rook;

/**
 *
 * @author bruce
 */
public class Player {
    
    public Player(){
        initPieces();        
    }
    private void initPieces(){
        playerPieces=new HashMap<>();
        for(Integer i=0; i<8; i++) { 
            if(i < 1) {
                playerPieces.put( "King", new King(playerColor) );
                playerPieces.put( "Queen", new Queen(playerColor) );
            }
            if(i < 2) {
                playerPieces.put( "Knight"+i.toString(), new Knight(playerColor) );
                playerPieces.put( "Bishop"+i.toString(), new Bishop(playerColor) );
                playerPieces.put( "Rook"+i.toString(), new Rook(playerColor) );
            }
            playerPieces.put( "Pawn"+i.toString(), new Pawn(playerColor,1) );            
        }
        
    }
    public ChessPiece getPlayerPiece (String pieceName) {
        return playerPieces.get(pieceName);
    }
    public Color getPlayerColor() {
        return playerColor;
    }
    private static Color chooseColor(){
        int randomColor = (int)(Math.random() * 2 + 1);
        switch (randomColor) {
            default:
                return Color.WHITE;                
            case 2:
                return Color.BLACK;                
        }
    }

    public boolean playerTurnAvailable;
    private Color playerColor;        
    private HashMap<String, ChessPiece> playerPieces;    
    private HashMap<String, ChessPiece> removedPieces;
}
