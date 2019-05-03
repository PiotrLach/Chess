    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.util.ArrayList;
import my.chess.pieces.*;

/**
 *
 * @author bruce
 */
public class Player {
    
    public Player(){
        startingRows();
        initPieces();        
    }
    private void initPieces(/*GameState g*/){
    /*                        
        
    */
        for(int i=0; i<8; i++) { 
            playerPieces.add(  new Pawn(playerColor, startingRowPawns, i) );
            if(i < 1) {
                playerPieces.add(  new Rook(playerColor, startingRowRest,0) );
                playerPieces.add(  new Knight(playerColor, startingRowRest,1) );
                playerPieces.add(  new Bishop(playerColor, startingRowRest,2) );                
                playerPieces.add(  new Queen(playerColor, startingRowRest, 3) );
                playerPieces.add(  new King(playerColor, startingRowRest, 4) );                
                playerPieces.add(  new Bishop(playerColor, startingRowRest,5) );                
                playerPieces.add(  new Knight(playerColor, startingRowRest,6) );
                playerPieces.add(  new Rook(playerColor, startingRowRest,7) );                                
            }                                    
        }
    
    }  
    private void startingRows() {
        if (playerColor == Color.BLACK) {  
            startingRowPawns = 1;            
            startingRowRest = startingRowPawns - 1;
        }
        else {
            startingRowPawns = 6; 
            startingRowRest = startingRowPawns + 1;            
        }        
    }
    public boolean playerTurnAvailable;
    private int startingRowPawns;
    private int startingRowRest;
    private Color playerColor;        
    private ArrayList<ChessPiece> playerPieces = new ArrayList<>();        
    private enum GameState { NEW, SAVED}; 
}
