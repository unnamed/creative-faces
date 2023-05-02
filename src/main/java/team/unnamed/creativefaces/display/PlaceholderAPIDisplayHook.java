package team.unnamed.creativefaces.display;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creativefaces.resourcepack.HeadProvider;

public class PlaceholderAPIDisplayHook implements DisplayHook {

    @Override
    public String plugin() {
        return "PlaceholderAPI";
    }

    @Override
    public void enable(Plugin plugin) {
        new FacesExpansion().register();
    }

    private static class FacesExpansion extends PlaceholderExpansion {

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
                return LegacyComponentSerializer.legacySection().serialize(HeadProvider.of(player));
            } else {
                return null;
            }
        }
    }

}
