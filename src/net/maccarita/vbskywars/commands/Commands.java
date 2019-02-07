package net.maccarita.vbskywars.commands;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.Arena;
import net.maccarita.vbskywars.arenas.ArenaException;
import net.maccarita.vbskywars.arenas.ArenaState;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.Game;
import net.maccarita.vbskywars.games.GameException;
import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * A class holding static functions in order to implement the commands from {@link MainCommand}.
 */
final class Commands {
    // Class will only be used within package - 'package-private' access modifier

    /**
     * A function for setting the hub location with a command.
     *
     * @param sender The sender of this command.
     */
    static void setHub(Player sender) {
        Location loc = sender.getLocation();
        ConfigurationSection cs = VBSkywars.getInstance().getConfig().getConfigurationSection(Constants.Config.HUB);

        VBUtils.saveLocationToConfig(cs, loc);
        VBSkywars.getInstance().setHubLocation(loc.clone());
        sender.sendMessage(Constants.Command.SETHUB_SUCCESS);
    }

    /**
     * A wrapper function to create an arena with a command.
     *
     * @param sender The sender of this command.
     * @param arg    Max players
     */
    static void arenaCreate(Player sender, String arg) {
        String worldName = sender.getWorld().getName();
        byte maxPlayers;

        if (!arg.matches(Constants.General.NUMERIC_REGEX)) {
            sender.sendMessage(Constants.Warning.NOT_VALID_INT);
            return;
        }

        maxPlayers = Byte.parseByte(arg);

        try {
            VBSkywars.getInstance().getArenaManager().createArena(worldName, maxPlayers);
        } catch (ArenaException e) {
            sender.sendMessage(e.getMessage());
            return;
        }

        sender.sendMessage(Constants.Command.ARENACREATE_SUCCESS);
    }

    /**
     * A wrapper function to add a spawn with a command at the current location of the sender.
     *
     * @param sender The sender of this command.
     */
    static void arenaSpawn(Player sender) {
        Arena arena = null;
        int index = -1;
        try {
            arena = VBSkywars.getInstance().getArenaManager().getArena(sender.getWorld().getName());
        } catch (ArenaException e) {
            sender.sendMessage(e.getMessage());
            return;
        }
        index = arena.getSpawnIndex();
        if (arena.canAddSpawn()) {
            arena.addSpawn(sender.getLocation());
        } else {
            sender.sendMessage(Constants.Warning.ARENA_SPAWNS_FULL);
            return;
        }

        sender.sendMessage(String.format(Constants.Command.ARENASPAWN_SUCCESS, index));
    }

    /**
     * A wrapper function to define an arena chest, at the blocked looked at.
     *
     * @param sender The sender of this command.
     * @param arg    The tier level of the chest.
     */
    static void arenaChest(Player sender, String arg) {
        Arena arena;
        BlockState blockState;
        byte tier;

        try {
            arena = VBSkywars.getInstance().getArenaManager().getArena(sender.getWorld().getName());
        } catch (ArenaException e) {
            sender.sendMessage(e.getMessage());
            return;
        }

        blockState = sender.getTargetBlock(null, 5).getState();
        if (!(blockState instanceof Chest)) {
            sender.sendMessage(Constants.Warning.CHEST_LOOK_ERROR);
            return;
        }

        if (!arg.matches(Constants.General.NUMERIC_REGEX)) {
            sender.sendMessage(Constants.Warning.NOT_VALID_INT);
            return;
        }

        tier = Byte.parseByte(arg);

        if (tier > 2 || tier < 1) {
            sender.sendMessage(Constants.Warning.NOT_VALID_INT);
            return;
        }

        arena.addChest(blockState.getLocation(), tier);
        sender.sendMessage(String.format(Constants.Command.ARENACHEST_SUCESS, tier));
    }

    /**
     * A wrapper function to delete the arena the sender is at, with a command.
     *
     * @param sender The sender of this command.
     */
    static void arenaDelete(Player sender) {
        try {
            VBSkywars.getInstance().getArenaManager().removeArena(sender.getWorld().getName(), true);
        } catch (ArenaException e) {
            sender.sendMessage(e.getMessage());
            return;
        }

        sender.sendMessage(Constants.Command.ARENADELETE_SUCCESS);
    }

    /**
     * A wrapper function to check that all the required information was set, and set the ArenaState to READY.
     *
     * @param sender The sender of this command.
     * @see ArenaState
     */
    static void arenaReady(Player sender) {
        Arena arena;

        try {
            arena = VBSkywars.getInstance().getArenaManager().getArena(sender.getWorld().getName());
        } catch (ArenaException e) {
            sender.sendMessage(e.getMessage());
            return;
        }

        if (ArenaState.UNINITIALIZED != arena.getState()) {
            sender.sendMessage(Constants.Warning.ARENA_ALREADY_INITIALIZED);
            return;
        }

        if (arena.canAddSpawn()) {
            sender.sendMessage(Constants.Warning.ARENA_SPAWNS_NOT_SET);
            return;
        }

        VBSkywars.getInstance().getDatabase().saveArena(arena, true);
        arena.setState(ArenaState.READY);
        sender.sendMessage(Constants.Command.ARENAREADY_SUCCESS);
    }

    /**
     * A wrapper function to join a game, create one if it doesn't exist and queue for the first game available.
     *
     * @param sender The sender of the command.
     */
    static void joinGame(Player sender) {
        Game game = null;
        try {
            game = VBSkywars.getInstance().getGameManager().getFreeGame();
        } catch (GameException e) {
            sender.sendMessage(e.getMessage());
            return;
        }

        game.joinGame(sender.getUniqueId());
        // Player message is in the event listener @see net.maccarita.vbskytwars.games.listeners.JoinListener
    }


}
