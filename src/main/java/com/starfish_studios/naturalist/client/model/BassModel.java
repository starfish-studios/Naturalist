package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bass;
//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} else if forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} else if forge {
@OnlyIn(Dist.CLIENT)
//?}
public class BassModel extends GeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/bass.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bass.rp_anim.json");
    }
}
