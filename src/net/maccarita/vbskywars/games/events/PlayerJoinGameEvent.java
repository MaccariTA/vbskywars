package net.maccarita.vbskywars.games.events;

import lombok.Getter;
import lombok.Setter;
import net.maccarita.vbskywars.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Player joined a game of Skywars.
 */
public class PlayerJoinGameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Setter @Getter private boolean cancelled;
    @Getter private final UUID uuid;
    @Getter private final Game game;

    public PlayerJoinGameEvent(UUID uuid, Game game) {
        this.uuid = uuid;
        this.game = game;
        this.cancelled = false;
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
