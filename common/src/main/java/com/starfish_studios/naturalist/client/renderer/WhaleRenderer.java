package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.client.model.WhaleModel;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Whale;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WhaleRenderer extends GeoEntityRenderer<Whale> {

    public WhaleRenderer(EntityRendererProvider.Context context) {
        super(context, new WhaleModel());
    }

    @Override
    public void render(Whale animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (animatable.isBaby()) {
            poseStack.scale(0.6F, 0.6F, 0.6F);
        }
        else {
            poseStack.scale(1.3F, 1.3F, 1.3F);
        }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
