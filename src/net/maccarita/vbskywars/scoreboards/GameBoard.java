package net.maccarita.vbskywars.scoreboards;

import lombok.Getter;
import net.maccarita.vbskywars.VBSkywars;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * The updating scoreboard the player would have in his sidebar.
 * Holding all the related information to update, remove and hide the scoreboard.
 */
public class GameBoard {

    @Getter private final BufferObjective bufferObjective;
    @Getter private final Scoreboard scoreboard;
    @Getter private final Player player;
    @Getter private boolean sidebarVisible;
    @Getter private boolean removed;
    @Getter private ScoreboardSupplier currentSupplier;

    /**
     * Constructor
     * @param player The player to build GameBoard to.
     */
    public GameBoard(final Player player) {
        this.sidebarVisible = false;
        this.removed = false;
        this.player = player;
        this.scoreboard = VBSkywars.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        this.bufferObjective = new BufferObjective(this.scoreboard);
        player.setScoreboard(this.scoreboard);
    }

    /**
     * Remove this gameboard, unregister it from the player.
     */
    public void remove() {
        this.removed = true;
        if (this.scoreboard != null) {
            for (final Team team : this.scoreboard.getTeams()) {
                team.unregister();
            }
            for (final Objective objective : this.scoreboard.getObjectives()) {
                objective.unregister();
            }
        }
    }

    /**
     * Change the visibility of the GameBoard.
     * @param visible True for visible, false otherwise.
     */
    public void setSidebarVisible(boolean visible) {
        this.sidebarVisible = visible;
        this.bufferObjective.setDisplaySlot(visible ? DisplaySlot.SIDEBAR : null);
    }

    /**
     * Set the scoreboard supplier for this GameBoard.
     * @param supplier The new scoreboard supplier to set.
     */
    public void setScoreboardSupplier(final ScoreboardSupplier supplier) {
        if (supplier != null && supplier.equals(this.currentSupplier)) {
            return;
        }
        this.currentSupplier = supplier;
        if (supplier == null) {
            this.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    /**
     * Update this gameboard.
     */
    void update() {
        if (this.currentSupplier == null) {
            this.bufferObjective.setVisible(false);
        } else {
            this.bufferObjective.setTitle(this.currentSupplier.getTitle());
            this.bufferObjective.setAllLines(this.currentSupplier.getLines(this.player));
            this.bufferObjective.flip();
        }
    }
}