package com.starfish_studios.naturalist.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

import static net.minecraft.util.Mth.lerp;

public class Whale extends WaterAnimal implements GeoEntity {
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static EntityDataAccessor<Integer> MOISTNESS_LEVEL;
    public Whale(EntityType<? extends Whale> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.01F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 5);
        this.setCanPickUpLoot(true);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setAirSupply(this.getMaxAirSupply());
        this.setXRot(0.0F);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    protected void handleAirSupply(int airSupply) {
    }

    public int getMoistnessLevel() {
        return this.entityData.get(MOISTNESS_LEVEL);
    }

    public void setMoisntessLevel(int moistnessLevel) {
        this.entityData.set(MOISTNESS_LEVEL, moistnessLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOISTNESS_LEVEL, 2400);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Moistness", this.getMoistnessLevel());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setMoisntessLevel(compound.getInt("Moistness"));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2000000476837158, true));
        this.goalSelector.addGoal(8, new FollowBoatGoal(this));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Guardian.class)).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 1.2000000476837158).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    /*public boolean doHurtTarget(Entity target) {
        boolean bl = target.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (bl) {
            this.doEnchantDamageEffects(this, target);
            this.playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
        }

        return bl;
    }

     */

    public int getMaxAirSupply() {
        return 4800;
    }

    protected int increaseAirSupply(int currentAir) {
        return this.getMaxAirSupply();
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 1.1F;
    }

    protected boolean canRide(Entity vehicle) {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.isInWaterRainOrBubble()) {
                this.setMoisntessLevel(2400);
            } else {
                this.setMoisntessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.hurt(this.damageSources().dryOut(), 1.0F);
                }

                if (this.onGround()) {
                    this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.setOnGround(false);
                    this.hasImpulse = true;
                }
            }

        }
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }

    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    static {
        MOISTNESS_LEVEL = SynchedEntityData.defineId(Whale.class, EntityDataSerializers.INT);
    }



    // ANIMATIONS

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 10, this::animController));
    }

    protected <E extends Whale> PlayState animController(final AnimationState<E> event) {
        return event.isMoving() && this.isInWater() ? event.setAndContinue(SWIM) : event.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    static {
        MOISTNESS_LEVEL = SynchedEntityData.defineId(Whale.class, EntityDataSerializers.INT);
    }

}
