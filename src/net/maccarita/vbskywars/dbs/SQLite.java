package net.maccarita.vbskywars.dbs;

import lombok.AllArgsConstructor;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.Arena;
import net.maccarita.vbskywars.arenas.ArenaState;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

@AllArgsConstructor
public class SQLite extends DataSource {
    private static final String name = "SQLite";
    private String fileName;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConnect() {
        try (Connection connection = getConnection()) {
            if (null != connection) {
                return true;
            }
        } catch (SQLException ignore) {
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * No need for connection pool in SQLite as it uses a file, and is connection-less.
     */
    @Override
    public Connection getConnection() {
        Connection connection = null;
        String jdbcURL = String.format(Constants.Database.SQLITE_CONNECTION_STRING,
                                       Paths.get(VBSkywars.getInstance().getDataFolder().getAbsolutePath(), this.fileName).toString());

        try {
            connection = DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            VBSkywars.getInstance().getLogger().severe(e.getMessage());
        }
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createDatabase() {
        // SQLite's database is a file, already created on connect - no need to create one.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createTables() {
        try (Connection connection = getConnection()) {
            connection.prepareStatement(Constants.Database.SQLITE_CREATE_TABLE).executeUpdate();
            VBSkywars.getInstance().getLogger().info(Constants.Database.SQLITE_TABLE_SUCCESS);
        } catch (SQLException e) {
            VBSkywars.getInstance().getLogger().severe(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveArena(Arena arena, boolean async) {
        BukkitRunnable saveRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(Constants.Database.SQLITE_INSERT_ARENA);
                    statement.setString(1, arena.getWorldName());
                    statement.setString(2, VBUtils.getGson().toJson(arena));
                    statement.executeUpdate();
                    VBSkywars.getInstance().getLogger().info(Constants.Database.SQLITE_INSERTARENA_SUCCESS);
                } catch (SQLException e) {
                    VBSkywars.getInstance().getLogger().severe(e.getMessage());
                }
            }
        };

        if (async) {
            saveRunnable.runTaskAsynchronously(VBSkywars.getInstance());
        } else {
            saveRunnable.run();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeArena(Arena arena, boolean async) {
        BukkitRunnable removeRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(Constants.Database.SQLITE_DELETE_ARENA);
                    statement.setString(1, arena.getWorldName());
                    statement.executeUpdate();
                    VBSkywars.getInstance().getLogger().info(Constants.Database.SQLITE_DELETEARENA_SUCCESS);
                } catch (SQLException e) {
                    VBSkywars.getInstance().getLogger().severe(e.getMessage());
                }
            }
        };

        if (async) {
            removeRunnable.runTaskAsynchronously(VBSkywars.getInstance());
        } else {
            removeRunnable.run();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadArenas(boolean async) {
        BukkitRunnable arenaLoaderRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    ResultSet results = connection.prepareStatement(Constants.Database.SQLITE_SELECT_ARENAS).executeQuery();
                    while (results.next()) {
                        String arenaJson = results.getString("ArenaJson");
                        Arena arena = VBUtils.getGson().fromJson(arenaJson, Arena.class);
                        // Set transient variables manually.
                        arena.setState(ArenaState.READY);
                        arena.setPlayersAlive(new ArrayList<>());
                        VBSkywars.getInstance().getArenaManager().getArenas().add(arena);
                    }
                    VBSkywars.getInstance().getLogger().info(Constants.Database.SQLITE_SELECTARENA_SUCCESS);
                } catch (SQLException e) {
                    VBSkywars.getInstance().getLogger().severe(e.getMessage());
                }
            }
        };

        if (async) {
            arenaLoaderRunnable.runTaskAsynchronously(VBSkywars.getInstance());
        } else {
            arenaLoaderRunnable.run();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setup() {
        boolean basicSetup = super.setup();
        if (!basicSetup) {
            return false;
        }
        // Enable WAL mode for SQLite
        String result = null;
        try (Connection connection = getConnection()) {
            // No need for async task on setup.
            ResultSet rs = connection.prepareStatement(Constants.Database.SQLITE_WAL_QUERY).executeQuery();
            if (rs.next()) {
                result = rs.getString(Constants.Database.SQLITE_COLUMN_REUSLT);
            }
            if (null != result && !result.equals(Constants.Database.SQLITE_WAL_RESULT)) {
                VBSkywars.getInstance().getLogger().severe(String.format(Constants.Warning.WAL_SETUP_ERROR, result));
                return false;
            }
            VBSkywars.getInstance().getLogger().info(Constants.Database.SQLITE_WAL_SUCCESS);
        } catch (SQLException e) {
            VBSkywars.getInstance().getLogger().severe(e.getMessage());
            return false;
        }
        return true;
    }


}
