package net.maccarita.vbskywars.arenas.loot;

import lombok.Getter;
import net.maccarita.vbskywars.arenas.ArenaException;
import net.maccarita.vbskywars.consts.Constants;
import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A loot chest. Fills randomly based on its TierLevel
 *
 * @see TierLevel
 */
public class LootChest {

    @Getter private final Location location;
    @Getter private final TierLevel tier;

    /**
     * Constructor
     *
     * @param location The location of the chest
     * @param tier     The int representation of the TierLevel
     */
    public LootChest(Location location, byte tier) {
        this.location = location;

        switch (tier) {
            case 1:
                this.tier = new TierLevel.TierLevel1();
                break;
            case 2:
                // FALLTHROUGH - assuming 2 by default
            default:
                this.tier = new TierLevel.TierLevel2();
                break;
        }
    }


    /**
     * Fill this chest with random items based on it's TierLevel.
     *
     * @throws ArenaException Item is not a chest.
     */
    public void fill() throws ArenaException {
        Block block = this.location.getBlock();
        BlockState blockState = block.getState();

        if (!(blockState instanceof Chest)) {
            throw new ArenaException(String.format(Constants.Warning.NOT_A_CHEST_ERROR, this.location.toString()));
        }
        Chest chest = (Chest) blockState;
        Inventory inv = chest.getSnapshotInventory();
        List<ItemStack> lootItems = new ArrayList<>();

        for (ItemType type : ItemType.values()) {
            if (type.isMust() || VBUtils.getRandom().nextBoolean()) {
                ItemStack im = this.tier.getItem(type);
                lootItems.add(im);
            }
        }

        inv.clear();
        inv.addItem(lootItems.stream().filter(Objects::nonNull).toArray(ItemStack[]::new));

        // Fisher–Yates shuffle
        for (int i = inv.getSize(); i > 1; i--)
            VBUtils.swap(inv, i - 1, VBUtils.getRandom().nextInt(i));

        // Update chest
        chest.update();
    }
}