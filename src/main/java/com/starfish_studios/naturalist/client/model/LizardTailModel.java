package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.LizardTail;
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
public class LizardTailModel extends GeoModel<LizardTail> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/green_tail.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown_tail.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/beardie_tail.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko_tail.png"),
    };

    @Override
    public ResourceLocation getModelResource(LizardTail lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/entity/lizard_tail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LizardTail lizard) {
        return TEXTURE_LOCATIONS[lizard.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(LizardTail lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lizard_tail.rp_anim.json");
    }
}
