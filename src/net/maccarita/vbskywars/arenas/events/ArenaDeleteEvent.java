package net.maccarita.vbskywars.arenas.events;

import lombok.Getter;
import net.maccarita.vbskywars.arenas.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An arena has been deleted.
 */
public class ArenaDeleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final Arena arena;

    public ArenaDeleteEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
