package com.starfish_studios.naturalist.mixin.fabric;

import com.starfish_studios.naturalist.core.platform.fabric.CommonPlatformHelperImpl;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.Builder.class)
public abstract class NaturalistPotionBrewingMixin {

    @Shadow
    public abstract void addMix(Holder<Potion> input, Item ingredient, Holder<Potion> output);

    @Inject(method = "build", at = @At("HEAD"))
    private void onBuild(CallbackInfoReturnable<PotionBrewing> cir) {
        for (CommonPlatformHelperImpl.BrewingRecipe recipe : CommonPlatformHelperImpl.RECIPES) {
            this.addMix(recipe.input(), recipe.ingredient(), recipe.output());
        }
    }
}
