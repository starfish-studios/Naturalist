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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
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

import java.util.List;
import java.util.Optional;

public class Snail extends ClimbingAnimal implements GeoEntity, Bucketable, HidingAnimal {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);

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

    @Override
    public void aiStep() {
        super.aiStep();
    }

    // BUCKETING

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setFromBucket(pCompound.getBoolean("FromBucket"));
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
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
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
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
        // TODO: used to be 5
        // data.setResetSpeedInTicks(5);
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
}
