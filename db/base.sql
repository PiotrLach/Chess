DROP TABLE IF EXISTS colors;
-- DROP TABLE IF EXISTS chessPlayers;
DROP TABLE IF EXISTS chessPieces;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS chessFields;
PRAGMA foreign_keys = ON;
CREATE TABLE colors(
	colorValue INTEGER PRIMARY KEY
);
CREATE TABLE chessPieces(
	pieceID INTEGER PRIMARY KEY,
	pieceName TEXT NOT NULL,
	pieceColor INTEGER NOT NULL,
	FOREIGN KEY (pieceColor) REFERENCES colors(colorValue)
);
CREATE TABLE startingPositions(
        positionID INTEGER PRIMARY KEY,
        gameID INTEGER,
        position INTEGER,        
        color INTEGER,
        FOREIGN KEY (color) REFERENCES colors(colorValue),
        FOREIGN KEY (gameID) REFERENCES games(gameID)
);
insert into startingPositions(gameID,position,color) VALUES
(1,1,0);
insert into startingPositions(gameID,position,color) VALUES
(1,6,1);
insert into startingPositions(gameID,position,color) VALUES
(2,1,1);
insert into startingPositions(gameID,position,color) VALUES
(2,6,0);
insert into startingPositions(gameID,position,color) VALUES
(3,1,1);
insert into startingPositions(gameID,position,color) VALUES
(3,6,0);
CREATE TABLE games(
	gameID INTEGER PRIMARY KEY,
	currentColor INTEGER NOT NULL,        
        date TEXT,
        name TEXT,
        FOREIGN KEY (currentColor) REFERENCES colors(colorValue)
);
-- CREATE TABLE chessPlayers(
-- 	playerName TEXT PRIMARY KEY,
-- 	playerColor INTEGER NOT NULL,
-- 	gameID INTEGER,
-- 	FOREIGN KEY (playerColor) REFERENCES colors(colorValue),
-- 	FOREIGN KEY (gameID) REFERENCES games(gameID)
-- );
CREATE TABLE chessFields(
	chessFieldID INTEGER PRIMARY KEY,
	x INTEGER CHECK (x<8),
	y INTEGER CHECK (y<8),
	piece INTEGER,
	game INTEGER NOT NULL,
	FOREIGN KEY (piece) REFERENCES chessPieces(pieceID),
	FOREIGN KEY (game) REFERENCES games(gameID)
);
INSERT INTO colors VALUES (0);
INSERT INTO colors VALUES (1);
INSERT INTO chessPieces VALUES (0, "Pawn1", 0);
INSERT INTO chessPieces VALUES (1, "Pawn6", 0);
INSERT INTO chessPieces VALUES (2, "Rook", 0);
INSERT INTO chessPieces VALUES (3, "Bishop", 0);
INSERT INTO chessPieces VALUES (4, "Knight", 0);
INSERT INTO chessPieces VALUES (5, "Queen", 0);
INSERT INTO chessPieces VALUES (6, "King", 0);
INSERT INTO chessPieces VALUES (7, "Pawn1", 1);
INSERT INTO chessPieces VALUES (8, "Pawn6", 1);
INSERT INTO chessPieces VALUES (9, "Rook", 1);
INSERT INTO chessPieces VALUES (10, "Bishop", 1);
INSERT INTO chessPieces VALUES (11, "Knight", 1);
INSERT INTO chessPieces VALUES (12, "Queen", 1);
INSERT INTO chessPieces VALUES (13, "King", 1);
--INSERT INTO games VALUES((SELECT MAX(gameID) FROM games)+1,0);
--INSERT INTO chessFields VALUES((SELECT MAX(chessFieldID) FROM chessFields)+1,x,y,piece,game);
