package com.starfish_studios.naturalist.common.item.fabric;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
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

    public CaughtMobItem(EntityType<? extends Mob> entitySupplier, Fluid content, SoundEvent emptySound, Properties properties) {
        super(entitySupplier, content, emptySound, properties);
        this.type = entitySupplier;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == NaturalistEntityTypes.BUTTERFLY) {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (customData.copyTag().contains("Variant", 3)) {
                Butterfly.Variant variant = Butterfly.Variant.getTypeById(customData.copyTag().getInt("Variant"));
                tooltip.add((Component.translatable(String.format("tooltip.naturalist.%s", variant.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        } /* else if (this.type == NaturalistEntityTypes.MOTH.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("Variant", 3)) {
                Moth.Variant variant = Moth.Variant.getTypeById(compoundnbt.getInt("Variant"));
                tooltip.add((Component.translatable(String.format("tooltip.naturalist.%s", variant.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        }
        */
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos pos) {
        Entity entity = this.type.spawn(serverLevel, itemStack, null, pos, EntitySpawnReason.BUCKET, true, false);
        if (entity instanceof Catchable catchable) {
            CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            catchable.loadFromHandTag(customData.copyTag());
            catchable.setFromHand(true);
        }

    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack containerStack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, containerStack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }

    }


    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = pos.relative(direction);
            if (pLevel.mayInteract(pPlayer, pos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkExtraContent(pPlayer, pLevel, itemstack, pos);
                this.playEmptySound(pPlayer, pLevel, pos);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public static @NotNull ItemStack getEmptySuccessItem(ItemStack bucketStack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.AIR) : bucketStack;
    }
}
