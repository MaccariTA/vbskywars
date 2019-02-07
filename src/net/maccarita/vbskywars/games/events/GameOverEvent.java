package net.maccarita.vbskywars.games.events;

import lombok.Getter;
import net.maccarita.vbskywars.games.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * A game of Skywars ended.
 */
public class GameOverEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final Game game;

    public GameOverEvent(Game game) {
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
