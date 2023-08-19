package team.unnamed.creativefaces.util;

import team.unnamed.creativefaces.FacesPluginConfig;
import team.unnamed.creativefaces.PlayerFaceComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public final class Faces {

    public static final int FACE_HEIGHT = 8;
    public static final int FACE_WIDTH = 8;

    private static final String[] OLD_DEFAULT_SKINS = { "old_steve", "old_alex" };
    private static final String[] DEFAULT_SKINS = { "alex", "ari", "efe", "kai", "makena", "noor", "steve", "sunny", "zuri" };

    private static final PlayerFaceComponent[] OLD_DEFAULT_FACES_COLORS = new PlayerFaceComponent[OLD_DEFAULT_SKINS.length];
    private static final PlayerFaceComponent[] DEFAULT_FACES_COLORS = new PlayerFaceComponent[DEFAULT_SKINS.length];

    private Faces() {
    }

    public static PlayerFaceComponent getDefaultFace(UUID playerId, boolean newSkins, FacesPluginConfig.Ref config) {
        if (newSkins) {
            // NEW DEFAULT SKINS
            int index = getDefaultSkin(playerId);
            PlayerFaceComponent face = DEFAULT_FACES_COLORS[index];
            if (face == null) {
                int[][] pixels = loadFace(DEFAULT_SKINS[index]);
                face = PlayerFaceComponent.fromPixels(pixels, config);
                DEFAULT_FACES_COLORS[index] = face;
            }
            return face;
        } else {
            // OLD DEFAULT SKINS
            int index = getOldDefaultSkin(playerId);
            PlayerFaceComponent face = OLD_DEFAULT_FACES_COLORS[index];
            if (face == null) {
                int[][] pixels = loadFace(OLD_DEFAULT_SKINS[index]);
                face = PlayerFaceComponent.fromPixels(pixels, config);
                OLD_DEFAULT_FACES_COLORS[index] = face;
            }
            return face;
        }
    }

    private static int[][] loadFace(String skinName) {

        URL faceUrl = Faces.class.getClassLoader().getResource("faces/" + skinName + ".png");

        if (faceUrl == null) {
            throw new IllegalStateException("No face data found for skin name: '" + skinName + "'");
        }

        // load image
        BufferedImage faceData;

        try {
            faceData = ImageIO.read(faceUrl);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read face data", e);
        }

        int[][] colors = new int[FACE_HEIGHT][FACE_WIDTH];
        for (int y = 0; y < FACE_HEIGHT; y++) {
            for (int x = 0; x < FACE_WIDTH; x++) {
                colors[y][x] = faceData.getRGB(x, y);
            }
        }

        return colors;
    }

    // 1.19.3+: We have nine default skins (Steve, Alex and seven more skins)
    private static int getDefaultSkin(UUID playerId) {
        int len = DEFAULT_SKINS.length;
        int type = Math.floorMod(playerId.hashCode(), len * 2);
        // We can know if the skin model will be slim using the following code:
        // boolean slim = type < len;
        return type % len;
    }

    // Before 1.19.3: We only have two default skins (Steve & Alex)
    private static int getOldDefaultSkin(UUID playerId) {
        // even: Steve, odd: Alex
        return playerId.hashCode() % 2;
    }

    public static String pixelsToBase64(int[][] pixels) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (DataOutputStream base64Output = new DataOutputStream(Base64.getEncoder().wrap(output))) {
            for (int y = 0; y < FACE_HEIGHT; y++) {
                for (int x = 0; x < FACE_WIDTH; x++) {
                    base64Output.writeInt(pixels[y][x]);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return output.toString();
    }

    public static int[][] base64ToPixels(String base64String) {
        ByteArrayInputStream input = new ByteArrayInputStream(base64String.getBytes(StandardCharsets.UTF_8));
        int[][] pixels = new int[FACE_HEIGHT][FACE_HEIGHT];
        try (DataInputStream base64Input = new DataInputStream(Base64.getDecoder().wrap(input))) {
            for (int y = 0; y < FACE_HEIGHT; y++) {
                for (int x = 0; x < FACE_WIDTH; x++) {
                    int pixel = base64Input.readInt();
                    pixels[y][x] = pixel;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return pixels;
    }

}
