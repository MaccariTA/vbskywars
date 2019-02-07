package net.maccarita.vbskywars.games.events;

import lombok.Getter;
import lombok.Setter;
import net.maccarita.vbskywars.games.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * A game of Skywars started.
 */
public class GameStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter @Setter private boolean cancelled;
    @Getter private final Game game;

    public GameStartEvent(Game game) {
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
