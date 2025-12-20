package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Butterfly;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class ButterflyModel extends GeoModel<Butterfly> {
    @Override
    public @NotNull ResourceLocation getModelResource(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/butterfly.geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(Butterfly butterfly) {
        String name = butterfly.getVariant().getName();
        return switch (name) {
            case "monarch" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
            case "clouded_yellow" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/clouded_yellow.png");
            case "blue_morpho" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/blue_morpho.png");
            case "green_swallowtail" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/green_swallowtail.png");
            case "jade_green_swallowtail" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/jade_green_swallowtail.png");
            case "purple_emperor" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/purple_emperor.png");
            case "red_admiral" -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/red_admiral.png");
            default -> new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        };
    }

    @Override
    public void setCustomAnimations(Butterfly animatable, long instanceId, AnimationState<Butterfly> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        if (root != null) {
            root.setRotX(0.0F);

            Vec3 motion = animatable.getDeltaMovement();
            double vz = motion.y * 2.0D;
            double hz = motion.horizontalDistance();
            float tilt = (float) Mth.clamp(Math.atan2(vz, hz), -Mth.DEG_TO_RAD * 45.0F, Mth.DEG_TO_RAD * 45.0F);
            float speed = (float) Math.sqrt(hz * hz + vz * vz);
            float factor = Mth.clamp(speed / 0.2F, 0.0F, 1.0F);
            factor *= factor;
            float base = root.getRotX();
            float target = base + tilt * factor;
            root.setRotX(Mth.lerp(0.25F, base, target));
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/butterfly.rp_anim.json");
    }
}
