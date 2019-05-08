DROP TABLE IF EXISTS colors;
DROP TABLE IF EXISTS chessPlayers;
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
CREATE TABLE games(
	gameID INTEGER PRIMARY KEY,
	currentColor INTEGER NOT NULL,
	FOREIGN KEY (currentColor) REFERENCES colors(colorValue)
);
CREATE TABLE chessPlayers(
	playerName TEXT PRIMARY KEY,
	playerColor INTEGER NOT NULL,
	gameID INTEGER,
	FOREIGN KEY (playerColor) REFERENCES colors(colorValue),
	FOREIGN KEY (gameID) REFERENCES games(gameID)
);
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
INSERT INTO chessPieces VALUES (0, "P", 0);
INSERT INTO chessPieces VALUES (1, "W", 0);
INSERT INTO chessPieces VALUES (2, "G", 0);
INSERT INTO chessPieces VALUES (3, "S", 0);
INSERT INTO chessPieces VALUES (4, "H", 0);
INSERT INTO chessPieces VALUES (5, "K", 0);
INSERT INTO chessPieces VALUES (6, "P", 1);
INSERT INTO chessPieces VALUES (7, "W", 1);
INSERT INTO chessPieces VALUES (8, "G", 1);
INSERT INTO chessPieces VALUES (9, "S", 1);
INSERT INTO chessPieces VALUES (10, "H", 1);
INSERT INTO chessPieces VALUES (11, "K", 1);
INSERT INTO games VALUES (0, 0);
INSERT INTO chessFields VALUES (0, 0, 0, 0, 0);
--INSERT INTO games VALUES((SELECT MAX(gameID) FROM games)+1,0);
--INSERT INTO chessFields VALUES((SELECT MAX(chessFieldID) FROM chessFields)+1,x,y,piece,game);
