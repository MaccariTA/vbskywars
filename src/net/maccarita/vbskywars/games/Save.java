package net.maccarita.vbskywars.games;

import net.maccarita.vbskywars.arenas.Arena;

/**
 * An interface that provides saving capabilities.
 * Used by the datasource to 'promise' implementation for saving and loading.
 *
 * @see net.maccarita.vbskywars.dbs.DataSource
 */
public interface Save {
    /**
     * Save an arena.
     *
     * @param arena The arena to save
     * @param async Whether or not to perform the operation async.
     */
    void saveArena(Arena arena, boolean async);

    /**
     * Remove an arena.
     *
     * @param arena The arena to remove.
     * @param async Whether or not to perform the operation async.
     */
    void removeArena(Arena arena, boolean async);

    /**
     * Load all the arenas saved in the past (that were not removed)
     *
     * @param async Whether or not to perform this operation async.
     */
    void loadArenas(boolean async);
}
