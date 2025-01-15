package com.starfish_studios.naturalist.mixin.legacy_block_registry;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Legacy block registration in 1.21.4 made by TKD_Kedis
@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Mutable
    @Shadow @Final protected String descriptionId;

    @Shadow public abstract Optional<ResourceKey<LootTable>> getLootTable();

    @Unique
    protected String descriptionIdOld;
    @Unique
    protected ResourceKey<LootTable> dropsOld;
    @Unique
    protected String airDescriptionId = Util.makeDescriptionId("block", ResourceLocation.withDefaultNamespace("air"));

    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    private void getDropsOld(BlockState $$0, LootParams.Builder $$1, CallbackInfoReturnable<List<ItemStack>> callbackInfoReturnable) {
        if (Objects.equals(descriptionId, airDescriptionId)) {
            if (this.getLootTable().isEmpty()) {
                callbackInfoReturnable.setReturnValue(Collections.emptyList());
            } else {
                LootParams $$2 = $$1.withParameter(LootContextParams.BLOCK_STATE, $$0).create(LootContextParamSets.BLOCK);
                ServerLevel $$3 = $$2.getLevel();
                LootTable $$4 = $$3.getServer().reloadableRegistries().getLootTable(this.getLootTable().get());
                callbackInfoReturnable.setReturnValue($$4.getRandomItems($$2));
            }
        }
    }

    @Inject(method = "getLootTable", at = @At("HEAD"), cancellable = true)
    private void getLootTableOld(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> callbackInfoReturnable) {
        if (((BlockBehaviour)(Object)this) instanceof Block block && Objects.equals(descriptionId, airDescriptionId)) {
            if (this.dropsOld == null) {
                ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
                this.dropsOld = ResourceKey.create(Registries.LOOT_TABLE, resourceLocation.withPrefix("blocks/"));
            }
            callbackInfoReturnable.setReturnValue(Optional.ofNullable(this.dropsOld));
        }
    }

    @Inject(method = "getDescriptionId", at = @At("HEAD"), cancellable = true)
    private void getDescriptionIdOld(CallbackInfoReturnable<String> callbackInfoReturnable) {
        if (((BlockBehaviour)(Object)this) instanceof Block block && Objects.equals(descriptionId, airDescriptionId)) {
            if (this.descriptionIdOld == null) {
                this.descriptionIdOld = Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(block));
                this.descriptionId = this.descriptionIdOld;
            }
            callbackInfoReturnable.setReturnValue(this.descriptionIdOld);
        }
    }
}
