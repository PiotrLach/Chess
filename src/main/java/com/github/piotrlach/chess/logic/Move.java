/*
 * Copyright (C) 2022 Piotr Lach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.piotrlach.chess.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Piotr Lach
 */
@RequiredArgsConstructor
public class Move {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Move(@JsonProperty("from") final int from,
                @JsonProperty("to") final int to,
                @JsonProperty("promoted") final String promoted) {
        this.from = from;
        this.to = to;
        this.promoted = promoted;
    }

    public final int from;
    public final int to;
    @Getter
    private String promoted = " ; ";
}
