package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Catfish;
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
public class CatfishModel extends GeoModel<Catfish> {
    @Override
    public ResourceLocation getModelResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/catfish.geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/catfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/catfish.rp_anim.json");
    }
}
