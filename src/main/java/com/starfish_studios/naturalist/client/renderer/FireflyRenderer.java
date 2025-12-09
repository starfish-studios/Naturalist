package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.FireflyModel;
import com.starfish_studios.naturalist.client.renderer.layers.FireflyGlowLayer;
import com.starfish_studios.naturalist.common.entity.Firefly;
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
public class FireflyRenderer extends GeoEntityRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.@NotNull Context renderManager) {
        super(renderManager, new FireflyModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new FireflyGlowLayer(this));
    }

    @Override
    public float getMotionAnimThreshold(Firefly animatable) {
        return 0.000001f;
    }

   public RenderType getRenderType(Firefly entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, @NotNull ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }
}
