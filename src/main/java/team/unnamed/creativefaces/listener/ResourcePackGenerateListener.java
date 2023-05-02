package team.unnamed.creativefaces.listener;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.central.event.EventListener;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;
import team.unnamed.creativefaces.resourcepack.Faces;
import team.unnamed.creativefaces.resourcepack.HeadProvider;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackGenerateListener implements EventListener<ResourcePackGenerateEvent> {

    @Override
    public void on(ResourcePackGenerateEvent event) {
        ResourcePack resourcePack = event.resourcePack();

        Font defaultFont = resourcePack.font(Font.MINECRAFT_DEFAULT);
        List<FontProvider> defaultFontProviders = defaultFont == null
                ? new ArrayList<>()
                : new ArrayList<>(defaultFont.providers());

        Key pixelTexture = Key.key("emojis", "emojiutil/pixel");

        // write pixel font providers
        for (int height = 0; height < Faces.FACE_HEIGHT; height++) {
            defaultFontProviders.add(
                    FontProvider.bitMap()
                            .file(pixelTexture)
                            .height(8)
                            .ascent(Faces.FACE_HEIGHT - height - 1)
                            .characters(HeadProvider.PIXELS_BY_HEIGHT[height])
                            .build()
            );
        }

        // write offset font provider
        // if (Version.CURRENT.minor() >= 19) {
            // 1.19 added space font providers
            defaultFontProviders.add(
                    FontProvider.space()
                            .advance(HeadProvider.OFFSET_MINUS_1, -1)
                            .advance(HeadProvider.OFFSET_MINUS_8, -8)
                            .build()
            );
        // } else {
        //    throw new UnsupportedOperationException("1.19+ required");
        // }

        resourcePack.font(Font.of(Font.MINECRAFT_DEFAULT, defaultFontProviders));

        resourcePack.texture(
                Texture.builder()
                        .key(pixelTexture)
                        .data(Writable.resource(HeadProvider.class.getClassLoader(), "pixel.png"))
                        .build()
        );
    }

}
