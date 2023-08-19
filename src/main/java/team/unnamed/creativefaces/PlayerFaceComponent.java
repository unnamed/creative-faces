package team.unnamed.creativefaces;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.font.Font;
import team.unnamed.creativefaces.util.Faces;

public final class PlayerFaceComponent implements ComponentLike {

    private final int[][] pixels;
    private final Component component; // player face data is duplicated here, hmmm

    private PlayerFaceComponent(int[][] pixels, Component component) {
        this.pixels = pixels;
        this.component = component;
    }

    /**
     * Returns all the pixels from the player's face
     * texture. Useful for serialization/storage.
     * Deserialize using {@link FaceProvider#fromPixels}
     *
     * @return The player's face pixels
     */
    public int[][] pixels() {
        return pixels;
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }

    public static PlayerFaceComponent fromPixels(int[][] pixels, FacesPluginConfig.Ref config) {
        FacesPluginConfig.Characters characters = config.current().characters();
        TextComponent.Builder component = Component.text();
        for (int y = 0; y < Faces.FACE_HEIGHT; y++) {
            if (y != 0) {
                component.append(Component.text(characters.offsetMinusEight()));
            }
            for (int x = 0; x < Faces.FACE_WIDTH; x++) {
                int color = pixels[y][x];
                component.append(
                        Component.text()
                                .content(characters.pixelsByHeight()[y])
                                .color(TextColor.color(color))
                );

                component.append(Component.text(characters.offsetMinusOne()));
            }
        }
        Key font = config.current().font();
        // this condition may cause issues, check later
        if (!font.equals(Font.MINECRAFT_DEFAULT)) {
            component.font(font);
        }
        return new PlayerFaceComponent(pixels, component.build());
    }

}
