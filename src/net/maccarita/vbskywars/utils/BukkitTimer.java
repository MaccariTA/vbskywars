package net.maccarita.vbskywars.utils;

import lombok.Getter;
import net.maccarita.vbskywars.VBSkywars;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Represents a Timer in Bukkit (counting with ticks)
 * The class was meant to countdown seconds and can be used very easily, initialized with a <code>callback</code> and
 * countdown <code>time</code>.
 */
public class BukkitTimer {
    private static final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    @Getter private int time;
    private int taskId;
    private final Runnable callback;

    /**
     * Constructor
     *
     * @param callback The callback function to invoke at the end of the timer.
     * @param time     The countdown time (in seconds)
     */
    public BukkitTimer(Runnable callback, int time) {
        this.callback = callback;
        this.time = time;
    }

    /**
     * Start this timer object countdown. Can be stopped with {@link #stop()}
     *
     * @see #stop
     */
    public void start() {
        this.taskId = scheduler.scheduleSyncRepeatingTask(VBSkywars.getInstance(), () -> {
            if (0 == time) {
                callback.run();
                stop();
            }
            time--;
        }, 0L, 20L);
    }

    /**
     * Stops this timer object countdown. You should be able to call {@link #start()} again to resume.
     *
     * @see #start
     */
    public void stop() {
        scheduler.cancelTask(this.taskId);
    }
}
