package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.BoarModel;
import com.starfish_studios.naturalist.common.entity.Boar;
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
public class BoarRenderer extends GeoEntityRenderer<Boar> {
    public BoarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoarModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public float getMotionAnimThreshold(Boar animatable) {
        return 0.000001f;
    }

    @Override
    public void render(Boar entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
        else {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

   public RenderType getRenderType(Boar entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }
}
