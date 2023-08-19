package team.unnamed.creativefaces.listener;

import net.kyori.adventure.key.Key;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.central.event.EventListener;
import team.unnamed.creative.central.event.pack.ResourcePackGenerateEvent;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;
import team.unnamed.creativefaces.FacesPluginConfig;
import team.unnamed.creativefaces.util.Faces;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackGenerateListener implements EventListener<ResourcePackGenerateEvent> {

    private final FacesPluginConfig.Ref config;

    public ResourcePackGenerateListener(FacesPluginConfig.Ref config) {
        this.config = config;
    }

    @Override
    public void on(ResourcePackGenerateEvent event) {

        FacesPluginConfig currentConfig = config.current();
        FacesPluginConfig.Characters characters = currentConfig.characters();
        ResourcePack resourcePack = event.resourcePack();

        Font font = resourcePack.font(currentConfig.font());
        List<FontProvider> fontProviders = font == null ? new ArrayList<>() : new ArrayList<>(font.providers());

        Texture pixelTexture = Texture.builder()
                .key(Key.key(currentConfig.namespace(), "textures/font/_px"))
                .data(Writable.resource(getClass().getClassLoader(), "pixel.png"))
                .build();

        // write pixel texture
        resourcePack.texture(pixelTexture);

        // write pixel font providers
        for (int height = 0; height < Faces.FACE_HEIGHT; height++) {
            fontProviders.add(
                    FontProvider.bitMap()
                            .file(pixelTexture.key())
                            .height(8)
                            .ascent(Faces.FACE_HEIGHT - height - 1)
                            .characters(characters.pixelsByHeight()[height])
                            .build()
            );
        }

        // write offset font provider
        fontProviders.add(
                FontProvider.space()
                        .advance(characters.offsetMinusOne(), -1)
                        .advance(characters.offsetMinusEight(), -8)
                        .build()
        );

        resourcePack.font(Font.of(currentConfig.font(), fontProviders));
    }

}
