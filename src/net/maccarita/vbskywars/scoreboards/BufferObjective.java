package net.maccarita.vbskywars.scoreboards;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class BufferObjective {
    private final Scoreboard scoreboard;
    private Set<String> previousLines;
    private final HashMap<Integer, ScoreboardEntry> contents;
    private boolean requiresUpdate;
    private String title;
    private final Objective current;
    private DisplaySlot displaySlot;

    /**
     * Constructor
     * @param scoreboard The scoreboard to build the object for.
     */
    public BufferObjective(Scoreboard scoreboard) {
        this.previousLines = new HashSet<>();
        this.contents = new HashMap<>();
        this.requiresUpdate = false;
        this.scoreboard = scoreboard;
        this.title = "dummy";
        this.current = scoreboard.registerNewObjective("objective", "dummy", this.title);
    }

    /**
     * Set the objective title.
     * @param title
     */
    public void setTitle(String title) {
        if (this.title == null || !this.title.equals(title)) {
            this.title = title;
            this.requiresUpdate = true;
        }
    }

    /**
     * Change the DisplaySlot to the given slot.
     * @param slot The new DisplaySlot
     */
    public void setDisplaySlot(DisplaySlot slot) {
        this.displaySlot = slot;
        this.current.setDisplaySlot(slot);
    }

    /**
     * Set the lines in the objective for the GameBoard.
     * @param lines The new lines to put.
     */
    public void setAllLines(List<ScoreboardEntry> lines) {
        if (lines.size() != this.contents.size()) {
            this.contents.clear();
            if (lines.isEmpty()) {
                this.requiresUpdate = true;
                return;
            }
        }
        int size = Math.min(16, lines.size());
        int count = 0;
        for (ScoreboardEntry scoreboardEntry : lines) {
            this.setLine(size - count++, scoreboardEntry);
        }
    }

    /**
     * Set a specific line in the objective for the GameBoard.
     * @param lineNumber The number of the line.
     * @param scoreboardEntry The entry.
     */
    private void setLine(int lineNumber, ScoreboardEntry scoreboardEntry) {
        ScoreboardEntry value = this.contents.get(lineNumber);
        if (value == null || !value.equals(scoreboardEntry)) {
            this.contents.put(lineNumber, scoreboardEntry);
            this.requiresUpdate = true;
        }
    }

    /**
     * Flip the old values (the old objective) with the new values (the new objective), removing duplicates.
     */
    public void flip() {
        if (!this.requiresUpdate) {
            return;
        }
        Set<String> newEntries = new HashSet<>();
        this.contents.forEach((i, scoreboardEntry) -> {
            String name = scoreboardEntry.getName();
            if (name.length() > 16) {
                name = name.substring(0, 16);
            }
            Team team = BufferObjective.this.scoreboard.getTeam(name);
            if (team == null) {
                team = BufferObjective.this.scoreboard.registerNewTeam(name);
            }
            String prefix = scoreboardEntry.getPrefix();
            if (prefix != null) {
                if (prefix.length() > 16) {
                    prefix = prefix.substring(0, 16);
                }
                if (!team.getPrefix().equals(prefix)) {
                    team.setPrefix(prefix);
                }
            }
            String suffix = scoreboardEntry.getSuffix();
            if (suffix != null) {
                if (suffix.length() > 16) {
                    suffix = suffix.substring(0, 16);
                }
                if (!team.getSuffix().equals(suffix)) {
                    team.setSuffix(suffix);
                }
            }
            newEntries.add(name);
            if (!team.hasEntry(name)) {
                team.addEntry(name);
            }
            this.current.getScore(name).setScore(i);
        });

        this.previousLines.removeAll(newEntries);
        Iterator<String> iterator = this.previousLines.iterator();

        while (iterator.hasNext()) {
            String last = iterator.next();
            Team team = this.scoreboard.getTeam(last);
            if (team != null && team.getEntries().contains(last)) {
                team.removeEntry(last);
            }
            this.scoreboard.resetScores(last);
            iterator.remove();
        }
        this.previousLines = newEntries;
        this.current.setDisplayName(this.title);
        this.requiresUpdate = false;
    }

    /**
     * Change the visibility
     * @param value True for visibile, false otherwise.
     */
    public void setVisible(boolean value) {
        if (this.displaySlot != null && !value) {
            this.scoreboard.clearSlot(this.displaySlot);
            this.displaySlot = null;
        } else if (this.displaySlot == null && value) {
            this.current.setDisplaySlot(this.displaySlot = DisplaySlot.SIDEBAR);
        }
    }
}