package net.maccarita.vbskywars.games;

import lombok.Getter;
import lombok.Setter;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.Arena;
import net.maccarita.vbskywars.arenas.ArenaState;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.events.*;
import net.maccarita.vbskywars.utils.BukkitTimer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The game class, holding game information, players, arena, kills, and more information
 * related to the Game.
 */
public class Game {
    private static final int countdown;
    private static int counter = 0;
    @Getter private BukkitTimer timer = null;
    @Getter private int id;
    @Getter private Arena gameArena;
    @Getter @Setter private GameState gameState = GameState.FREE;
    @Getter private final List<UUID> gamePlayers = new ArrayList<>();
    private final Map<UUID, Integer> kills = new HashMap<>();

    static {
        FileConfiguration config = VBSkywars.getInstance().getConfig();
        countdown = config.getInt(Constants.Config.COUNTDOWN_PATH);
    }

    /**
     * Constructor. Use {@link GameManager#createGame(Arena)}
     *
     * @param arena
     */
    Game(Arena arena) {
        this.gameArena = arena;
        this.id = ++counter;
    }

    /**
     * Broadcast a message to the Game
     *
     * @param alive   Only alive players?
     * @param message The message to broadcast.
     */
    public void broadcast(boolean alive, String message) {
        List<UUID> all = alive ? this.getGameArena().getPlayersAlive() : this.gamePlayers;
        for (UUID playerUUID : all) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (null == player) {
                // Player left Minecraft..
                continue;
            }
            player.sendMessage(message);
        }
    }

    /**
     * Teleport players to the spawn points.
     */
    public void teleportPlayers() {
        int i = 0;
        for (UUID uuid : this.getGamePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (null == player) {
                continue;
            }
            player.teleport(this.gameArena.getSpawnPoints()[i++]);
        }
    }

    /**
     * Player joins game. Calls an event that handles the implementation.
     *
     * @param uuid The player uuid.
     * @see PlayerJoinGameEvent
     */
    public void joinGame(UUID uuid) {
        PlayerJoinGameEvent pjEvent = new PlayerJoinGameEvent(uuid, this);
        VBSkywars.getPluginManager().callEvent(pjEvent);
    }

    /**
     * Player leaves game. Calls an event that handles the implementation.
     *
     * @param uuid The player uuid.
     * @see PlayerLeaveGameEvent
     */
    public void leaveGame(UUID uuid) {
        PlayerLeaveGameEvent plEvent = new PlayerLeaveGameEvent(uuid, this);
        VBSkywars.getPluginManager().callEvent(plEvent);
    }

    /**
     * Starting game countdown. Setting the states and broadcasting the players.
     */
    public void startCountdown() {
        if (ArenaState.READY != this.gameArena.getState()) {
            return;
        }
        this.gameArena.setState(ArenaState.COUNTDOWN);
        timer = new BukkitTimer(this::startGame, countdown);
        broadcast(false, String.format(Constants.Game.GAME_COUNTDOWN_START, countdown));
        timer.start();
    }

    /**
     * Stop the game's countdown and broadcast the players.
     */
    public void stopCountdown() {
        this.timer.stop();
        this.gameArena.setState(ArenaState.READY);
        broadcast(false, String.format(Constants.Game.GAME_COUNTDOWN_STOP, this.gameArena.getMaxPlayers() - this.gamePlayers.size()));
    }

    /**
     * Start the game
     */
    public void startGame() {
        GameStartEvent gsEvent = new GameStartEvent(this);
        VBSkywars.getPluginManager().callEvent(gsEvent);
    }

    /**
     * Player lost the game. Calls an event that handles the implementation.
     *
     * @param uuid
     * @see GamePlayerDiedEvent
     */
    public void loser(UUID uuid) {
        GamePlayerDiedEvent pdEvent = new GamePlayerDiedEvent(uuid, this);
        VBSkywars.getPluginManager().callEvent(pdEvent);
    }

    /**
     * Add a killer to a player. Kills were implemeneted here to save a lot of code for 'GamePlayer' management.
     *
     * @param killer
     */
    public void addKill(UUID killer) {
        if (this.kills.containsKey(killer)) {
            this.kills.put(killer, this.kills.get(killer) + 1);
        } else {
            this.kills.put(killer, 1);
        }
    }

    /**
     * Game is over. Calls an event that handles the implementation.
     *
     * @see GameOverEvent
     */
    public void gameOver() {
        GameOverEvent goEvent = new GameOverEvent(this);
        VBSkywars.getPluginManager().callEvent(goEvent);
    }

    /**
     * Get the amount of kills a player has in this game.
     *
     * @param uuid The player UUID
     * @return The amount of kills the player has in this specific game.
     */
    public int getKills(UUID uuid) {
        if (!this.kills.containsKey(uuid)) {
            return 0;
        }
        return this.kills.get(uuid);
    }
}
