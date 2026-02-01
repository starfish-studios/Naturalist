package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import com.starfish_studios.naturalist.common.entity.core.SleepingAnimal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.network.syncher.EntityDataAccessor;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;

import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class Bear extends NaturalistAnimal implements NeutralMob, NaturalistGeoEntity, SleepingAnimal, Shearable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.HONEYCOMB, Items.SWEET_BERRIES, Items.COD,
            Items.SALMON, Items.TROPICAL_FISH, NaturalistItems.VENISON.get(), NaturalistItems.COOKED_VENISON.get());
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNIFFING = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bear.class,
            EntityDataSerializers.INT);
    @Nullable
    private UUID persistentAngerTarget;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.bear.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.bear.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.bear.run");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.sf_nba.bear.sit");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.bear.sleep");
    protected static final RawAnimation SNIFF = RawAnimation.begin().thenLoop("animation.sf_nba.bear.sniff");
    protected static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.sf_nba.bear.eat");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.sf_nba.bear.attack");

    public Bear(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    @Override
    public void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.25D);
        } else {
            this.setSprinting(false);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        // 1.21: EntityType.create() requires EntitySpawnReason parameter
        return NaturalistEntityTypes.BEAR.get().create(level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.TEMPT_RANGE, 10.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BearFloatGoal(this));
        // V27: Priority 1 for Eating ensures they eat immediately instead of walking
        // around
        this.goalSelector.addGoal(1, new BearPickupFoodAndSitGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new BearMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new BearSleepGoal(this));
        this.goalSelector.addGoal(4, new BearTemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new DistancedFollowParentGoal(this, 1.25D, 48.0D, 8.0D, 12.0D));
        this.goalSelector.addGoal(5, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8, 2));
        this.goalSelector.addGoal(6, new BearHarvestFoodGoal(this, 1.2F, 12, 3));
        // this.goalSelector.addGoal(7, new BearPickupFoodAndSitGoal(this)); // Moved to
        // 1
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BearAttackPlayerNearBabiesGoal(this, Player.class, 20, false, true, null));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                        (entity, level) -> this.isAngryAt(entity, level)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Deer.class, 10, true, false, null));

        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Salmon.class, 10, true, false, null));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Cod.class, 10, true, false, null));
        this.targetSelector.addGoal(6,
                new NearestAttackableTargetGoal<>(this, TropicalFish.class, 10, true, false, null));
        this.targetSelector.addGoal(4,
                new NearestAttackableTargetGoal<>(this, PathfinderMob.class, 10, true, false,
                        (entity, level) -> entity.getType().is(NaturalistTags.EntityTypes.BEAR_HOSTILES)
                                && !this.isSleeping()
                                && !this.isBaby()));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    // V27: Restrict pickup to FOOD_ITEMS only.
    // Fix V27b: Removed wantsToPickUp (API mismatch) and fixed predicate
    // compilation errors.

    // ... (rest of code) ...

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isSitting()) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0); // Kill horizontal momentum
            this.setZza(0.0F);
        }
        if (!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }

        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandItem().isEmpty()) {
            if (this.isAngry()) {
                this.stopBeingAngry();
            }
            this.setSniffing(false);
        }
        // looting section commented out - APIs changed in 1.21\n //
        // this.level().getProfiler().push(\"looting\");\n // if
        // (!this.level().isClientSide() && this.canPickUpLoot() && this.isAlive() &&
        // !this.dead\n // &&
        // this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {\n //
        // for (ItemEntity itementity :
        // this.level().getEntitiesOfClass(ItemEntity.class,\n //
        // this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D))) {\n // if
        // (!itementity.isRemoved() && !itementity.getItem().isEmpty()\n // &&
        // this.wantsToPickUp(itementity.getItem())) {\n //
        // this.pickUpItem(itementity);\n // }\n // }\n // }\n //
        // this.level().getProfiler().pop();
    }

    // isInvulnerableTo signature changed in 1.21 (now takes ServerLevel)
    // @Override
    // public boolean isInvulnerableTo(DamageSource source) {
    // return source.equals(this.damageSources().sweetBerryBush()) ||
    // super.isInvulnerableTo(source);
    // }

    // getStandingEyeHeight removed in 1.21 - EntityDimensions.height is private

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(SNIFFING, false);
        builder.define(SITTING, false);
        builder.define(SHEARED, false);
        builder.define(EAT_COUNTER, 0);
        builder.define(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueInput input) {
        super.readAdditionalSaveData(input);
        this.readPersistentAngerSaveData(this.level(), input);
        input.read("Sheared", com.mojang.serialization.Codec.BOOL).ifPresent(this::setSheared);
    }

    @Override
    public void addAdditionalSaveData(@NotNull net.minecraft.world.level.storage.ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.addPersistentAngerSaveData(output);
        output.store("Sheared", com.mojang.serialization.Codec.BOOL, this.isSheared());
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDayTime();
        return (dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 6000 && !this.isAngry()
                && !this.level().isWaterAt(this.blockPosition());
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isSniffing() {
        return this.entityData.get(SNIFFING);
    }

    public void setSniffing(boolean sniffing) {
        this.entityData.set(SNIFFING, sniffing);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(SHEARED, sheared);
    }

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eat) {
        this.entityData.set(EAT_COUNTER, eat ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.entityData.set(EAT_COUNTER, amount);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    private void handleEating() {
        if (this.getEatCounter() > 0 && this.tickCount % 20 == 0) {
            // System.out.println("Bear " + (this.level().isClientSide() ? "Client" :
            // "Server") +
            // " Eating Tick: " + this.getEatCounter() +
            // " Sitting: " + isSitting() +
            // " Item: " + this.getMainHandItem());
        }

        if (!this.isEating() && this.isSitting() && !this.isSleeping() && !this.getMainHandItem().isEmpty()) {
            if (this.random.nextInt(80) == 1 || this.getEatCounter() == 0) { // Eagerly start if just sat down?
                // Actually, just remove RNG if we want continuous eating.
                // But we want it to look natural?
                // If we remove RNG, it eats instantly.
                // Let's reduce RNG or make it guarantee if Sitting.

                this.eat(true);
            }
        } else if (this.getMainHandItem().isEmpty()) {
            if (!this.level().isClientSide() && this.isSitting()) {

                this.setSitting(false);
            }
            this.eat(false);
        }
        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.level().isClientSide() && this.getEatCounter() > 40) {

                if (this.isFood(this.getItemBySlot(EquipmentSlot.MAINHAND))) {
                    if (!this.level().isClientSide()) {

                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);
                        this.setSheared(false);
                    }
                } else {
                    // System.out
                    // .println("Bear Item NOT Food or logic skip: " +
                    // this.getItemBySlot(EquipmentSlot.MAINHAND));
                }

                this.setSitting(false);
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0 || this.getEatCounter() == 0) {
            this.playSound(NaturalistSoundEvents.BEAR_EAT.get(), 0.5F + 0.5F * (float) this.random.nextInt(2),
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for (int i = 0; i < 6; ++i) {
                Vec3 speedVec = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.1D);
                speedVec = speedVec.xRot(-this.getXRot() * ((float) Math.PI / 180F));
                speedVec = speedVec.yRot(-this.getYRot() * ((float) Math.PI / 180F));
                double y = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3 posVec = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.8D, y,
                        1.0D + ((double) this.random.nextFloat() - 0.5D) * 0.4D);
                posVec = posVec.yRot(-this.yBodyRot * ((float) Math.PI / 180F));
                posVec = posVec.add(this.getX(), this.getEyeY() - 0.2D, this.getZ() - 0.1D);
                this.level().addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)),
                        posVec.x, posVec.y, posVec.z, speedVec.x, speedVec.y + 0.05D, speedVec.z);
            }
        }
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    // hurt() returns void in 1.21
    // @Override
    // public boolean hurt(@NotNull DamageSource source, float amount) {
    // if (!this.getMainHandItem().isEmpty() && !this.level().isClientSide()) {
    // ItemEntity itemEntity = new ItemEntity(this.level(), this.getX() +
    // this.getLookAngle().x,
    // this.getY() + 1.0D, this.getZ() + this.getLookAngle().z,
    // this.getMainHandItem());
    // itemEntity.setPickUpDelay(80);
    // itemEntity.setThrower(this.getUUID());
    // this.playSound(NaturalistSoundEvents.BEAR_SPIT.get(), 1.0F, 1.0F);
    // this.level().addFreshEntity(itemEntity);
    // this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    // }
    // return super.hurt(source, amount);
    // }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        var tag = CommonPlatformHelper.getShearsTag();
        if ((itemStack.is(tag) || itemStack.is(Items.SHEARS)) && this.readyForShearing()) {
            if (!this.isSleeping()) {
                this.setLastHurtByMob(player);
                this.setPersistentAngerTarget(player.getUUID());
                this.startPersistentAngerTimer();
            }
            if (!this.level().isClientSide()) {
                this.shear((ServerLevel) this.level(), SoundSource.PLAYERS, itemStack);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player,
                        hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        } else if (itemStack.is(Items.SHEARS) && !this.readyForShearing()) {
            // System.out.println("Bear Shearing Failed: Alive=" + this.isAlive() + ",
            // Sheared=" + this.isSheared()
            // + ", Baby=" + this.isBaby());
        }
        return super.mobInteract(player, hand);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void shear(@NotNull ServerLevel level, @NotNull SoundSource source, @NotNull ItemStack itemStack) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0f, 1.0f);
        this.setSheared(true);
        int amount = 1 + this.random.nextInt(2);
        for (int j = 0; j < amount; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation((ServerLevel) this.level(),
                    new net.minecraft.world.item.ItemStack(NaturalistItems.FUR.get()), 1);
            if (itemEntity == null)
                continue;
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    void tryToSit() {
        if (this.isTouchingWater()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setZza(0.0F);
            this.getNavigation().stop();
            this.setSitting(true);
        }
    }

    boolean isTouchingWater() {
        return !this.level().isWaterAt(this.blockPosition());
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.BEAR_HURT_BABY.get() : NaturalistSoundEvents.BEAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BEAR_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? NaturalistSoundEvents.BEAR_SLEEP.get()
                : this.isBaby() ? NaturalistSoundEvents.BEAR_AMBIENT_BABY.get()
                        : NaturalistSoundEvents.BEAR_AMBIENT.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isSleeping() ? super.getVoicePitch() * 0.3F
                : this.isBaby() ? super.getVoicePitch() * 0.4F : super.getVoicePitch();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState predicate(
            final software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();

        // Debug Logging
        // if (this.level().getGameTime() % 5 == 0) { // Log every 5 ticks to avoid
        // extreme lag
        // String currentAnim = controller.getCurrentAnimation() != null
        // ? controller.getCurrentAnimation().animation().name()
        // : "null";
        // System.out.println("Bear Anim: " + currentAnim +
        // " | AnimSpeed: " + controller.getAnimationSpeed() +
        // " | Velocity: " + String.format("%.4f",
        // this.getDeltaMovement().horizontalDistance()) +
        // " | IsEating: " + this.isEating() +
        // " | IsSitting: " + this.isSitting());
        // }

        if (this.isSleeping()) {
            controller.setAnimation(SLEEP);
            return PlayState.CONTINUE;
        } else if (this.isEating()
                || (this.isSitting() && !this.getMainHandItem().isEmpty() && this.isFood(this.getMainHandItem()))) {
            controller.setAnimation(EAT);
            controller.setAnimationSpeed(1.0D); // V21 Fix: Reset speed!
            // Debug Log for Eating
            // if (this.tickCount % 40 == 0) {
            // System.out.println("Bear Anim: eat | AnimSpeed: " +
            // controller.getAnimationSpeed() + " | IsEating: "
            // + this.isEating());
            // }
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            controller.setAnimation(SIT);
            controller.setAnimationSpeed(1.0D); // V21 Fix: Reset speed!
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5) {
            if (this.isSprinting()) {
                controller.setAnimation(RUN);
                controller.setAnimationSpeed(2.0D);
            } else {
                controller.setAnimation(WALK);
                // V27 Fix: Use Fixed Speed (0.5D) to prevent "Stomping/Jitter" caused by
                // dynamic velocity
                double animSpeed = 0.5D;
                controller.setAnimationSpeed(animSpeed);

                // V25 Debug Log for Walk (High Precision)
                // if (this.tickCount % 20 == 0) {
                // double velocity = this.getDeltaMovement().horizontalDistance();
                // System.out.println("Bear Anim: walk | Velocity: " + String.format("%.6f",
                // velocity)
                // + " | AnimSpeed: " + String.format("%.6f", animSpeed));
                // }
            }
            return PlayState.CONTINUE;
        } else {
            controller.setAnimation(IDLE);
            controller.setAnimationSpeed(1.0D);
        }
        controller.forceAnimationReset();

        return PlayState.STOP;
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState sniffPredicate(
            final @NotNull software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.isSniffing()) {
            controller.setAnimation(SNIFF);
            return PlayState.CONTINUE;
        }
        controller.forceAnimationReset();
        return PlayState.STOP;
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState attackPredicate(
            final software.bernie.geckolib.animatable.processing.AnimationTest<E> state) {
        software.bernie.geckolib.animatable.processing.AnimationController<E> controller = state.controller();
        if (this.swinging && controller.getAnimationState().equals(AnimationController.State.STOPPED)) {
            controller.forceAnimationReset();

            controller.setAnimationSpeed(1.3F);
            controller.setAnimation(ATTACK);

            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Bear>("controller", 5, this::predicate));
        controllers.add(new AnimationController<Bear>("sniffController", 2, this::sniffPredicate));
        controllers.add(new AnimationController<Bear>("swingController", 2, this::attackPredicate));
    }

    static class BearAttackPlayerNearBabiesGoal extends NearestAttackableTargetGoal<Player> {
        private final Bear bear;

        // 1.21: NearestAttackableTargetGoal uses Selector which is (entity, level) ->
        // boolean
        public BearAttackPlayerNearBabiesGoal(Bear mob, Class<Player> pTargetType, int pRandomInterval,
                boolean pMustSee, boolean pMustReach,
                @org.jetbrains.annotations.Nullable java.util.function.BiPredicate<LivingEntity, ServerLevel> pTargetPredicate) {
            super(mob, pTargetType, pRandomInterval, pMustSee, pMustReach, null);
            this.bear = mob;
        }

        @Override
        public boolean canUse() {
            if (!bear.isBaby() && !bear.isSleeping()) {
                if (super.canUse()) {
                    for (Bear bear : bear.level().getEntitiesOfClass(Bear.class,
                            bear.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (bear.isBaby()) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }

    class BearSleepGoal extends SleepGoal<Bear> {
        public BearSleepGoal(Bear animal) {
            super(animal);
        }

        @Override
        public void start() {
            Bear.this.setSniffing(false);
            super.start();
        }
    }

    static class BearHarvestFoodGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        private final @NotNull Bear bear;

        public BearHarvestFoodGoal(@NotNull Bear mob, double speedModifier, int pSearchRange,
                int pVerticalSearchRange) {
            super(mob, speedModifier, pSearchRange, pVerticalSearchRange);
            this.bear = mob;
        }

        @Override
        public double acceptedDistance() {
            return 3.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof BeehiveBlock) {
                return state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
            } else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
                return state.getValue(SweetBerryBushBlock.AGE) >= 2;
            } else if (state.is(Blocks.CAMPFIRE) && level.getBlockEntity(pos) instanceof CampfireBlockEntity campfire) {
                return campfireIsTempting(campfire);
            }
            return false;
        }

        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            } else if (!this.isReachedTarget() && bear.getRandom().nextFloat() < 0.05F) {
                bear.playSound(NaturalistSoundEvents.BEAR_SNIFF.get(), 1.0F, 1.0F);
                bear.setSniffing(true);
            }
            bear.getLookControl().setLookAt(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, 10.0F,
                    bear.getMaxHeadXRot());
            super.tick();
        }

        protected void onReachedTarget() {
            if (bear.level() instanceof ServerLevel serverLevel
                    && serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockState state = bear.level().getBlockState(blockPos);
                bear.setSniffing(false);
                if (state.getBlock() instanceof BeehiveBlock && state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    this.harvestHoney(state);
                } else if (state.is(Blocks.SWEET_BERRY_BUSH) && state.getValue(SweetBerryBushBlock.AGE) >= 2) {
                    this.pickSweetBerries(state);
                } else if (state.is(Blocks.CAMPFIRE)
                        && bear.level().getBlockEntity(blockPos) instanceof CampfireBlockEntity campfire
                        && campfireIsTempting(campfire)) {
                    this.stealCampfireFood(state, campfire);
                }
            }
        }

        private void stealCampfireFood(BlockState state, @NotNull CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItems().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItems().get(i))) {
                    Containers.dropItemStack(bear.level(), blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                            campfire.getItems().get(i));
                    campfire.getItems().set(i, ItemStack.EMPTY);
                    bear.level().sendBlockUpdated(blockPos, state, state, 3);
                    campfire.setChanged();
                    break;
                }
            }
        }

        private boolean campfireIsTempting(@NotNull CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItems().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItems().get(i))) {
                    return true;
                }
            }
            return false;
        }

        private void harvestHoney(BlockState state) {
            state.setValue(BeehiveBlock.HONEY_LEVEL, 0);
            Block.popResource(bear.level(), blockPos, new ItemStack(Items.HONEYCOMB, 3));
            bear.playSound(SoundEvents.BEEHIVE_SHEAR, 1.0F, 1.0F);
            bear.level().setBlock(blockPos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 2);
            bear.swing(InteractionHand.MAIN_HAND);
        }

        private void pickSweetBerries(@NotNull BlockState state) {
            int age = state.getValue(SweetBerryBushBlock.AGE);
            state.setValue(SweetBerryBushBlock.AGE, 1);
            int berryAmount = 1 + bear.level().random.nextInt(2) + (age == 3 ? 1 : 0);
            Block.popResource(bear.level(), this.blockPos, new ItemStack(Items.SWEET_BERRIES, berryAmount));
            bear.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            bear.level().setBlock(this.blockPos, state.setValue(SweetBerryBushBlock.AGE, 1), 2);
            bear.swing(InteractionHand.MAIN_HAND);
        }

        @Override
        public boolean canUse() {
            return !bear.isBaby() && bear.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return bear.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.ticksWaited = 0;
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos);
            while (bear.level().getBlockState(mutable.below()).isAir()) {
                mutable.move(Direction.DOWN);
            }
            return mutable;
        }
    }

    static class BearFloatGoal extends FloatGoal {
        private final Bear bear;

        public BearFloatGoal(Bear mob) {
            super(mob);
            this.bear = mob;
        }

        @Override
        public boolean canUse() {
            if (!bear.isBaby()) {
                return (bear.level().isWaterAt(bear.blockPosition().below())
                        || bear.level().isWaterAt(bear.blockPosition().above())) && super.canUse();
            } else {
                return super.canUse();
            }
        }
    }

    static class BearTemptGoal extends TemptGoal {
        private final Bear bear;

        public BearTemptGoal(Bear mob, double speedModifier, Ingredient pItems, boolean pCanScare) {
            super(mob, speedModifier, pItems, pCanScare);
            this.bear = mob;
        }

        @Override
        public boolean canUse() {
            return bear.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            bear.setSniffing(true);
        }

        @Override
        public void tick() {
            super.tick();
            if (bear.getRandom().nextFloat() < 0.05F) {
                bear.playSound(NaturalistSoundEvents.BEAR_SNIFF.get(), 1.0F, 1.0F);
            }
        }

        @Override
        public void stop() {
            super.stop();
            bear.setSniffing(false);
        }
    }

    static class BearPickupFoodAndSitGoal extends Goal {
        private int cooldown;
        private final Bear bear;

        public BearPickupFoodAndSitGoal(Bear bear) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
            this.bear = bear;
        }

        @Override
        public boolean canUse() {
            if (this.cooldown > bear.tickCount)
                return false;
            if (bear.isInWater() || bear.isSleeping())
                return false;
            return !bear.canSleep() && !bear.getMainHandItem().isEmpty() && bear.isFood(bear.getMainHandItem());
        }

        @Override
        public boolean canContinueToUse() {
            // V28: Removed isSitting() requirement so it can retry sitting if nudged.
            // Using !isInWater instead of isTouchingWater to match canUse.
            // V31: Check canSleep() to allow Sleep Goal to interrupt eating.
            return !bear.isInWater() && !bear.getMainHandItem().isEmpty() && bear.isFood(bear.getMainHandItem())
                    && !bear.canSleep();
        }

        @Override
        public void tick() {
            // V28: Force sit every tick if not sitting
            if (!bear.isSitting()) {
                bear.getNavigation().stop();
                bear.tryToSit();
            }
        }

        @Override
        public void start() {

            bear.getNavigation().stop();
            bear.tryToSit();
            this.cooldown = 0;
        }

        @Override
        public void stop() {

            // V28 Fix: Do NOT drop the item!
            // Do NOT set massive cooldown!
            // Just stand up.
            bear.setSitting(false);
            this.cooldown = bear.tickCount + 20; // Short cooldown (1s) before retry
        }
    }

    static class BearMeleeAttackGoal extends MeleeAttackGoal {

        public BearMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(mob, speedModifier, pFollowingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        // 1.21: getAttackReachSqr removed from Mob, using super (MeleeAttackGoal)
        // implementation
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + attackTarget.getBbWidth());
        }
    }
}
