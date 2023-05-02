package team.unnamed.creativefaces.display;

import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.unnamed.creativefaces.resourcepack.HeadProvider;

public class MiniPlaceholdersDisplayHook implements DisplayHook {

    @Override
    public String plugin() {
        return "MiniPlaceholders";
    }

    @Override
    public void enable(Plugin plugin) {
        Expansion expansion = Expansion.builder("faces")
                .filter(Player.class)
                .audiencePlaceholder("player", (audience, arguments, ctx) ->
                        Tag.selfClosingInserting(HeadProvider.of((Player) audience)))
                .build();
        expansion.register();
    }

}
