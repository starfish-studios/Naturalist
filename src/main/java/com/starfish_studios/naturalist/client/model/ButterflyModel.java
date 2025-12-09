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
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

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
        if (butterfly.getVariant().getName().equals("cabbage_white")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/cabbage_white.png");
        } else if (butterfly.getVariant().getName().equals("monarch")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        } else if (butterfly.getVariant().getName().equals("clouded_yellow")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/clouded_yellow.png");
        } else if (butterfly.getVariant().getName().equals("swallowtail")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/swallowtail.png");
        } else if (butterfly.getVariant().getName().equals("blue_morpho")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/blue_morpho.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/monarch.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/butterfly.rp_anim.json");
    }
}
