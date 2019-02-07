package net.maccarita.vbskywars.scoreboards;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.games.events.GamePlayerDiedEvent;
import net.maccarita.vbskywars.games.events.PlayerJoinGameEvent;
import net.maccarita.vbskywars.games.events.PlayerLeaveGameEvent;
import net.maccarita.vbskywars.scoreboards.impl.GameSupplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the scoreboard for all the players, give players the scoreboard on specific events, updates the scoreboard,
 * provide the supplier for the title and lines.
 */
public class ScoreboardManager implements Listener, Runnable {

    private final Map<UUID, GameBoard> playerBoards;
    private final GameSupplier gameScoreboard;

    /**
     * Constructor
     */
    public ScoreboardManager() {
        this.playerBoards = new HashMap<>();
        this.gameScoreboard = new GameSupplier();
    }

    /**
     * Initialize the repeated task to update the scoreboards.
     */
    public void init() {
        Bukkit.getScheduler().runTaskTimer(VBSkywars.getInstance(), this, 0L, 2L);
    }

    /**
     * Handle players who joins the minigame and provide them with a scoreboard.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoinGame(PlayerJoinGameEvent event) {
        Player player = event.getPlayer();
        setPlayerGameBoard(player.getUniqueId(), new GameBoard(player));
    }

    /**
     * Handle players who leave the minigame and delete their scoreboard.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerLeaveGameEvent event) {
        GameBoard board = this.playerBoards.remove(event.getPlayer().getUniqueId());
        if (null != board) {
            board.remove();
        }
    }

    /**
     * Handle players who die during the minigame and delete their scoreboard.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(GamePlayerDiedEvent event) {
        GameBoard board = this.playerBoards.remove(event.getPlayer().getUniqueId());
        if (null != board) {
            board.remove();
        }
    }

    /**
     * Set the given board as the player's GameBoard.
     *
     * @param uuid  The UUID of the player
     * @param board The new board to set
     * @see GameBoard
     */
    private void setPlayerGameBoard(UUID uuid, GameBoard board) {
        this.playerBoards.put(uuid, board);
        board.setSidebarVisible(true);
        board.setScoreboardSupplier(this.gameScoreboard);
    }

    /**
     * The runnable method that repeatedly runs and updates the boards.
     */
    @Override
    public void run() {
        for (GameBoard board : this.playerBoards.values()) {
            if (board.getPlayer().isOnline() && !board.getPlayer().isDead() && !board.isRemoved()) {
                board.update();
            }
        }
    }
}