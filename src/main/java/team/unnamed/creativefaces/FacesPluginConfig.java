package team.unnamed.creativefaces;

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import org.bukkit.configuration.ConfigurationSection;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus;

public record FacesPluginConfig(
        Characters characters,
        Key font,
        @Subst(Key.MINECRAFT_NAMESPACE)
        String namespace
) {

    public record Characters(
            String offsetMinusEight,
            String offsetMinusOne,
            String[] pixelsByHeight
    ) {
    }

    public static FacesPluginConfig load(ConfigurationSection config) {
        @Subst("minecraft:default")
        String fontStr = config.getString("font", "minecraft:default");
        @Subst("minecraft")
        String namespace = config.getString("namespace", "creativefaces");
        Key font;

        try {
            font = Key.key(fontStr);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid font key!", e);
        }

        ConfigurationSection height = config.getConfigurationSection("characters.height");

        if (height == null) {
            throw new IllegalStateException("No height characters specified!");
        }

        return new FacesPluginConfig(
                new Characters(
                        config.getString("characters.offset-8"),
                        config.getString("characters.offset-1"),
                        new String[] { // repetitive thing but i'm lazy to do logic
                                height.getString("0"),
                                height.getString("1"),
                                height.getString("2"),
                                height.getString("3"),
                                height.getString("4"),
                                height.getString("5"),
                                height.getString("6"),
                                height.getString("7")
                        }
                ),
                font,
                namespace
        );
    }

    public static class Ref {

        private FacesPluginConfig value;

        @ApiStatus.Internal
        public void set(FacesPluginConfig value) {
            this.value = value;
        }

        /**
         * Returns the current plugin config, should not be stored
         * for so much time since it can change when it's reloaded
         *
         * @return The current plugin's config
         */
        public FacesPluginConfig current() {
            if (value == null) {
                throw new IllegalStateException("The plugin configuration has not been loaded yet!");
            }
            return value;
        }

    }

}
