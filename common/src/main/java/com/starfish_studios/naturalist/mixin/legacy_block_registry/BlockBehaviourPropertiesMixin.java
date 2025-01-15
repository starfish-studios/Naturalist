package com.starfish_studios.naturalist.mixin.legacy_block_registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Legacy block registration in 1.21.4 made by TKD_Kedis
@Mixin(BlockBehaviour.Properties.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Shadow @Nullable
    private ResourceKey<Block> id;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initMixin(CallbackInfo callbackInfo) {
        // Needed
        this.id = ResourceKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("air"));
    }
}
