package net.maccarita.vbskywars.scoreboards;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * An interface representing a scoreboard supplier. It supplies the scoreboard with the title and lines.
 * <p>
 * Implementation example is {@link net.maccarita.vbskywars.scoreboards.impl.GameSupplier}
 */
public interface ScoreboardSupplier {
    /**
     * Get the title for the scoreboard.
     *
     * @return The scoreboard provided title.
     */
    String getTitle();

    /**
     * Get the lines for the scoreboard
     *
     * @param player The player to which provide the lines to.
     * @return The scoreboard provided lines.
     */
    List<ScoreboardEntry> getLines(Player player);
}