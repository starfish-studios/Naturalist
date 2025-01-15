package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.FireflyModel;
import com.starfish_studios.naturalist.client.renderer.layers.FireflyGlowLayer;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class FireflyRenderer extends GeoEntityRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireflyModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new FireflyGlowLayer(this));
    }

    @Override
    public float getMotionAnimThreshold(Firefly animatable) {
        return 0.000001f;
    }

    @Override
    public @Nullable RenderType getRenderType(Firefly animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
