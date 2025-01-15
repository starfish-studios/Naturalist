package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.naturalist.client.model.CaterpillarModel;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class CaterpillarRenderer extends GeoEntityRenderer<Caterpillar> {
    public CaterpillarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CaterpillarModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public float getMotionAnimThreshold(Caterpillar animatable) {
        return 0.000001f;
    }

    @Override
    public @Nullable RenderType getRenderType(Caterpillar animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
