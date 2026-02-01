package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CaughtMobItem extends MobBucketItem {
    private final EntityType<?> type;

    @SuppressWarnings("deprecation")
    public CaughtMobItem(EntityType<? extends net.minecraft.world.entity.Mob> entitySupplier, Fluid fluid,
            SoundEvent emptyingSound, Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.type = entitySupplier;
    }

    // @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag lore) {
        if (this.type == NaturalistEntityTypes.BUTTERFLY.get()) {
            net.minecraft.world.item.component.CustomData customData = stack
                    .get(net.minecraft.core.component.DataComponents.BUCKET_ENTITY_DATA);
            if (customData != null) {
                net.minecraft.nbt.CompoundTag compoundnbt = customData.copyTag();
                if (compoundnbt.contains("Variant")) {
                    /*
                     * Butterfly.Variant variant =
                     * Butterfly.Variant.getTypeById(compoundnbt.getInt("Variant"));
                     * tooltip.add((Component
                     * .translatable(String.format("tooltip.naturalist.%s",
                     * variant.toString().toLowerCase()))
                     * .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
                     */
                }
            }
        }
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, @NotNull BlockPos pos) {
        Entity entity = this.type.spawn(serverLevel, itemStack, null, pos, EntitySpawnReason.BUCKET, true, false);
        if (entity instanceof Catchable catchable) {
            catchable.setFromHand(true);
            catchable.loadFromHandTag(itemStack);
        }
    }

    // @Override // Removed override as checkExtraContent might be static or
    // different
    public void checkNaturalistExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack stack,
            @NotNull BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel) level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }

    }

    protected ItemStack getCaughtMobEmptySuccessItem(@NotNull ItemStack stack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.BUCKET) : stack;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player,
            @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = pos.relative(direction);
            if (level.mayInteract(player, pos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkNaturalistExtraContent(player, level, itemstack, pos);
                this.playEmptySound(player, level, pos);
                player.awardStat(Stats.ITEM_USED.get(this));
                ItemStack emptyStack = this.getCaughtMobEmptySuccessItem(itemstack, player);
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, emptyStack);
                }
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
