package team.unnamed.creativefaces.display;

import org.bukkit.plugin.Plugin;

/**
 * Represents a plugin hook, used to display
 * player faces using a specific plugin
 */
public interface DisplayHook {

    /**
     * The plugin name this hook requires
     *
     * @return The hooked plugin name
     */
    String plugin();

    /**
     * Enables the hook
     *
     * @param plugin The hooked plugin
     */
    void enable(Plugin plugin);

}
