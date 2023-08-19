package team.unnamed.creativefaces.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import team.unnamed.creative.central.CreativeCentralProvider;
import team.unnamed.creativefaces.FacesPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.logging.Level;

public final class FacesCommand implements CommandExecutor {

    private final FacesPlugin plugin;

    public FacesCommand(FacesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        sender.sendMessage(Component.text("Reloading configuration...", NamedTextColor.GREEN));
        long startTime = System.currentTimeMillis();
        try {
            plugin.reloadConfig();
            long duration = System.currentTimeMillis() - startTime;
            sender.sendMessage(Component.text("Done! (" + duration + "ms)\nReloading resource-pack...", NamedTextColor.GREEN));
            CreativeCentralProvider.get().generate().whenComplete((ignored, e) -> {
                if (e == null) {
                    sender.sendMessage(Component.text("Done!", NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text("Failed to reload the resource-pack, check logs", NamedTextColor.RED));
                    plugin.getLogger().log(Level.SEVERE, "Failed to reload resource-pack", e);
                }
            });
        } catch (Throwable e) {
            sender.sendMessage(Component.text("Reload failed, check logs", NamedTextColor.RED));
            plugin.getLogger().log(Level.SEVERE, "Failed to reload configuration", e);
        }
        return true;
    }

}
