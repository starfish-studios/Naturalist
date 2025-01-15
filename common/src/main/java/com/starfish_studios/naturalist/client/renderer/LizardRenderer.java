package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.LizardModel;
import com.starfish_studios.naturalist.common.entity.Lizard;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class LizardRenderer extends GeoEntityRenderer<Lizard> {
    public LizardRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LizardModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public float getMotionAnimThreshold(Lizard animatable) {
        return 0.000001f;
    }

    @Override
    public @Nullable RenderType getRenderType(Lizard animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
