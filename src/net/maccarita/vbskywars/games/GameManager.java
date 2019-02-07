package net.maccarita.vbskywars.games;

import lombok.Getter;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.Arena;
import net.maccarita.vbskywars.consts.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The game manager, manage all the games, create games, remove games, and call the appropriate events.
 */
public class GameManager {
    @Getter private final List<Game> games = new ArrayList<>();

    /**
     * Creating a new Game.
     *
     * @param arena The game arena.
     * @return The game
     */
    public Game createGame(Arena arena) {
        Game game = new Game(arena);

        games.add(game);
        return game;
    }

    /**
     * Removing a game
     *
     * @param game The game to remove
     */
    public void removeGame(Game game) {
        this.games.remove(game);
    }

    /**
     * Get the game a player is playing
     *
     * @param playerUUID The player's UUID
     * @return The game
     * @throws GameException The player is not in a game.
     */
    public Game getGameByPlayer(UUID playerUUID) throws GameException {
        Game playerGame = null;
        for (Game game : this.games) {
            boolean inThisGame = game.getGamePlayers().stream().anyMatch(uuid -> uuid.equals(playerUUID));
            if (inThisGame) {
                playerGame = game;
                break;
            }
        }

        if (null == playerGame) {
            throw new GameException(Constants.Warning.PLAYER_NOT_IN_GAME);
        }

        return playerGame;
    }

    /**
     * Return whether the player is in a game
     *
     * @param playerUUID The player UUID
     * @return true if the player is in a game. false otherwise.
     */
    public boolean isInGame(UUID playerUUID) {
        try {
            getGameByPlayer(playerUUID);
            return true;
        } catch (GameException ignore) {
            // Player is not in a game
        }
        return false;
    }

    /**
     * Return the first game available, creates one if no games are available.
     *
     * @return The game
     * @throws GameException No games are available and there is no free arena for a new game.
     */
    public Game getFreeGame() throws GameException {
        Game freeGame = this.games.stream()
                                  .filter(game -> game.getGameState() == GameState.FREE)
                                  .findFirst()
                                  .orElse(null);
        if (null != freeGame) {
            return freeGame;
        }

        Arena arena = VBSkywars.getInstance().getArenaManager().getFirstReady();
        if (null == arena) {
            throw new GameException(Constants.Warning.NO_AVAILABLE_GAMES);
        }
        return createGame(arena);
    }
}
