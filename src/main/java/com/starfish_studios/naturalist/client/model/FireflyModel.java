package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Firefly;
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
public class FireflyModel extends GeoModel<Firefly> {
    @Override
    public ResourceLocation getModelResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/firefly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/firefly.rp_anim.json");
    }
}
