package net.maccarita.vbskywars.consts;

import org.bukkit.ChatColor;

/**
 * A class holding all the String constants for ease of use, simple classification and modularity.
 */
public final class Constants {
    private static final String WARN = ChatColor.RED + "" + ChatColor.BOLD;
    private static final String SUCCESS = ChatColor.DARK_GREEN + "" + ChatColor.BOLD;
    private static final String DARK_BLUE = ChatColor.DARK_BLUE + "" + ChatColor.BOLD;
    private static final String GOLD = ChatColor.GOLD + "" + ChatColor.BOLD;
    private static final String YELLOW = ChatColor.YELLOW + "" + ChatColor.BOLD;

    public static class General {
        public static final String NUMERIC_REGEX = "[1-9]\\d*";
        public static final String PLUGIN_NAME = "VBSkywars";
        public static final String SPACE = " ";
    }

    public static class Game {
        public static final String GAME_OVER = WARN + "It's a game over for %s :(";
        public static final String GAME_WINNER = SUCCESS + "Congratulations for winning VBSkywars on '%s'.";

        public static final String GAME_COUNTDOWN_START = SUCCESS + "Game is now full, and will start in %d seconds.";
        public static final String GAME_COUNTDOWN_STOP = WARN + "Someone left, waiting for %d more players..";
        public static final String GAME_STARTING = SUCCESS + "Game is now starting...";
    }

    public static class Config {
        public static final String DB_TYPE_PATH = "db.type";
        public static final String DB_FILENAME_PATH = "db.filename";

        public static final String COUNTDOWN_PATH = "game.countdown";

        public static final String HUB = "hub";
        public static final String WORLD = "world";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String Z = "z";
        public static final String YAW = "yaw";
        public static final String PITCH = "pitch";
    }

    public static class Database {
        public static final String SQLITE = "sqlite";
        public static final String SQLITE_CONNECTION_STRING = "jdbc:sqlite:%s";
        public static final String SQLITE_WAL_QUERY = "PRAGMA journal_mode = WAL";
        public static final String SQLITE_COLUMN_REUSLT = "journal_mode";
        public static final String SQLITE_WAL_RESULT = "wal";
        public static final String SQLITE_WAL_SUCCESS = "Successfully set 'WAL' mode to SQLite.";
        public static final String SQLITE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS arenas (\n"
                                                         + "\tWorld TEXT(16) NOT NULL PRIMARY KEY,\n"
                                                         + "\tArenaJson TEXT NOT NULL\n"
                                                         + ");";
        public static final String SQLITE_TABLE_SUCCESS = "Successfully created tables.";
        public static final String SQLITE_INSERT_ARENA = "INSERT OR IGNORE INTO arenas\n"
                                                         + "(World, ArenaJson)\n"
                                                         + "VALUES(?, ?)";
        public static final String SQLITE_INSERTARENA_SUCCESS = "Successfully inserted an arena.";
        public static final String SQLITE_DELETE_ARENA = "DELETE FROM arenas\n"
                                                         + "WHERE World=?";
        public static final String SQLITE_DELETEARENA_SUCCESS = "Successfully removed an arena.";
        public static final String SQLITE_SELECT_ARENAS = "SELECT ArenaJson\n"
                                                          + "FROM arenas;\n";
        public static final String SQLITE_SELECTARENA_SUCCESS = "Successfully loaded arenas.";

    }

    public static class Warning {

