package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.SnakeModel;
import com.starfish_studios.naturalist.common.entity.Snake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SnakeRenderer extends GeoEntityRenderer<Snake, NaturalistGeoRenderState> {
    public SnakeRenderer(EntityRendererProvider.@NotNull Context renderManager) {
        super(renderManager, new SnakeModel());
        this.shadowRadius = 0.4F;
        // TODO: Re-enable after adapting to GeckoLib 5 GeoRenderLayer API
        // this.addRenderLayer(new SleepLayer<>(this, ...));
    }

    @Override
    public void extractRenderState(Snake entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public float getMotionAnimThreshold(Snake animatable) {
        return 0.000001f;
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Snake entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}



