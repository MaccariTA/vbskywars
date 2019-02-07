package net.maccarita.vbskywars.arenas.loot;

import net.maccarita.vbskywars.utils.VBUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

/**
 * An abstract class representing a tier level
 */
public abstract class TierLevel {

    /**
     * Get the tier level.
     *
     * @return The tier level.
     */
    public abstract byte getTierLevel();

    /**
     * Get a random item, matching the ItemType provided.
     *
     * @param type The type of item.
     * @return A random item, matching the ItemType provided
     * @see ItemType
     */
    public abstract ItemStack getItem(ItemType type);

    private static final ItemStack POTION_FIRE_RESISTANCE = new ItemStack(Material.POTION, 1);
    private static final ItemStack POTION_SPEED = new ItemStack(Material.POTION, 1);

    static {
        PotionMeta frMeta = (PotionMeta) POTION_FIRE_RESISTANCE.getItemMeta();
        frMeta.addCustomEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(200, 0), true);
        POTION_FIRE_RESISTANCE.setItemMeta(frMeta);

        PotionMeta sMeta = (PotionMeta) POTION_SPEED.getItemMeta();
        sMeta.addCustomEffect(PotionEffectType.SPEED.createEffect(200, 1), true);
        POTION_SPEED.setItemMeta(sMeta);
    }


    public static class TierLevel2 extends TierLevel {
        /**
         * {@inheritDoc}
         */
        public ItemStack getItem(ItemType type) {
            boolean[] randoms = new boolean[]{VBUtils.getRandom().nextBoolean(), VBUtils.getRandom().nextBoolean()};

            switch (type) {
                case HELMET:
                    return new ItemStack(randoms[0] ? Material.IRON_HELMET : Material.CHAINMAIL_HELMET);
                case CHESTPLATE:
                    return new ItemStack(randoms[0] ? Material.IRON_CHESTPLATE : Material.CHAINMAIL_CHESTPLATE);
                case LEGGINGS:
                    return new ItemStack(randoms[0] ? Material.IRON_LEGGINGS : Material.CHAINMAIL_LEGGINGS);
                case BOOTS:
                    return new ItemStack(randoms[0] ? Material.IRON_BOOTS : Material.CHAINMAIL_BOOTS);
                case AXE:
                    return new ItemStack(randoms[0] ? Material.STONE_AXE : Material.IRON_AXE);
                case PICKAXE:
                    return new ItemStack(randoms[0] ? Material.STONE_PICKAXE : Material.IRON_PICKAXE);
                case SWORD:
                    return new ItemStack(randoms[0] ? Material.IRON_SWORD : Material.STONE_SWORD);
                case FISHING_ROD:
                    return new ItemStack(Material.FISHING_ROD);
                case BOW:
                    return new ItemStack(Material.BOW);
                case ARROWS:
                    return new ItemStack(Material.ARROW, 16);
                case POTION:
                    return randoms[0] ? POTION_FIRE_RESISTANCE : POTION_SPEED;
                case BLOCKS:
                    return new ItemStack(randoms[0] ? Material.OAK_WOOD : Material.STONE, randoms[1] ? 64 : 32);
                case FOOD:
                    return new ItemStack(randoms[0] ? Material.COOKED_BEEF : Material.BREAD, randoms[1] ? 64 : 32);
                case ENDER_PEARL:
                    new ItemStack(Material.ENDER_PEARL, 2);
                    break;
                case GOLDEN_APPLE:
                    return new ItemStack(Material.GOLDEN_APPLE, 4);
                case THROWABLE:
                    return new ItemStack(randoms[0] ? Material.SNOWBALL : Material.EGG, 16);
                case LAVA_BUCKET:
                    return new ItemStack(Material.LAVA_BUCKET);
                case WATER_BUCKET:
                    return new ItemStack(Material.WATER_BUCKET);
                case FLINT_AND_STEEL:
                    return new ItemStack(Material.FLINT_AND_STEEL);
                case ENCHANTMENT_TABLE:
                    return new ItemStack(Material.ENCHANTING_TABLE);
            }

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte getTierLevel() {
            return 2;
        }
    }

    public static class TierLevel1 extends TierLevel {

        /**
         * {@inheritDoc}
         */
        public ItemStack getItem(ItemType type) {
            boolean[] randoms = new boolean[]{VBUtils.getRandom().nextBoolean(), VBUtils.getRandom().nextBoolean()};

            switch (type) {
                case HELMET:
                    return new ItemStack(randoms[0] ? Material.LEATHER_HELMET : Material.GOLDEN_HELMET);
                case CHESTPLATE:
                    return new ItemStack(randoms[0] ? Material.LEATHER_CHESTPLATE : Material.GOLDEN_CHESTPLATE);
                case LEGGINGS:
                    return new ItemStack(randoms[0] ? Material.LEATHER_LEGGINGS : Material.GOLDEN_LEGGINGS);
                case BOOTS:
                    return new ItemStack(randoms[0] ? Material.LEATHER_BOOTS : Material.GOLDEN_BOOTS);
                case AXE:
                    return new ItemStack(randoms[0] ? Material.WOODEN_AXE : Material.STONE_AXE);
                case PICKAXE:
                    return new ItemStack(randoms[0] ? Material.WOODEN_PICKAXE : Material.STONE_AXE);
                case SWORD:
                    return new ItemStack(randoms[0] ? Material.STONE_SWORD : Material.WOODEN_SWORD);
                case FISHING_ROD:
                    return new ItemStack(Material.FISHING_ROD);
                case BOW:
                    return new ItemStack(Material.BOW);
                case ARROWS:
                    return new ItemStack(Material.ARROW, 8);
                case POTION:
                    return randoms[0] ? POTION_FIRE_RESISTANCE : POTION_SPEED;
                case BLOCKS:
                    return new ItemStack(randoms[0] ? Material.OAK_WOOD : Material.STONE, randoms[1] ? 32 : 16);
                case FOOD:
                    return new ItemStack(randoms[0] ? Material.COOKED_BEEF : Material.BREAD, randoms[1] ? 16 : 8);
                case ENDER_PEARL:
                    return new ItemStack(Material.ENDER_PEARL, 1);
                case GOLDEN_APPLE:
                    return new ItemStack(Material.GOLDEN_APPLE, 2);
                case THROWABLE:
                    return new ItemStack(randoms[0] ? Material.SNOWBALL : Material.EGG, randoms[1] ? 16 : 8);
                case WATER_BUCKET:
                    return new ItemStack(Material.WATER_BUCKET);
                case LAVA_BUCKET:
                    return new ItemStack(Material.LAVA_BUCKET);
                case FLINT_AND_STEEL:
                    return new ItemStack(Material.FLINT_AND_STEEL);
                case ENCHANTMENT_TABLE:
                    return new ItemStack(Material.ENCHANTING_TABLE);
                default:
                    return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte getTierLevel() {
            return 1;
        }
    }
}