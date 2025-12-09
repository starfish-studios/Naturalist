package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.ElephantModel;
import com.starfish_studios.naturalist.common.entity.Elephant;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class ElephantRenderer extends GeoEntityRenderer<Elephant> {
    public ElephantRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ElephantModel());
        this.shadowRadius = 1.5F;
    }

    @Override
    public float getMotionAnimThreshold(Elephant animatable) {
        return 0.000001f;
    }

    @Override
    public void render(Elephant entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

   public RenderType getRenderType(Elephant entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, @NotNull ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }
}
