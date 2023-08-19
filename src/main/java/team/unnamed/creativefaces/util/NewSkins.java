package team.unnamed.creativefaces.util;

import org.bukkit.Material;

public final class NewSkins { // is this a NewJeans reference?

    private static final boolean AVAILABLE;

    static {
        boolean available = false;
        try {
            // added in 1.19.3
            Material.valueOf("ENDER_DRAGON_SPAWN_EGG");
            available = true;
        } catch (IllegalArgumentException ignored) {
        }
        AVAILABLE = available;
    }

    private NewSkins() {
    }

    public static boolean available() {
        return AVAILABLE;
    }

}
