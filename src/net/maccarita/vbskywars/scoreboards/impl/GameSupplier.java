package net.maccarita.vbskywars.scoreboards.impl;

import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.games.Game;
import net.maccarita.vbskywars.games.GameException;
import net.maccarita.vbskywars.scoreboards.ScoreboardEntry;
import net.maccarita.vbskywars.scoreboards.ScoreboardSupplier;
import net.maccarita.vbskywars.utils.BukkitTimer;
import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the ScoreboardSupplier for the Skywars game.
 *
 * @see ScoreboardSupplier
 */
public class GameSupplier implements ScoreboardSupplier {

    /**
     * {@inheritDoc}
     */
    public String getTitle() {
        return Constants.Scoreboard.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    public List<ScoreboardEntry> getLines(Player player) {
        List<ScoreboardEntry> lines = new ArrayList<>();
        Game game;

        try {
            game = VBSkywars.getInstance().getGameManager().getGameByPlayer(player.getUniqueId());
        } catch (GameException e) {
            VBSkywars.getInstance().getLogger().severe(e.getMessage());
            return lines;
        }

        String playing = String.format(Constants.Scoreboard.PLAYING,
                                       game.getGameArena().getPlayersAlive().size(),
                                       game.getGamePlayers().size());
        String waiting = String.format(Constants.Scoreboard.PLAYING,
                                       game.getGamePlayers().size(),
                                       game.getGameArena().getMaxPlayers());
        String arena = String.format(Constants.Scoreboard.ARENA, game.getGameArena().getWorldName());
        String kills = String.format(Constants.Scoreboard.KILLS, game.getKills(player.getUniqueId()));

        switch (game.getGameArena().getState()) {
            case READY:
                lines.add(new ScoreboardEntry(Constants.Scoreboard.PLAYING_TITLE));
                lines.add(new ScoreboardEntry(waiting));
                break;
            case COUNTDOWN:
                BukkitTimer gameTimer = game.getTimer();
                String countdown = String.format(Constants.Scoreboard.COUNTDOWN,
                                                 gameTimer != null ? gameTimer.getTime() : -1);
                lines.add(new ScoreboardEntry(Constants.Scoreboard.PLAYING_TITLE));
                lines.add(new ScoreboardEntry(waiting));
                lines.add(new ScoreboardEntry(VBUtils.pad(Constants.Scoreboard.BLANK, 1)));
                lines.add(new ScoreboardEntry(Constants.Scoreboard.COUNTDOWN_TITLE));
                lines.add(new ScoreboardEntry(countdown));
                break;
            case INGAME:
                lines.add(new ScoreboardEntry(Constants.Scoreboard.PLAYING_TITLE));
                lines.add(new ScoreboardEntry(playing));
                lines.add(new ScoreboardEntry(VBUtils.pad(Constants.Scoreboard.BLANK, 2)));
                lines.add(new ScoreboardEntry(Constants.Scoreboard.ARENA_TITLE));
                lines.add(new ScoreboardEntry(arena));
                lines.add(new ScoreboardEntry(VBUtils.pad(Constants.Scoreboard.BLANK, 3)));
                lines.add(new ScoreboardEntry(Constants.Scoreboard.KILLS_TITLE));
                lines.add(new ScoreboardEntry(kills));
                break;
            default:
                break;
        }

        return lines;
    }
}