package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.RhinoModel;
import com.starfish_studios.naturalist.common.entity.Rhino;
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
public class RhinoRenderer extends GeoEntityRenderer<Rhino, NaturalistGeoRenderState> {
    public RhinoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RhinoModel());
        this.shadowRadius = 1.1F;
    }

    @Override
    public void extractRenderState(Rhino entity, NaturalistGeoRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public float getMotionAnimThreshold(Rhino animatable) {
        return 0.000001f;
    }

    // render() method removed as it is final in GeckoLib 5.

    public @NotNull RenderType getRenderType(Rhino entity, float partialTicks, PoseStack stack,
            @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn,
            ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public NaturalistGeoRenderState createRenderState(Rhino entity, Void unused) {
        return new NaturalistGeoRenderState();
    }
}



