package com.starfish_studios.naturalist.mixin;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.SearchableMidnightConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MidnightConfig.class)
public abstract class MidnightConfigMixin {

    @Inject(method = "getScreen(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)Lnet/minecraft/client/gui/screens/Screen;", at = @At("HEAD"), cancellable = true)
    private static void naturalist$injectSearchableConfigScreen(Screen parent, String modid, CallbackInfoReturnable<Screen> cir) {
        cir.setReturnValue(new SearchableMidnightConfigScreen(parent, modid));
    }
}

