package net.maccarita.vbskywars.arenas;

import lombok.Getter;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.arenas.events.ArenaCreateEvent;
import net.maccarita.vbskywars.arenas.events.ArenaDeleteEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The arena manager, manage all the arenas, create arenas, remove arenas, and call the appropriate events.
 */
public class ArenaManager {
    @Getter private final List<Arena> arenas = new ArrayList<>();

    /**
     * Create an arena.
     *
     * @param worldName  The world to create at.
     * @param maxPlayers Max players in the arena.
     * @return The arena.
     * @throws ArenaException The world already has an arena.
     */
    public Arena createArena(String worldName, byte maxPlayers) throws ArenaException {
        ArenaCreateEvent ac = new ArenaCreateEvent(worldName, maxPlayers);
        Arena arena = new Arena(worldName, maxPlayers);

        if (this.arenas.stream().anyMatch(eachArena -> eachArena.getWorldName().equals(worldName))) {
            throw new ArenaException(String.format(Constants.Warning.WORLD_HAS_ARENA_EXCEPTION, worldName));
        }
        this.arenas.add(arena);
        VBSkywars.getPluginManager().callEvent(ac);
        return arena;
    }

    /**
     * Get an arena by world.
     *
     * @param worldName The world name.
     * @return The arena.
     * @throws ArenaException That world doesn't has an arena.
     */
    public Arena getArena(String worldName) throws ArenaException {
        return this.arenas.stream()
                          .filter(arena -> arena.getWorldName().equals(worldName))
                          .findFirst()
                          .orElseThrow(() -> new ArenaException(String.format(Constants.Warning.WORLD_HAS_NO_ARENA_EXCEPTION, worldName)));
    }

    /**
     * Remove an arena.
     *
     * @param worldName The name of the world in which the arena is at.
     * @param async     Whether to perform this operation async.
     * @throws ArenaException The world doesn't has an arena.
     */
    public void removeArena(String worldName, boolean async) throws ArenaException {
        Arena arena = getArena(worldName);
        ArenaDeleteEvent ad = new ArenaDeleteEvent(arena);
        this.arenas.remove(arena);
        VBSkywars.getInstance().getDatabase().removeArena(arena, async);
        VBSkywars.getPluginManager().callEvent(ad);
    }

    /**
     * Load all the arenas from the datasource.
     *
     * @param async Whether to perform the operation async.
     */
    public void loadArenas(boolean async) {
        VBSkywars.getInstance().getDatabase().loadArenas(async);
    }


    /**
     * Save all the arenas to the datasource.
     *
     * @param async Whether to perform the operation async.
     */
    public void saveArenas(boolean async) {
        this.arenas.forEach((arena -> VBSkywars.getInstance().getDatabase().saveArena(arena, async)));
    }

    /**
     * Get the first arena that has a ready state. null if none exist.
     *
     * @return The first ready arena, or null if none exist.
     */
    public Arena getFirstReady() {
        return this.arenas.stream().filter(arena -> arena.getState() == ArenaState.READY).findFirst().orElse(null);
    }
}
