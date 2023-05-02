package team.unnamed.creativefaces;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.central.CreativeCentral;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creativefaces.display.MiniPlaceholdersDisplayHook;
import team.unnamed.creativefaces.display.PlaceholderAPIDisplayHook;
import team.unnamed.creativefaces.listener.ResourcePackGenerateListener;

import java.util.Set;

public class FacesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CreativeCentral central = CreativeCentralProvider.get();

        // register our listener to ResourcePackGenerateEvent
        central.eventBus().listen(this, ResourcePackGenerateEvent.class, new ResourcePackGenerateListener());

        // register our hooks
        boolean none = Set.of(
                        new MiniPlaceholdersDisplayHook(),
                        new PlaceholderAPIDisplayHook()
                )
                .stream()
                .noneMatch(hook -> {
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(hook.plugin());
                    if (plugin != null) {
                        hook.enable(plugin);
                        getLogger().info("Successfully hooked into " + plugin.getName());
                        return true;
                    }
                    return false;
                });
        if (none) {
            getLogger().warning(
                    "No placeholder plugin found, there is no way to display faces!\n"
                    + "Please install a supported placeholder plugin, such as:\n"
                    + "    - MiniPlaceholders (recommended)\n"
                    + "    - PlaceholderAPI"
            );
        }
    }

}
