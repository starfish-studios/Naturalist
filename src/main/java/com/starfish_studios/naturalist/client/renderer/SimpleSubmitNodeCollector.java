package com.starfish_studios.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.entity.state.HitboxesRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.List;

public class SimpleSubmitNodeCollector implements SubmitNodeCollector {
    private final MultiBufferSource bufferSource;
    // We might need to handle ordering, but for simple held items, returning
    // null/this might suffice?
    // OrderedSubmitNodeCollector is an interface too.

    private final OrderedSubmitNodeCollector dummyOrdered = new OrderedSubmitNodeCollector() {
        @Override
        public void submitHitbox(PoseStack poseStack, EntityRenderState entityRenderState,
                HitboxesRenderState hitboxesRenderState) {
        }

        @Override
        public void submitShadow(PoseStack poseStack, float v, List<EntityRenderState.ShadowPiece> list) {
        }

        @Override
        public void submitNameTag(PoseStack poseStack, Vec3 vec3, int i, Component component, boolean b, int i1,
                double v, net.minecraft.client.renderer.state.CameraRenderState cameraRenderState) {
        }

        @Override
        public void submitText(PoseStack poseStack, float v, float v1, FormattedCharSequence formattedCharSequence,
                boolean b, Font.DisplayMode displayMode, int i, int i1, int i2, int i3) {
        }

        @Override
        public void submitFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
        }

        @Override
        public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
        }

        @Override
        public <S> void submitModel(Model<? super S> model, S s, PoseStack poseStack, RenderType renderType, int i,
                int i1, int i2, TextureAtlasSprite textureAtlasSprite, int i3,
                net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        }

        @Override
        public <S> void submitModel(Model<? super S> model, S s, PoseStack poseStack, RenderType renderType, int i,
                int i1, int i2,
                net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        }

        @Override
        public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int i, int i1,
                TextureAtlasSprite textureAtlasSprite, boolean b, boolean b1, int i2,
                net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int i3) {
        }

        @Override
        public void submitBlock(PoseStack poseStack, BlockState blockState, int i, int i1, int i2) {
        }

        @Override
        public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
        }

        @Override
        public void submitBlockModel(PoseStack poseStack, RenderType renderType, BlockStateModel blockStateModel,
                float v, float v1, float v2, int i, int i1, int i2) {
        }

        @Override
        public void submitItem(PoseStack poseStack, ItemDisplayContext itemDisplayContext, int i, int i1, int i2,
                int[] ints, List<BakedQuad> list, RenderType renderType, ItemStackRenderState.FoilType foilType) {
            // Forward to the main collector if needed, but for ordering we might assume
            // immediate?
            // Actually, ItemRenderer.renderItem writes to buffer immediately.
            SimpleSubmitNodeCollector.this.submitItem(poseStack, itemDisplayContext, i, i1, i2, ints, list, renderType,
                    foilType);
        }

        @Override
        public void submitCustomGeometry(PoseStack poseStack, RenderType renderType,
                CustomGeometryRenderer customGeometryRenderer) {
        }

        @Override
        public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
        }

        // Add missing methods if any
        @Override
        public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int i, int i1,
                TextureAtlasSprite textureAtlasSprite, int i2,
                net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        }

        @Override
        public void submitModelPart(ModelPart part, PoseStack pose, RenderType type, int light, int overlay,
                TextureAtlasSprite sprite, boolean p_438944_, boolean p_440215_) {
        }

        @Override
        public void submitModelPart(ModelPart part, PoseStack pose, RenderType type, int light, int overlay,
                TextureAtlasSprite sprite) {
        }

    };

    public SimpleSubmitNodeCollector(MultiBufferSource bufferSource) {
        this.bufferSource = bufferSource;
    }

    @Override
    public OrderedSubmitNodeCollector order(int i) {
        return dummyOrdered;
    }

    @Override
    public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int i, int i1, int i2, int[] ints,
            List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
        // Crucial Part: Forward to ItemRenderer.renderItem
        // Signature from javap:
        // public static void renderItem(ItemDisplayContext, PoseStack,
        // MultiBufferSource, int, int, int[], List<BakedQuad>, RenderType,
        // ItemStackRenderState$FoilType)

        ItemRenderer.renderItem(displayContext, poseStack, this.bufferSource, i, i1, ints, quads, renderType, foilType);
    }

    // Empty implementations for others
    @Override
    public void submitHitbox(PoseStack poseStack, EntityRenderState entityRenderState,
            HitboxesRenderState hitboxesRenderState) {
    }

    @Override
    public void submitShadow(PoseStack poseStack, float v, List<EntityRenderState.ShadowPiece> list) {
    }

    @Override
    public void submitNameTag(PoseStack poseStack, Vec3 vec3, int i, Component component, boolean b, int i1, double v,
            net.minecraft.client.renderer.state.CameraRenderState cameraRenderState) {
    }

    @Override
    public void submitText(PoseStack poseStack, float v, float v1, FormattedCharSequence formattedCharSequence,
            boolean b, Font.DisplayMode displayMode, int i, int i1, int i2, int i3) {
    }

    @Override
    public void submitFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
    }

    @Override
    public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S s, PoseStack poseStack, RenderType renderType, int i, int i1,
            int i2, TextureAtlasSprite textureAtlasSprite, int i3,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S s, PoseStack poseStack, RenderType renderType, int i, int i1,
            int i2, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    }

    @Override
    public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int i, int i1,
            TextureAtlasSprite textureAtlasSprite, boolean b, boolean b1, int i2,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int i3) {
    }

    @Override
    public void submitBlock(PoseStack poseStack, BlockState blockState, int i, int i1, int i2) {
    }

    @Override
    public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
    }

    @Override
    public void submitBlockModel(PoseStack poseStack, RenderType renderType, BlockStateModel blockStateModel, float v,
            float v1, float v2, int i, int i1, int i2) {
    }

    @Override
    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType,
            CustomGeometryRenderer customGeometryRenderer) {
    }

    @Override
    public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
    }

    // Add missing methods if any
    @Override
    public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int i, int i1,
            TextureAtlasSprite textureAtlasSprite, int i2,
            net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    }

    @Override
    public void submitModelPart(ModelPart part, PoseStack pose, RenderType type, int light, int overlay,
            TextureAtlasSprite sprite, boolean p_438944_, boolean p_440215_) {
    }

    @Override
    public void submitModelPart(ModelPart part, PoseStack pose, RenderType type, int light, int overlay,
            TextureAtlasSprite sprite) {
    }
}
