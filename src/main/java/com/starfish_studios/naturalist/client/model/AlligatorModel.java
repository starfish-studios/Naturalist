package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Alligator;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class AlligatorModel extends GeoModel<Alligator> {
    private final Map<Long, Float> lerpedTailRotMap = new HashMap<>();
    private final Map<Long, Float> lerpedTailPitchMap = new HashMap<>();
    private final Map<Long, Float> lerpedRootPitchMap = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/alligator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/alligator/alligator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/alligator.rp_anim.json");
    }

    @Override
    public void setCustomAnimations(Alligator entity, long instanceId, @Nullable AnimationState<Alligator> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        
        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone tail = this.getAnimationProcessor().getBone("tail");

        if (root != null && entity.isInWater()) {
            int entityId = entity.getId();
            Vec3 deltaMovement = entity.getDeltaMovement();
            
            double horizontalDistance = Math.sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.z * deltaMovement.z);
            
            if (horizontalDistance > 0.001D) {
                double pitchRad = Math.atan2(-deltaMovement.y, horizontalDistance);
                float targetPitch = Mth.clamp((float) pitchRad, -Mth.HALF_PI * 0.7F, Mth.HALF_PI * 0.7F);
                
                float lerpedPitch = lerpedRootPitchMap.getOrDefault((long) entityId, targetPitch);
                float deltaPitch = targetPitch - lerpedPitch;
                lerpedPitch += deltaPitch * 0.1F;
                
                if (Math.abs(deltaPitch) < 0.001F) {
                    lerpedPitch = targetPitch;
                }
                
                if (Float.isFinite(lerpedPitch)) {
                    lerpedRootPitchMap.put((long) entityId, lerpedPitch);
                    root.setRotX(lerpedPitch);
                }
            } else {
                float lerpedPitch = lerpedRootPitchMap.getOrDefault((long) entityId, 0.0F);
                lerpedPitch *= 0.9F;
                if (Math.abs(lerpedPitch) < 0.001F) {
                    lerpedPitch = 0.0F;
                }
                lerpedRootPitchMap.put((long) entityId, lerpedPitch);
                root.setRotX(lerpedPitch);
            }
        } else if (root != null) {
            root.setRotX(0.0F);
        }

        if (head != null) {
            if (entity.isBaby()) {
                head.setScaleX(1.5F);
                head.setScaleY(1.5F);
                head.setScaleZ(1.5F);
            } else {
                head.setScaleX(1.0F);
                head.setScaleY(1.0F);
                head.setScaleZ(1.0F);
            }

            if (extraDataOfType != null) {
                float headPitch = extraDataOfType.headPitch() * Mth.DEG_TO_RAD;
                float headYaw = extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD;
                head.setRotX(headPitch);
                head.setRotY(headYaw);
            }
        }

        if (tail != null) {
            int entityId = entity.getId();
            
            float targetTailYawDeg = entity.yBodyRot - entity.getTailRot();
            float targetTailYawRad = targetTailYawDeg * Mth.DEG_TO_RAD;

            float lerpedTailRot = lerpedTailRotMap.getOrDefault((long) entityId, targetTailYawRad);

            float currentTailRotDeg = lerpedTailRot * Mth.RAD_TO_DEG;
            float deltaDeg = Mth.wrapDegrees(targetTailYawDeg - currentTailRotDeg);
            float deltaRad = deltaDeg * Mth.DEG_TO_RAD;

            lerpedTailRot += deltaRad * 0.3F;

            if (Math.abs(deltaRad) < 0.001F) {
                lerpedTailRot = targetTailYawRad;
            }

            if (!Float.isFinite(lerpedTailRot)) {
                lerpedTailRot = targetTailYawRad;
            }
            lerpedTailRotMap.put((long) entityId, lerpedTailRot);
            tail.setRotY(lerpedTailRot);

            double deltaY = entity.getDeltaMovement().y;
            
            float targetTailPitchRad = Mth.clamp((float) deltaY * 0.5F, -0.5F, 0.5F);

            float lerpedTailPitch = lerpedTailPitchMap.getOrDefault((long) entityId, targetTailPitchRad);

            float currentTailPitchRad = lerpedTailPitch;
            float deltaPitchRad = targetTailPitchRad - currentTailPitchRad;

            lerpedTailPitch += deltaPitchRad * 0.3F;

            if (Math.abs(deltaPitchRad) < 0.001F) {
                lerpedTailPitch = targetTailPitchRad;
            }

            if (!Float.isFinite(lerpedTailPitch)) {
                lerpedTailPitch = targetTailPitchRad;
            }
            lerpedTailPitchMap.put((long) entityId, lerpedTailPitch);
            tail.setRotX(lerpedTailPitch);
        }
    }
}
