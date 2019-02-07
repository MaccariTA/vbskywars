package net.maccarita.vbskywars;

import lombok.Getter;
import lombok.Setter;
import net.maccarita.vbskywars.arenas.ArenaManager;
import net.maccarita.vbskywars.commands.MainCommand;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.dbs.DataSource;
import net.maccarita.vbskywars.dbs.SQLite;
import net.maccarita.vbskywars.games.GameManager;
import net.maccarita.vbskywars.games.listeners.GameListener;
import net.maccarita.vbskywars.games.listeners.JoinListener;
import net.maccarita.vbskywars.games.listeners.LeaveListener;
import net.maccarita.vbskywars.scoreboards.ScoreboardManager;
import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents the main Skywars plugin instance.
 *
 * @author MaccariTA
 */
public class VBSkywars extends JavaPlugin {
    @Getter private static VBSkywars instance;
    @Getter private static PluginManager pluginManager;

    @Getter private DataSource database;
    @Getter @Setter private Location hubLocation;
    @Getter private ArenaManager arenaManager;
    @Getter private GameManager gameManager;
    @Getter private ScoreboardManager scoreboardManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        instance = this;
        pluginManager = this.getServer().getPluginManager();

        this.saveDefaultConfig();

        if (!this.setupDatabase()) {
            pluginManager.disablePlugin(this);
            return;
        }

        this.setupCommands();
        this.loadHubLocation();
        this.loadArenaManager();
        this.loadGameManager();
        this.registerListeners();
        this.setupScoreboardHandler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        this.arenaManager.saveArenas(false);
    }

    /**
     * Setup the database instance, taking values from the configuration file,
     * and initialize the database type according to this value.
     *
     * @return Whether the setup was successful or not.
     */
    private boolean setupDatabase() {
        String type = this.getConfig().getString(Constants.Config.DB_TYPE_PATH);

        switch (type.toLowerCase()) {
            case Constants.Database.SQLITE:
                this.database = new SQLite(this.getConfig().getString(Constants.Config.DB_FILENAME_PATH));
                break;
        }
        return this.database.setup();
    }

    /**
     * Register all the commands, in our case, registers the MainCommand
     * that handles the subcommands.
     */
    private void setupCommands() {
        this.getCommand(Constants.Command.MAIN_COMMAND).setExecutor(new MainCommand());
    }

    /**
     * Loads the hub location from the configuration file.
     */
    private void loadHubLocation() {
        ConfigurationSection cs = this.getConfig().getConfigurationSection(Constants.Config.HUB);
        Location hubLocation = VBUtils.loadLocationFromConfig(cs);
        this.setHubLocation(hubLocation);
    }

    /**
     * Initializes the ArenaManager, as well as loads the arenas from the database, that was initialized earlier on.
     * @see #setupDatabase
     */
    private void loadArenaManager() {
        this.arenaManager = new ArenaManager();
        this.arenaManager.loadArenas(true);
    }

    /**
     * Initializes the minigame manager.
     */
    private void loadGameManager() {
        this.gameManager = new GameManager();
    }

    /**
     * Register all the general listeners.
     */
    private void registerListeners() {
        Listener[] listeners = new Listener[]{
                new GameListener(),
                new JoinListener(),
                new LeaveListener()
        };
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    /**
     * Initialize and setup everything related to the scoreboard.
     * Running the scoreboardManager task, as well as registering its events.
     */
    private void setupScoreboardHandler() {
        this.scoreboardManager = new ScoreboardManager();
        this.scoreboardManager.init();
        pluginManager.registerEvents(this.scoreboardManager, this);
    }

}
