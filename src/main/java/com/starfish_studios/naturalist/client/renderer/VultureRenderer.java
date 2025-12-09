package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.VultureModel;
import com.starfish_studios.naturalist.common.entity.Vulture;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class VultureRenderer extends GeoEntityRenderer<Vulture> {

    public VultureRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VultureModel());
        this.shadowRadius = 0.65F;
    }

    @Override
    public float getMotionAnimThreshold(Vulture animatable) {
        return 0.000001f;
    }

   public RenderType getRenderType(Vulture entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderRecursively(PoseStack stack, Vulture entity, GeoBone bone, RenderType renderType, @NotNull MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {



        if (bone.getName().equals("held_item")) {
            stack.pushPose();
            stack.mulPose(new Quaternionf(-0.7071f, 0.0f, 0.0f, 0.7071f));
            stack.translate(0.0D, 1.1D, 0.25D);

            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.GROUND, packedLight, packedOverlay, stack, bufferSource, animatable.level(), 0);
            stack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(((GeoRenderer<Vulture>)this).getTextureLocation(entity)));
        }
        super.renderRecursively(stack, entity, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
