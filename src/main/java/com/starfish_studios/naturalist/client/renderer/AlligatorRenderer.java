package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.AlligatorModel;
import com.starfish_studios.naturalist.common.entity.Alligator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

@Environment(EnvType.CLIENT)
public class AlligatorRenderer extends GeoEntityRenderer<Alligator, NaturalistGeoRenderState> {
    public AlligatorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlligatorModel());
        // TODO: Re-enable after adapting to GeckoLib 5 GeoRenderLayer API
        // addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.shadowRadius = 1.0F;
    }

    @Override
    public void extractRenderState(Alligator entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public float getMotionAnimThreshold(Alligator animatable) {
        return 0.000001f;
    }

    // render() method removed as it is final in GeckoLib 5.
    // Scaling should be handled in getRenderType or
    // extractRenderState/createRenderState if needed.
    // For now, removing to fix compilation.

    public RenderType getRenderType(Alligator entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Alligator entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}


