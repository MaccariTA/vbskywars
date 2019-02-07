package net.maccarita.vbskywars.games.events;

import lombok.Getter;
import net.maccarita.vbskywars.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Player died on Skywars.
 */
public class GamePlayerDiedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final UUID uuid;
    @Getter private final Game game;
    @Getter private UUID killer;

    public GamePlayerDiedEvent(UUID uuid, Game game) {
        this.uuid = uuid;
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}
