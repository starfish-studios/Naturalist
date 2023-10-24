package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.ClimbingAnimal;
import com.starfish_studios.naturalist.common.entity.core.HidingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.HideGoal;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimationState;

import java.util.*;

public class Snail extends ClimbingAnimal implements GeoEntity, Bucketable, HidingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_COLOR;

    public Snail(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    // ATTRIBUTES/BREEDING/AI

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new HideGoal<>(this));
        this.goalSelector.addGoal(1, new SnailStrollGoal(this, 0.9D, 0.0F));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(this.canHide() ? strength / 4 : strength, x, z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, this.canHide() ? amount * 0.8F : amount);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.3F;
    }

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    // BUCKETING

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(DATA_COLOR, Color.BROWN.getId());
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
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("FromBucket", this.fromBucket());
        pCompound.putByte("Color", (byte)this.getSnailColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setFromBucket(pCompound.getBoolean("FromBucket"));
        this.setSnailColor(Color.BY_ID[pCompound.getInt("Color")]);
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

    public void setColor(DyeColor color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

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

        public static final Snail.Color[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Snail.Color::getId)).toArray(Snail.Color[]::new);
        private final int id;
        private final String name;

        private Color(int j, String string2, boolean bl) {
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
                if (type.id == id) return type;
            }
            return Snail.Color.BROWN;
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();
        label90: {
                if (!(item instanceof DyeItem)) {
                    break label90;
                }

                DyeItem dyeItem = (DyeItem)item;

                DyeColor dyeColor = dyeItem.getDyeColor();
                if (dyeColor != this.getColor()) {
                    this.setColor(dyeColor);
                    if (!pPlayer.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        return bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }


    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.BUCKET && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            ItemStack bucketStack = entity.getBucketItemStack();
            entity.saveToBucketTag(bucketStack);
            ItemStack resultStack = ItemUtils.createFilledResult(stack, player, bucketStack, false);
            player.setItemInHand(hand, resultStack);
            Level level = entity.level();
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucketStack);
            }

            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        compoundTag.putInt("Color", this.getSnailColor().getId());
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        int i = tag.getInt("Color");
        if (i >= 0 && i < Snail.Color.BY_ID.length) {
            this.setSnailColor(Snail.Color.BY_ID[i]);
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(NaturalistItems.SNAIL_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return NaturalistSoundEvents.BUCKET_FILL_SNAIL.get();
    }

    // SOUNDS

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.SNAIL_CRUSH.get();
    }

    // ANIMATION

    @Override
    public boolean canHide() {
        List<Player> players = this.level().getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D).selector(EntitySelector.NO_CREATIVE_OR_SPECTATOR::test), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends Snail> PlayState predicate(final AnimationState<E> event) {
        if (this.canHide()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("retreat"));
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("crawl"));
            event.getController().setAnimationSpeed(0.6F);
        } else if (this.isClimbing()){
            event.getController().setAnimation(RawAnimation.begin().thenLoop("climb"));
            event.getController().setAnimationSpeed(0.6F);
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
            event.getController().setAnimationSpeed(0.4F);
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Snail> event) {
        Snail snail = event.getAnimatable();
        if (snail.level().isClientSide) {
            if (event.getKeyframeData().getSound().equals("forward")) {
                snail.level().playLocalSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_FORWARD.get(), snail.getSoundSource(), 0.5F, 1.0F, false);
            }
            if (event.getKeyframeData().getSound().equals("back")) {
                snail.level().playLocalSound(snail.getX(), snail.getY(), snail.getZ(), NaturalistSoundEvents.SNAIL_BACK.get(), snail.getSoundSource(), 0.5F, 1.0F, false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Snail> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        controller.setSoundKeyframeHandler(this::soundListener);
        controllers.add(controller);
    }


    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        public SnailStrollGoal(PathfinderMob pMob, double pSpeedModifier, float pProbability) {
            super(pMob, pSpeedModifier, pProbability);
            this.forceTrigger = true;
            this.interval = 1;
        }
    }

    static {
        DATA_COLOR = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
        FROM_BUCKET = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    }
}
