package net.maccarita.vbskywars.games.listeners;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.ArenaState;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.Game;
import net.maccarita.vbskywars.games.GameException;
import net.maccarita.vbskywars.games.events.GameOverEvent;
import net.maccarita.vbskywars.games.events.GamePlayerDiedEvent;
import net.maccarita.vbskywars.games.events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A listener responsible for the game logic.
 */
public class GameListener implements Listener {

    /**
     * Player died in the middle of a game.
     *
     * @see GamePlayerDiedEvent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UUID playerUUID = e.getEntity().getUniqueId();
        Game game;
        try {
            game = VBSkywars.getInstance().getGameManager().getGameByPlayer(playerUUID);
        } catch (GameException ignore) {
            // Player is not in game.
            return;
        }
        game.loser(playerUUID);
    }

    /**
     * Player died in game, handles game logic.
     */
    @EventHandler
    public void onGamePlayerDeath(GamePlayerDiedEvent e) {
        Game game = e.getGame();
        UUID deadUUID = e.getUuid();
        List<UUID> alive = game.getGameArena().getPlayersAlive();
        Player killer;
        boolean removed = alive.remove(deadUUID);
        if (removed) {
            game.broadcast(true, String.format(Constants.Game.GAME_OVER, Bukkit.getOfflinePlayer(deadUUID).getName()));
            killer = e.getPlayer().getKiller();
            if (null != killer) {
                game.addKill(killer.getUniqueId());
            }
        }

        // Teleport dead player to the hub.
        e.getPlayer().setBedSpawnLocation(VBSkywars.getInstance().getHubLocation(), true);

        if (1 == alive.size()) {
            game.gameOver();
        }
    }

    /**
     * All but one player is left, game is over.
     */
    @EventHandler
    public void gameOver(GameOverEvent e) {
        Game game = e.getGame();
        UUID winner = game.getGameArena().getPlayersAlive().get(0);
        Player winnerPlayer = Bukkit.getPlayer(winner);

        if (null != winnerPlayer) {
            winnerPlayer.sendMessage(String.format(Constants.Game.GAME_WINNER, game.getGameArena().getWorldName()));
            winnerPlayer.teleport(VBSkywars.getInstance().getHubLocation());
        }

        game.getGameArena().restart();
        VBSkywars.getInstance().getGameManager().removeGame(game);
    }

    @EventHandler
    public void startGame(GameStartEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Game game = e.getGame();
        ArrayList<UUID> gamePlayers = (ArrayList<UUID>) game.getGamePlayers();
        game.broadcast(false, Constants.Game.GAME_STARTING);
        //noinspection unchecked
        game.getGameArena().setPlayersAlive((ArrayList<UUID>) gamePlayers.clone());
        game.getGameArena().setState(ArenaState.INGAME);
        game.getGameArena().populateChests();
        game.teleportPlayers();
    }
}


