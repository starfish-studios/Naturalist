package com.starfish_studios.naturalist.common.item.forge;

import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends MobBucketItem {
    private final @NotNull Supplier<? extends EntityType<?>> typeSup;

    @SuppressWarnings("unused")
    public NoFluidMobBucketWithVariantsItem(@NotNull Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, @NotNull Properties properties, int colorCount) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
        this.typeSup = entitySupplier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag lore) {
        if (this.typeSup.get() == NaturalistEntityTypes.SNAIL.get()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Color", 3)) {
                Snail.Color color = Snail.Color.getTypeById(tag.getInt("Color"));
                tooltip.add(Component.translatable("item.minecraft.firework_star." + color.toString().toLowerCase()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, level, itemstack, hit);
        if (ret != null) return ret;
        if (hit.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(itemstack);

        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos targetPos = pos.relative(direction);
        if (level.mayInteract(player, pos) && player.mayUseItemAt(targetPos, direction, itemstack)) {
            this.checkExtraContent(player, level, itemstack, pos);
            this.playEmptySound(player, level, pos);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, player), level.isClientSide());
        }
        return InteractionResultHolder.fail(itemstack);
    }
}

