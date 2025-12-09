package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Caterpillar;
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
public class CaterpillarModel extends GeoModel<Caterpillar> {
    @Override
    public ResourceLocation getModelResource(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/caterpillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(Caterpillar animatable) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/caterpillar.rp_anim.json");
    }
}
