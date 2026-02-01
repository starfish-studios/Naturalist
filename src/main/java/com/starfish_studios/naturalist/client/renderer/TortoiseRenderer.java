package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.TortoiseModel;
import com.starfish_studios.naturalist.client.renderer.layers.TortoiseSkinLayer;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class TortoiseRenderer extends GeoEntityRenderer<Tortoise, NaturalistGeoRenderState> {
    public TortoiseRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TortoiseModel());
        this.shadowRadius = 0.8F;
        // TODO: Re-enable after adapting to GeckoLib 5 GeoRenderLayer API
        // this.addRenderLayer(new TortoiseSkinLayer(this));
    }

    @Override
    public void extractRenderState(Tortoise entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public float getMotionAnimThreshold(Tortoise animatable) {
        return 0.000001f;
    }

    // render() method removed as it is final in GeckoLib 5.

    public RenderType getRenderType(Tortoise entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Tortoise entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}



