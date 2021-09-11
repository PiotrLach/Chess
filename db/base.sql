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
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS chessFields;
PRAGMA foreign_keys = ON;
CREATE TABLE games(
    gameID INTEGER PRIMARY KEY,
    currentColor INTEGER NOT NULL,            
    date TEXT,
    name TEXT
);
CREATE TABLE chessFields(
    chessFieldID INTEGER PRIMARY KEY,
    x INTEGER CHECK (x<8),
    y INTEGER CHECK (y<8),
    piece INTEGER,
    game INTEGER NOT NULL,
    FOREIGN KEY (game) REFERENCES games(gameID)
);
