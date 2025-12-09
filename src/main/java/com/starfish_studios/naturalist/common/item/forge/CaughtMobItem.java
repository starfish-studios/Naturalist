package com.starfish_studios.naturalist.common.item.forge;

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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class CaughtMobItem extends NoFluidMobBucketItem {
    private final Supplier<? extends EntityType<?>> typeSup;

    public CaughtMobItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
        this.typeSup = entitySupplier;
    }

    private EntityType<?> type() {
        return this.typeSup.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag lore) {
        if (this.type() == NaturalistEntityTypes.BUTTERFLY.get()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("Variant", 3)) {
                Butterfly.Variant variant = Butterfly.Variant.getTypeById(tag.getInt("Variant"));
                tooltip.add(Component.translatable("tooltip.naturalist." + variant.toString().toLowerCase()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
        }
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos pos) {
        Entity entity = this.type().spawn(serverLevel, itemStack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Catchable catchable) {
            catchable.loadFromHandTag(itemStack.getOrCreateTag());
            catchable.setFromHand(true);
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack stack, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            spawn(serverLevel, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, level, stack, hit);
        if (eventResult != null) return eventResult;

        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos targetPos = pos.relative(direction);
        if (level.mayInteract(player, pos) && player.mayUseItemAt(targetPos, direction, stack)) {
            checkExtraContent(player, level, stack, pos);
            playEmptySound(player, level, pos);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
        }
        return InteractionResultHolder.fail(stack);
    }

    public static @NotNull ItemStack getEmptySuccessItem(@NotNull ItemStack stack, Player player) {
        return player.getAbilities().instabuild ? stack : new ItemStack(Items.AIR);
    }
}

