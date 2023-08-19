package team.unnamed.creativefaces.display;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creativefaces.FaceProvider;

public class PlaceholderAPIDisplayHook implements DisplayHook {

    private final Plugin plugin;
    private final FaceProvider faceProvider;
    private boolean warnNonDefaultFont = true;

    public PlaceholderAPIDisplayHook(Plugin plugin, FaceProvider faceProvider) {
        this.plugin = plugin;
        this.faceProvider = faceProvider;
    }

    @Override
    public String plugin() {
        return "PlaceholderAPI";
    }

    @Override
    public void enable(Plugin plugin) {
        new FacesExpansion().register();
    }

    private class FacesExpansion extends PlaceholderExpansion {

        @Override
        public @NotNull String getAuthor() {
            return "Unnamed Team";
        }

        @Override
        public @NotNull String getIdentifier() {
            return "faces";
        }

        @Override
        public @NotNull String getVersion() {
            return "1.0.0";
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
            if (player == null) {
                return null;
            }
            if (params.equalsIgnoreCase("face")) {
                Component component = faceProvider.get(player).asComponent();
                if (component.font() != null) {
                    // if component uses a custom font, legacy component serializer will
                    // not work, placeholder api doesn't support this
                    if (warnNonDefaultFont) {
                        plugin.getLogger().warning("You set a custom font, it cannot be used with PlaceholderAPI!");
                        warnNonDefaultFont = false;
                    }
                    return null;
                } else {
                    return LegacyComponentSerializer.legacySection().serialize(component);
                }
            } else {
                return null;
            }
        }
    }

}
