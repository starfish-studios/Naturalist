package com.starfish_studios.naturalist.common.entity;

import net.minecraft.world.item.Items;

import com.starfish_studios.naturalist.common.entity.core.*;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.EggLayingBreedGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.LayEggGoal;
import com.starfish_studios.naturalist.core.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.KeyFrameEvent;
import software.bernie.geckolib.animation.keyframe.event.data.SoundKeyframeData;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

public class Snail extends ClimbingAnimal implements NaturalistGeoEntity, Bucketable, HidingAnimal, EggLayingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEETROOT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Snail.class,
            EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Snail.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Snail.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Snail.class,
            EntityDataSerializers.BOOLEAN);
    int layEggCounter;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.snail.idle");
    protected static final RawAnimation CRAWL = RawAnimation.begin().thenLoop("animation.sf_nba.snail.crawl");
    protected static final RawAnimation CLIMB = RawAnimation.begin().thenLoop("animation.sf_nba.snail.climb");
    protected static final RawAnimation HIDE = RawAnimation.begin().thenPlay("animation.sf_nba.snail.hide_start")
            .thenLoop("animation.sf_nba.snail.hide_idle");

    public Snail(@NotNull EntityType<? extends NaturalistAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new SnailStrollGoal(this, 0.9D, 0.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.canHide() ? strength / 4 : strength, x, z);
    }

    /*
     * Removed for 1.21 - hurt() is final
     * 
     * @Override
     * public boolean hurt(@NotNull DamageSource source, float amount) {
     * return super.hurt(source, this.canHide() ? amount * 0.8F : amount);
     * }
     */

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public Block getEggBlock() {
        return NaturalistBlocks.SNAIL_EGGS.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return NaturalistTags.BlockTags.ALLIGATOR_EGG_LAYABLE_ON;
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.layEggCounter = isLayingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return NaturalistEntityTypes.SNAIL.get().create(level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        super.travel(vec3);
        if (this.canHide()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
            vec3.multiply(0, 1, 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.canHide() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(DATA_COLOR, Color.BROWN.getId());
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("FromBucket", Codec.BOOL, this.fromBucket());
        output.store("Color", Codec.INT, this.getSnailColor().getId());
        output.store("HasEgg", Codec.BOOL, this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFromBucket(input.read("FromBucket", Codec.BOOL).orElse(false));
        this.setSnailColor(Color.BY_ID[input.read("Color", Codec.INT).orElse(12)]);
        this.setHasEgg(input.read("HasEgg", Codec.BOOL).orElse(false));
    }

    public Color getSnailColor() {
        return Snail.Color.BY_ID[this.entityData.get(DATA_COLOR)];
    }

    public void setSnailColor(Snail.Color color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR));
    }

    public void setColor(@NotNull DyeColor color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    @SuppressWarnings("unused")
    public enum Color {
        WHITE(0, "white", true),
        ORANGE(1, "orange", true),
        MAGENTA(2, "magenta", true),
        LIGHT_BLUE(3, "light_blue", true),
        YELLOW(4, "yellow", true),
        LIME(5, "lime", true),
        PINK(6, "pink", true),
        GRAY(7, "gray", true),
        LIGHT_GRAY(8, "light_gray", true),
        CYAN(9, "cyan", true),
        PURPLE(10, "purple", true),
        BLUE(11, "blue", true),
        BROWN(12, "brown", true),
        GREEN(13, "green", true),
        RED(14, "red", true),
        BLACK(15, "black", true);

        public static final Snail.Color[] BY_ID = Arrays.stream(values())
                .sorted(Comparator.comparingInt(Snail.Color::getId)).toArray(Snail.Color[]::new);
        private final int id;
        private final String name;

        Color(int j, String string2, boolean bl) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static Snail.Color getTypeById(int id) {
            for (Snail.Color type : values()) {
                if (type.id == id)
                    return type;
            }
            return Snail.Color.BROWN;
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        label90: {
            if (!(item instanceof DyeItem dyeItem)) {
                break label90;
            }

            DyeColor dyeColor = dyeItem.getDyeColor();
            if (dyeColor != this.getColor()) {
                this.setColor(dyeColor);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(@NotNull Player player,
            @NotNull InteractionHand hand, T entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.BUCKET && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            entity.saveToBucketTag(stack);
            ItemStack resultStack = ItemUtils.createFilledResult(stack, player, entity.getBucketItemStack(), false);
            player.setItemInHand(hand, resultStack);
            Level level = entity.level();
            if (!level.isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, stack);
            }

            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        // stack.set(NaturalistDataComponents.SNAIL_COLOR,
        // this.getSnailColor().getId()); // TODO: Implement DataComponents
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        // int i = tag.getInt("Color").orElse(0); // TODO: Use DataComponents
        // if (i >= 0 && i < Snail.Color.BY_ID.length) {
        // this.setSnailColor(Snail.Color.BY_ID[i]);
        // }
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistItems.SNAIL_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return NaturalistSoundEvents.BUCKET_FILL_SNAIL.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAIL_CRUSH.get();
    }

    @Override
    public boolean canHide() {
        List<Player> players = this.level().getEntitiesOfClass(Player.class,
                this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D),
                player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && !player.isDiscrete()
                        && !player.isHolding(FOOD_ITEMS));
        return !players.isEmpty();
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState predicate(AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            controller.setAnimation(CRAWL);
        } else if (this.isNaturalistClimbing()) {
            controller.setAnimation(CLIMB);
        } else {
            controller.setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    private <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState hidePredicate(
            final AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.canHide()) {
            controller.setAnimation(HIDE);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private void soundListener(KeyFrameEvent<Snail, SoundKeyframeData> event) {
        Snail snail = this;
        if (snail.level().isClientSide()) {
            if (event.keyframeData().getSound().equals("snail_forward")) {
                snail.level().playLocalSound(snail.getX(), snail.getY(), snail.getZ(),
                        NaturalistSoundEvents.SNAIL_FORWARD.get(), snail.getSoundSource(), 0.5f, 1.0f, false);
            } else if (event.keyframeData().getSound().equals("snail_back")) {
                snail.level().playLocalSound(snail.getX(), snail.getY(), snail.getZ(),
                        NaturalistSoundEvents.SNAIL_BACK.get(), snail.getSoundSource(), 0.5f, 1.0f, false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Snail>("controller", 10, this::predicate)
                .setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<Snail>("hideController", 0, this::hidePredicate));
    }

    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        public SnailStrollGoal(PathfinderMob mob, double speedModifier, float pProbability) {
            super(mob, speedModifier, pProbability);
            this.forceTrigger = true;
            this.interval = 1;
        }
    }
}