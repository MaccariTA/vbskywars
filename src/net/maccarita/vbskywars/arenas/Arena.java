package net.maccarita.vbskywars.arenas;

import lombok.Getter;
import lombok.Setter;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.events.ArenaAddSpawnEvent;
import net.maccarita.vbskywars.arenas.loot.LootChest;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An arena. Holding information about the arena such as maxPlayers, spawnPoints and etc.
 * This object can be safely Gson'ed WHENEVER the arena is in GameState.READY.
 *
 * @see net.maccarita.vbskywars.games.GameState
 */
public class Arena {
    @Getter private final String worldName;
    @Getter private final byte maxPlayers;
    @Getter private byte spawnIndex = 0;
    @Getter private final List<LootChest> chests = new ArrayList<>();
    @Getter private final Location[] spawnPoints;
    @Getter @Setter private transient ArenaState state = ArenaState.UNINITIALIZED;
    @Getter @Setter private transient List<UUID> playersAlive = new ArrayList<>();


    /**
     * Constructor. Package-private, use {@link ArenaManager#createArena(String, byte)}
     *
     * @param worldName
     * @param maxPlayers
     */
    Arena(String worldName, byte maxPlayers) {
        this.worldName = worldName;
        this.maxPlayers = maxPlayers;
        this.spawnPoints = new Location[this.maxPlayers];
    }

    /**
     * Add a spawn at the given location.
     *
     * @param spawn The location to set a spawn at.
     */
    public void addSpawn(Location spawn) {
        ArenaAddSpawnEvent event = new ArenaAddSpawnEvent(this, spawn);
        spawnPoints[spawnIndex] = spawn.clone();
        spawnIndex++;
        VBSkywars.getPluginManager().callEvent(event);
    }

    /**
     * Set the chest at the given location to be a loot chest.
     *
     * @param location The location of the chest.
     * @param tier     The tier level (1 or 2)
     */
    public void addChest(Location location, byte tier) {
        this.chests.add(new LootChest(location, tier));
    }

    /**
     * Check whether a new spawn can be added or were all the spawns set.
     *
     * @return True if a new spawn can be added. false otherwise.
     */
    public boolean canAddSpawn() {
        return spawnIndex < this.spawnPoints.length;
    }


    /**
     * Restart the map at the end of the game.
     */
    public void restart() {
        this.setState(ArenaState.RESTARTING);
        this.setState(ArenaState.READY);
    }

    /**
     * Populate the loot chests set.
     */
    public void populateChests() {
        for (LootChest chest : this.chests) {
            try {
                chest.fill();
            } catch (ArenaException e) {
                VBSkywars.getInstance().getLogger().severe(e.getMessage());
            }
        }

    }
}