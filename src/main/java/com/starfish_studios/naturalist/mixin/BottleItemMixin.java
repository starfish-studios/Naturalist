package com.starfish_studios.naturalist.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.core.BlockPos;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;

@Mixin(BottleItem.class)
public abstract class BottleItemMixin extends Item { // Changed to abstract
    public BottleItemMixin(@NotNull Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BottleItem;turnBottleIntoItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BEFORE), cancellable = true)
    private void naturalist$use(Level level, Player player, InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        List<AreaEffectCloud> list = level.getEntitiesOfClass(AreaEffectCloud.class,
                player.getBoundingBox().inflate(2.0), areaEffectCloud -> areaEffectCloud != null
                        && areaEffectCloud.isAlive() && areaEffectCloud
                                .getOwner() instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon);
        ItemStack itemstack = player.getItemInHand(hand);
        if (!list.isEmpty()) {
            AreaEffectCloud areaEffectCloud2 = list.get(0);
            areaEffectCloud2.discard();
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH,
                    SoundSource.NEUTRAL, 1.0f, 1.0f);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
            cir.setReturnValue(InteractionResult.SUCCESS); // Was sidedSuccess(itemstack, boolean) which is invalid for
                                                           // InteractionResult
            // Also need to handle item transformation? naturalist$onTurnBottleIntoItem
            // logic was passed?
            // "this.naturalist$onTurnBottleIntoItem(itemstack, player, new
            // ItemStack(Items.DRAGON_BREATH))"
            // Wait, this method call was inside sidedSuccess?
            // Yes. I must call it if I want to update inventory.
            this.naturalist$onTurnBottleIntoItem(itemstack, player, new ItemStack(Items.DRAGON_BREATH));
        } else {
            BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (blockhitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = blockhitresult.getBlockPos();
                if (level.mayInteract(player, blockpos)) {
                    if (level.getFluidState(blockpos).is(NaturalistTags.Fluids.WATERS)) {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        this.naturalist$onTurnBottleIntoItem(itemstack, player, new ItemStack(Items.POTION)); // Assuming
                                                                                                              // water
                                                                                                              // bottle
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }
                }
            }
        }
    }

    @Unique
    protected ItemStack naturalist$onTurnBottleIntoItem(ItemStack bottleStack, Player player,
            ItemStack filledBottleStack) {
        player.awardStat(Stats.ITEM_USED.get((BottleItem) (Object) this));
        return ItemUtils.createFilledResult(bottleStack, player, filledBottleStack);
    }
}