        public static final String DB_CONNECTION_ERROR = "Could not connect to database: %s";
        public static final String NOT_PLAYER_ERROR = WARN + "You must be a player to use this command.";
        public static final String WAL_SETUP_ERROR = "Failed to setup WAL: %s";
        public static final String WORLD_HAS_ARENA_EXCEPTION = WARN + "The world '%s' already has an arena.";
        public static final String WORLD_HAS_NO_ARENA_EXCEPTION = WARN + "The world '%s' is not an arena.";
        public static final String NOT_VALID_INT = WARN + "Please choose a valid int value.";
        public static final String ARENA_SPAWNS_FULL = WARN + String.format("All spawn points are already set for this arena. '/%s %s' to recreate.", Command.MAIN_COMMAND, Command.ARENADELETE_SUBCOMMAND);
        public static final String CHEST_LOOK_ERROR = WARN + "Please look at a chest when executing this command.";
        public static final String NOT_A_CHEST_ERROR = WARN + "Chest at '%s' is not set. Recreate the arena.";
        public static final String ARENA_ALREADY_INITIALIZED = WARN + "Arena assigned to this world is initialized.";
        public static final String ARENA_SPAWNS_NOT_SET = WARN + "You must set all the spawns first.";
        public static final String NO_AVAILABLE_GAMES = WARN + "Sorry. There are no available games or arenas at the moment.";
        public static final String PLAYER_NOT_IN_GAME = WARN + "This player is not in a game.";
        public static final String PLAYER_IN_GAME = WARN + "You can't execute this command while playing.";


    }

    // TODO Automatically generate these based of permissions and commands, implementing a better command system.
    public static class Command {
        public static final String MAIN_COMMAND = "vbsw";
        public static final String MAIN_COMMAND_USAGE = String.format("%sUSAGE:%s /%s <cmd>\n"
                                                                      + "\n"
                                                                      + "%sSupported Command:%s\n"
                                                                      + "sethub () - Set the current location as the hub.\n"
                                                                      + "arenacreate (max_players) - Create an arena in this world.\n"
                                                                      + "arenaspawn () - Add the current location as an arena spawn location.\n"
                                                                      + "arenachest (tier[1,2]) - Mark a chest as a loot chest.\n"
                                                                      + "arenaready () - Initializing the arena to 'ready' mode.\n"
                                                                      + "arenadelete () - Delete the arena assigned to this world.\n"
                                                                      + "join () - Join the first available game.",
                                                                      ChatColor.RED + "" + ChatColor.BOLD,
                                                                      ChatColor.RESET,
                                                                      MAIN_COMMAND,
                                                                      ChatColor.BOLD, ChatColor.RESET);

        public static final String SETHUB_SUBCOMMAND = "sethub";
        public static final String SETHUB_SUCCESS = SUCCESS + "Sucessfully set hub.";

        public static final String ARENACREATE_SUBCOMMAND = "arenacreate";
        public static final String ARENACREATE_SUCCESS = SUCCESS + "Sucessfully created an arena.";

        public static final String ARENASPAWN_SUBCOMMAND = "arenaspawn";
        public static final String ARENASPAWN_SUCCESS = SUCCESS + "Spawn %d set.";

        public static final String ARENACHEST_SUBCOMMAND = "arenachest";
        public static final String ARENACHEST_SUCESS = SUCCESS + "Added as an arena chest of tier %d.";

        public static final String ARENADELETE_SUBCOMMAND = "arenadelete";
        public static final String ARENADELETE_SUCCESS = SUCCESS + "Arena deleted successfully.";

        public static final String ARENAREADY_SUBCOMMAND = "arenaready";
        public static final String ARENAREADY_SUCCESS = SUCCESS + "Arena is now ready.";

        public static final String JOIN_SUBCOMMAND = "join";
        public static final String JOIN_SUCCESS = SUCCESS + "You have joined the game at '%s' %d/%d.";

    }

    public static class Scoreboard {
        public static final String TITLE = DARK_BLUE + General.PLUGIN_NAME;
        public static final String BLANK = "";
        public static final String PLAYING_TITLE = GOLD + "Currently:";
        public static final String PLAYING = YELLOW + "%d/%d";
        public static final String ARENA_TITLE = GOLD + "Arena:";
        public static final String ARENA = YELLOW + "%s";
        public static final String KILLS_TITLE = GOLD + "Kills:";
        public static final String KILLS = YELLOW + "%d";
        public static final String COUNTDOWN_TITLE = GOLD + "Starting in:";
        public static final String COUNTDOWN = YELLOW + "%d";
    }

    // Preventing class from being initialized.
    private Constants() {
    }
}
