package net.maccarita.vbskywars.arenas.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * A new arena was created.
 */
public class ArenaCreateEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    @Getter private final String worldName;
    @Getter private final int maxPlayers;

    public ArenaCreateEvent(String worldName, int maxPlayers){
        this.worldName = worldName;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
