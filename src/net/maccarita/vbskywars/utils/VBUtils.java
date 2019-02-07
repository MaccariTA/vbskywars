package net.maccarita.vbskywars.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.maccarita.vbskywars.VBSkywars;
import net.maccarita.vbskywars.arenas.loot.TierLevel;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.utils.gson.LocationAdapter;
import net.maccarita.vbskywars.utils.gson.LootTierAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Class holding utilities methods and fields, related to the plugin.
 */
public class VBUtils {

    @Getter private static final Random random = new Random();
    @Getter private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        builder.registerTypeAdapter(TierLevel.class, new LootTierAdapter());
        gson = builder.create();
    }

    /**
     * Save the given location to the given configuration section.
     *
     * @param config The configratuion section to save to.
     * @param loc    The location to save.
     */
    public static void saveLocationToConfig(ConfigurationSection config, Location loc) {
        config.set(Constants.Config.WORLD, loc.getWorld().getName());
        config.set(Constants.Config.X, loc.getX());
        config.set(Constants.Config.Y, loc.getY());
        config.set(Constants.Config.Z, loc.getZ());
        config.set(Constants.Config.YAW, loc.getYaw());
        config.set(Constants.Config.PITCH, loc.getPitch());
        VBSkywars.getInstance().saveConfig();
    }

    /**
     * Load the location from the given configuration section.
     *
     * @param cs The configuration section to load from.
     * @return The Location that was loaded.
     */
    public static Location loadLocationFromConfig(ConfigurationSection cs) {
        return new Location(Bukkit.getWorld(cs.getString(Constants.Config.WORLD)),
                            cs.getDouble(Constants.Config.X),
                            cs.getDouble(Constants.Config.Y),
                            cs.getDouble(Constants.Config.Z),
                            (float) cs.getDouble(Constants.Config.YAW),
                            (float) cs.getDouble(Constants.Config.PITCH));
    }

    /**
     * Pad the given data with spaces (" ") to the given length.
     *
     * @param data   The data to pad.
     * @param length The length to pad to.
     * @return The string after padding.
     */
    public static String pad(String data, int length) {
        StringBuilder sb = new StringBuilder(data);
        while (length > sb.length()) {
            sb.append(Constants.General.SPACE);
        }
        return sb.toString();
    }

    /**
     * An inventory swap method that swap the items at the given slots (works even when one or both of the slots
     * are empty).
     *
     * <p>
     * Was implemented as a part of Fisher–Yates shuffle to the inventory object.
     *
     * @param inv
     * @param slot
     * @param slot2
     */
    public static void swap(Inventory inv, int slot, int slot2) {
        ItemStack temp = inv.getItem(slot);
        inv.setItem(slot, inv.getItem(slot2) == null ? new ItemStack(Material.AIR) : inv.getItem(slot2));
        inv.setItem(slot2, temp == null ? new ItemStack(Material.AIR) : temp);
    }
}
