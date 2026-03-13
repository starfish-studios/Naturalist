package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.client.model.MammothModel;
import com.starfish_studios.naturalist.common.entity.Mammoth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class MammothRenderer extends GeoEntityRenderer<Mammoth> {
    public MammothRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MammothModel());
        this.shadowRadius = 0.9F;
    }

    @Override
    public float getMotionAnimThreshold(Mammoth animatable) {
        return 0.000001f;
    }

    @Override
    public void render(@NotNull Mammoth entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
