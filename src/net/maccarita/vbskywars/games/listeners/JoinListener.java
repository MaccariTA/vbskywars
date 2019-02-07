package net.maccarita.vbskywars.games.listeners;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.Game;
import net.maccarita.vbskywars.games.GameState;
import net.maccarita.vbskywars.games.events.PlayerJoinGameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Listener class responsible for leaving.
 */
public class JoinListener implements Listener {

    /**
     * Player joins a specific game.
     */
    @EventHandler
    public void playerJoinGame(PlayerJoinGameEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Game game = e.getGame();
        game.getGamePlayers().add(e.getUuid());
        int currentPlayers = game.getGamePlayers().size();
        int maxPlayers = game.getGameArena().getMaxPlayers();
        e.getPlayer().sendMessage(String.format(Constants.Command.JOIN_SUCCESS,
                                                game.getGameArena().getWorldName(),
                                                currentPlayers,
                                                maxPlayers));
        if (currentPlayers == maxPlayers) {
            game.setGameState(GameState.FULL);
            game.startCountdown();
        }
    }

    /**
     * Player joins Minecraft and is not in game.
     */
    @EventHandler
    public void playerJoinMinecraft(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        boolean inGame = VBSkywars.getInstance().getGameManager().isInGame(player.getUniqueId());

        if (!inGame) {
            // Must be delayed. Can't teleport player on login without delay.
            // #setJoinLocation sounds like a good addition to the event :P
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(VBSkywars.getInstance().getHubLocation());
                }
            }.runTaskLater(VBSkywars.getInstance(), 1L);
        }
    }
}
