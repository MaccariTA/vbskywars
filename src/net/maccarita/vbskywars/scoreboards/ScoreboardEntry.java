package net.maccarita.vbskywars.scoreboards;

import lombok.Data;

/**
 * An object represeting an entry in the scoreboard (or more accurately, GameBoard)
 * @see GameBoard
 */
@Data
public class ScoreboardEntry {

    private final String name;
    private String prefix;
    private String suffix;

}