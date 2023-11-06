package com.starfish_studios.naturalist.item.forge;

import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class NoFluidMobBucketWithVariantsItem extends MobBucketItem {
    private final int colorCount;
    private final Supplier<? extends EntityType<?>> typeSup;

    private EntityType<?> type() {
        return this.typeSup.get();
    }
    public NoFluidMobBucketWithVariantsItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties, int colorCount) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
        this.colorCount = colorCount;
        this.typeSup = entitySupplier;
    }

    /*
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < colorCount; i++) {
            ItemStack colorStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Color", i);
            colorStack.setTag(compoundTag);
            items.add(colorStack);
        }
    }
    */

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type() == NaturalistEntityTypes.SNAIL.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("Color", 3)) {
                Snail.Color color = Snail.Color.getTypeById(compoundnbt.getInt("Color"));
                tooltip.add((Component.translatable(String.format("tooltip.naturalist.%s", color.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = pos.relative(direction);
            if (pLevel.mayInteract(pPlayer, pos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkExtraContent(pPlayer, pLevel, itemstack, pos);
                this.playEmptySound(pPlayer, pLevel, pos);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
