package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.DeerModel;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Deer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class DeerRenderer extends GeoEntityRenderer<Deer> {
    public DeerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DeerModel());
        this.shadowRadius = 0.8F;
    }

    @Override
    public float getMotionAnimThreshold(Deer animatable) {
        return 0.000001f;
    }

    //TODO: 1.21.4
    /*@Override
    public void render(Deer entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
        else {
            poseStack.scale(0.9F, 0.9F, 0.9F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }*/

    @Override
    public @Nullable RenderType getRenderType(Deer animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
