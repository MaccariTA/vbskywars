package net.maccarita.vbskywars.arenas.events;

import lombok.Getter;
import net.maccarita.vbskywars.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * A new spawn was created.
 */
public class ArenaAddSpawnEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final Location spawn;
    @Getter private final Arena arena;

    public ArenaAddSpawnEvent(Arena arena, Location spawn){
        this.arena = arena;
        this.spawn = spawn;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
