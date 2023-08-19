package team.unnamed.creativefaces;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creativefaces.util.Faces;
import team.unnamed.creativefaces.util.NewSkins;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class FaceProvider {

    private static final Object HEAD_PLACEHOLDER = new Object();

    private final FacesPluginConfig.Ref config;
    private final Cache<UUID, Object> heads = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public FaceProvider(FacesPluginConfig.Ref config) {
        this.config = config;
    }

    public PlayerFaceComponent get(Player player) {
        PlayerFaceComponent data = getOrFind(player);
        if (data == null) {
            data = Faces.getDefaultFace(player.getUniqueId(), NewSkins.available(), config);
        }
        return data;
    }

    public PlayerFaceComponent fromPixels(int[][] pixels) {
        return PlayerFaceComponent.fromPixels(pixels, config);
    }

    /**
     * Finds the head of the player in local cache, if not found,
     * it fetches it (see {@link FaceProvider#fetch}),
     * the found head is saved to cache if present. This method
     * may return null if the head couldn't be found
     *
     * @param player The player
     * @return The player head data, or null if not found
     */
    public @Nullable PlayerFaceComponent getOrFind(Player player) {
        UUID id = player.getUniqueId();
        // find the cached data
        PlayerFaceComponent data = getCached(id);
        if (data == null) {
            data = fetch(player);
            heads.put(id, data == null ? HEAD_PLACEHOLDER : data);
        }
        return data;
    }

    /**
     * Finds the head of the player with the given {@code uuid},
     * or null if not present
     *
     * @param uuid The player's UUID
     * @return The player head data, or null if not found
     */
    public @Nullable PlayerFaceComponent getCached(UUID uuid) {
        Object head = heads.getIfPresent(uuid);
        if (head == HEAD_PLACEHOLDER) {
            return null;
        } else {
            return (PlayerFaceComponent) head;
        }
    }

    /**
     * Performs maintenance operations for the internal cache of
     * head data
     */
    public void cleanUp() {
        heads.cleanUp();
    }

    /**
     * Invalidates all the cached player heads
     */
    public void invalidateCache() {
        heads.invalidateAll();
    }

    /**
     * Fetches the player head data, returns null if the player does not
     * have a skin
     *
     * @param player The player
     * @return The player head data
     */
    public @Nullable PlayerFaceComponent fetch(Player player) {
        PlayerTextures textures = player.getPlayerProfile().getTextures();
        URL skinUrl = textures.getSkin();

        if (skinUrl == null) {
            return null;
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

        return PlayerFaceComponent.fromPixels(headColors, config);
    }

}
