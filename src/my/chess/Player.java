/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.chess;

import java.awt.Color;
import java.util.HashMap;
import my.chess.pieces.*;

/**
 *
 * @author bruce
 */
public class Player {
    
    public Player(){
        initPieces();        
    }
    private void initPieces(){
//    /*        
        playerPieces=new HashMap<>();        
        for(int i=0; i<8; i++) { 
            playerPieces.put( "Pawn"+i+playerColor, new Pawn(playerColor, startingRowPawns, i) );
            if(i < 1) {
                playerPieces.put( "Rook"+i+playerColor, new Rook(playerColor, startingRowRest,0) );
                playerPieces.put( "Knight"+i+playerColor, new Knight(playerColor, startingRowRest,1) );
                playerPieces.put( "Bishop"+i+playerColor, new Bishop(playerColor, startingRowRest,2) );                
                playerPieces.put( "Queen"+i+playerColor, new Queen(playerColor, startingRowRest, 3) );
                playerPieces.put( "King"+i+playerColor, new King(playerColor, startingRowRest, 4) );                
                playerPieces.put( "Bishop"+i+playerColor, new Bishop(playerColor, startingRowRest,5) );                
                playerPieces.put( "Knight"+i+playerColor, new Knight(playerColor, startingRowRest,6) );
                playerPieces.put( "Rook"+i+playerColor, new Rook(playerColor, startingRowRest,7) );                                
            }                                    
        }
//    */    
    }
    public ChessPiece getPlayerPiece (String pieceName) {
        return playerPieces.get(pieceName);
    }    
    private void startingRowPawns() {
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
    private HashMap<String, ChessPiece> playerPieces;    
    private HashMap<String, ChessPiece> removedPieces;
}
