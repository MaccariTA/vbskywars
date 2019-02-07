package net.maccarita.vbskywars.dbs;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.Save;

import java.sql.Connection;

/**
 * An abstract class representing any datasource and what methods he should implement in order
 * to provide a polymorphic code. Implements Save in order to 'promise' Game-related saving and loading abilities.
 *
 * @see Save
 * @see SQLite
 */
public abstract class DataSource implements Save {

    /**
     * Return whether the connection to the database was successful.
     *
     * @return true if the connection was successful.
     */
    public abstract boolean canConnect();

    /**
     * Get a connection to the database.
     *
     * @return The connection
     */
    public abstract Connection getConnection();

    /**
     * Creates the required databases.
     */
    protected abstract void createDatabase();

    /**
     * Creates the required tables.
     */
    protected abstract void createTables();

    /**
     * Get the name of the datasource
     *
     * @return The name of the datasource.
     */
    protected abstract String getName();

    /**
     * A general setup method for any datasource, checking connection and creating the database and tables.
     *
     * @return true if the setup finished successfully.
     */
    public boolean setup() {
        if (canConnect()) {
            createDatabase();
            createTables();
            return true;
        } else {
            VBSkywars.getInstance().getLogger().severe(String.format(Constants.Warning.DB_CONNECTION_ERROR, getName()));
            return false;
        }
    }
}
