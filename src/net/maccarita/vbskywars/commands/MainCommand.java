package net.maccarita.vbskywars.commands;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.consts.Constants;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The main command, handling subcommands and arguments.
 * TODO Implement a better command system.
 */
public class MainCommand implements CommandExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        String subcommand;
        if (!(sender instanceof Player)) {
            sender.sendMessage(Constants.Warning.NOT_PLAYER_ERROR);
            return true;
        }
        Player player = (Player) sender;

        if (0 == args.length) {
            player.sendMessage(Constants.Command.MAIN_COMMAND_USAGE);
            return true;
        }

        if (VBSkywars.getInstance().getGameManager().isInGame(player.getUniqueId())) {
            player.sendMessage(Constants.Warning.PLAYER_IN_GAME);
            return true;
        }

        subcommand = args[0];
        switch (subcommand) {
            case Constants.Command.SETHUB_SUBCOMMAND:
                Commands.setHub(player);
                break;
            case Constants.Command.ARENACREATE_SUBCOMMAND:
                if (2 != args.length) {
                    sender.sendMessage(Constants.Command.MAIN_COMMAND_USAGE);
                } else {
                    Commands.arenaCreate(player, args[1]);
                }
                break;
            case Constants.Command.ARENASPAWN_SUBCOMMAND:
                Commands.arenaSpawn(player);
                break;
            case Constants.Command.ARENACHEST_SUBCOMMAND:
                if (2 != args.length) {
                    sender.sendMessage(Constants.Command.MAIN_COMMAND_USAGE);
                } else {
                    Commands.arenaChest(player, args[1]);
                }
                break;
            case Constants.Command.ARENADELETE_SUBCOMMAND:
                Commands.arenaDelete(player);
                break;
            case Constants.Command.ARENAREADY_SUBCOMMAND:
                Commands.arenaReady(player);
                break;
            case Constants.Command.JOIN_SUBCOMMAND:
                Commands.joinGame(player);
                break;
            default:
                sender.sendMessage(Constants.Command.MAIN_COMMAND_USAGE);
                break;
        }
        // always return true, usage is handled in the plugin.
        return true;
    }
}
