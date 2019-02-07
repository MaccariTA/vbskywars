package net.maccarita.vbskywars.arenas.loot;

public enum ItemType {

    HELMET(true),
    CHESTPLATE(true),
    LEGGINGS(true),
    BOOTS(true),
    AXE(false),
    PICKAXE(true),
    SWORD(true),
    FISHING_ROD(false),
    BOW(false),
    ARROWS(false),
    POTION(true),
    BLOCKS(true),
    FOOD(true),
    ENDER_PEARL(false),
    GOLDEN_APPLE(false),
    THROWABLE(false),
    WATER_BUCKET(false),
    LAVA_BUCKET(false),
    FLINT_AND_STEEL(false),
    ENCHANTMENT_TABLE(false);

    private final boolean must;

    ItemType(boolean must) {
        this.must = must;
    }

    public boolean isMust() {
        return must;
    }
}