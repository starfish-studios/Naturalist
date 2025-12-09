package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.LizardTailModel;
import com.starfish_studios.naturalist.common.entity.LizardTail;
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
public class LizardTailRenderer extends GeoEntityRenderer<LizardTail> {
    public LizardTailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LizardTailModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public float getMotionAnimThreshold(LizardTail animatable) {
        return 0.000001f;
    }

   public RenderType getRenderType(LizardTail entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void render(@NotNull LizardTail entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.translate(0, -0.3, 0);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
