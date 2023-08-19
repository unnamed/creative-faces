package team.unnamed.creativefaces;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.central.CreativeCentral;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creativefaces.command.FacesCommand;
import team.unnamed.creativefaces.display.MiniPlaceholdersDisplayHook;
import team.unnamed.creativefaces.display.PlaceholderAPIDisplayHook;
import team.unnamed.creativefaces.listener.ResourcePackGenerateListener;

import java.util.Objects;
import java.util.Set;

public class FacesPlugin extends JavaPlugin {

    private final FacesPluginConfig.Ref config = new FacesPluginConfig.Ref();
    private FaceProvider faceProvider;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        parseConfig();

        faceProvider = new FaceProvider(config);
        Bukkit.getServicesManager().register(FaceProvider.class, faceProvider, this, ServicePriority.High);

        Objects.requireNonNull(getCommand("faces"), "Couldn't find command 'faces'. Altered 'plugin.yml'?")
                .setExecutor(new FacesCommand(this));

        CreativeCentral central = CreativeCentralProvider.get();

        // register our listener to ResourcePackGenerateEvent
        central.eventBus().listen(this, ResourcePackGenerateEvent.class, new ResourcePackGenerateListener(config));

        // register our hooks
        Set.of(
                new MiniPlaceholdersDisplayHook(faceProvider),
                new PlaceholderAPIDisplayHook(this, faceProvider)
        ).forEach(hook -> {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(hook.plugin());
            if (plugin != null) {
                hook.enable(plugin);
                getLogger().info("Successfully hooked into " + plugin.getName());
            }
        });
    }

    @Override
    public void onDisable() {
        config.set(null);
        faceProvider = null;
    }

    public FacesPluginConfig.Ref config() {
        return config;
    }

    public FaceProvider headProvider() {
        if (faceProvider == null) {
            throw new IllegalStateException("Plugin not initialized yet!");
        }
        return faceProvider;
    }

    public FacesPluginConfig parseConfig() {
        FacesPluginConfig config = FacesPluginConfig.load(super.getConfig());
        this.config.set(config);
        return config;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        parseConfig();
        if (faceProvider != null) {
            faceProvider.invalidateCache();
        }
    }

    /**
     * {@inheritDoc}
     * @deprecated Use {@link FacesPlugin#config()} instead
     */
    @Override
    @Deprecated
    public @NotNull FileConfiguration getConfig() {
        return super.getConfig();
    }

}
