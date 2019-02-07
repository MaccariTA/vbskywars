package net.maccarita.vbskywars.games.listeners;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.ArenaState;
import net.maccarita.vbskywars.games.Game;
import net.maccarita.vbskywars.games.GameException;
import net.maccarita.vbskywars.games.GameState;
import net.maccarita.vbskywars.games.events.PlayerLeaveGameEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

/**
 * Listener class responsible for leaving.
 */
public class LeaveListener implements Listener {

    /**
     * Player left the server.
     */
    @EventHandler
    public void leaveServer(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Game game;
        try {
            game = VBSkywars.getInstance().getGameManager().getGameByPlayer(uuid);
        } catch (GameException ignore) {
            // Player is not in game.
            return;
        }
        game.leaveGame(uuid);
    }

    /**
     * Player teleported to the hub from another world (indicates that he probably died)
     */
    @EventHandler
    public void leaveArenaToHub(PlayerTeleportEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();
        Location to = e.getTo();
        Location from = e.getFrom();
        String hubWorld = VBSkywars.getInstance().getHubLocation().getWorld().getName();
        Game game;
        //  assures player teleports to the hub world from another world.
        if (!to.getWorld().getName().equals(hubWorld) && from.getWorld().getName().equals(hubWorld)) {
            return;
        }

        try {
            game = VBSkywars.getInstance().getGameManager().getGameByPlayer(playerUUID);
        } catch (GameException ignore) {
            // Player is not in game.
            return;
        }

        game.leaveGame(playerUUID);
    }

    /**
     * A player left in the middle of the game.
     */
    @EventHandler
    public void leaveGame(PlayerLeaveGameEvent e) {
        Game game = e.getGame();
        ArenaState arenaState = game.getGameArena().getState();
        switch (arenaState) {
            case READY:
                game.getGamePlayers().remove(e.getUuid());
                break;
            case COUNTDOWN:
                game.getGamePlayers().remove(e.getUuid());
                game.setGameState(GameState.FREE);
                game.stopCountdown();
                break;
            case INGAME:
                game.loser(e.getUuid());
                break;
            default:
                break;
        }
    }
}
