package com.starfish_studios.naturalist.mixin.fabric;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void addNaturalistMixes(PotionBrewing.Builder builder, CallbackInfo callbackInfo) {
        Naturalist.registerBrewingRecipes(builder);
    }
}
