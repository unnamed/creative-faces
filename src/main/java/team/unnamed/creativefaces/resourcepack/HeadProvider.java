package team.unnamed.creativefaces.resourcepack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadProvider {

    public static final String[] PIXELS_BY_HEIGHT = {
            string(0x10B00B),
            string(0x10B00C),
            string(0x10B00D),
            string(0x10B00E),
            string(0x10B00F),
            string(0x10B010),
            string(0x10B011),
            string(0x10B012)
    };
    public static final String OFFSET_MINUS_1 = string(0x10B013);
    public static final String OFFSET_MINUS_8 = string(0x10B014);

    private static final Map<UUID, int[][]> heads = new HashMap<>();
    
    private HeadProvider() {
    }

    public static Component of(Player player) {
        int[][] headColors = heads.computeIfAbsent(player.getUniqueId(), k -> findHead(player));
        TextComponent.Builder component = Component.text();
        for (int y = 0; y < Faces.FACE_HEIGHT; y++) {
            if (y != 0) {
                component.append(Component.text(OFFSET_MINUS_8));
            }
            for (int x = 0; x < Faces.FACE_WIDTH; x++) {
                int color = headColors[y][x];
                component.append(
                        Component.text()
                                .content(PIXELS_BY_HEIGHT[y])
                                .color(TextColor.color(color))
                );

                component.append(Component.text(OFFSET_MINUS_1));
            }
        }
        return component.build();
    }

    public static int[][] findHead(Player player) {
        PlayerTextures textures = player.getPlayerProfile().getTextures();
        URL skinUrl = textures.getSkin();

        if (skinUrl == null) {
            return Faces.getDefaultFace(player.getUniqueId(), true);
        }

        BufferedImage skinData;

        // read image
        try {
            skinData = ImageIO.read(skinUrl);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read skin data", e);
        }

        int[][] headColors = new int[Faces.FACE_HEIGHT][Faces.FACE_WIDTH];

        // head layer
        for (int x = 8; x < 16; x++) {
            for (int y = 8; y < 16; y++) {
                headColors[y - 8][x - 8] = skinData.getRGB(x, y);
            }
        }

        // helmet layer
        for (int x = 40; x < 48; x++) {
            for (int y = 8; y < 16; y++) {
                int rgba = skinData.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xff;
                if ((alpha) == 0xFF) {
                    headColors[y - 8][x - 40] = rgba;
                }
            }
        }

        return headColors;
    }

    private static String string(int codePoint) {
        return new String(Character.toChars(codePoint));
    }

}
