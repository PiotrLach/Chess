-- Java chess game implementation
-- Copyright (C) 2021 Piotr Lach
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.

-- You should have received a copy of the GNU General Public License
-- along with this program. If not, see <https://www.gnu.org/licenses/>.
-- 
DROP TABLE IF EXISTS colors;
DROP TABLE IF EXISTS chessPieces;
DROP TABLE IF EXISTS startingPositions;
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
        date TEXT,
        name TEXT,
        FOREIGN KEY (currentColor) REFERENCES colors(colorValue)
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
