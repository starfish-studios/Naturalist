package com.crispytwig.naturalist.server.entity.mob;

import com.crispytwig.naturalist.Naturalist;
import com.crispytwig.naturalist.server.entity.base.EggLayingAnimal;
import com.crispytwig.naturalist.server.entity.base.HuntingAnimal;
import com.crispytwig.naturalist.server.entity.base.NaturalistAnimal;
import com.crispytwig.naturalist.server.entity.ai.goal.*;
import com.crispytwig.naturalist.registry.NaturalistEntityTypes;
import com.crispytwig.naturalist.registry.NaturalistRegistry;
import com.crispytwig.naturalist.registry.NaturalistSoundEvents;
import com.crispytwig.naturalist.registry.NaturalistTags;
import com.crispytwig.naturalist.server.entity.variant.DataDrivenVariantAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import com.crispytwig.naturalist.server.block.AlligatorEggBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import com.crispytwig.naturalist.server.entity.util.BodyChain;
import com.crispytwig.naturalist.server.entity.util.SmoothAnimationState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public class Alligator extends NaturalistAnimal implements EggLayingAnimal, HuntingAnimal, DataDrivenVariantAnimal {
    //region Data
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.ALLIGATOR_FOOD_ITEMS);

    private static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);

    private static final float MAX_SWIM_TILT = 35.0F;
    private static final float ROLL_PER_YAW = 4.0F;
    private static final float MAX_ROLL = 25.0F;

    int layEggCounter;
    private int huntingCooldown;

    private float xBodyRot;
    private float xBodyRotO;
    private float zBodyRot;
    private float zBodyRotO;
    private final BodyChain chain = new BodyChain(1.0F,
            new float[]{0.35F, 0.16F, 0.12F},
            new float[]{0.24F, 0.12F, 0.1F},
            0.0F, 0.0F, 1.0F);
    private final BodyChain babyChain = new BodyChain(1.0F,
            new float[]{0.55F, 0.28F, 0.22F},
            new float[]{0.4F, 0.22F, 0.18F},
            0.0F, 0.0F, 1.0F);

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState swimAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState biteAnimationState = SmoothAnimationState.instant();

    public Alligator(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.4F, 1.0F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0f);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.60)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, this.getDefaultVariant().location().toString());
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public ResourceLocation getFallbackVariantTexture() {
        return Naturalist.location("textures/entity/alligator/alligator.png");
    }

    @Override
    public String getVariantString() {
        return this.entityData.get(DATA_VARIANT);
    }

    @Override
    public void setVariantString(String location) {
        this.entityData.set(DATA_VARIANT, location);
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
        return NaturalistRegistry.ALLIGATOR_EGG.get();
    }

    @Override
    public BlockState createEggBlockState(int eggCount) {
        return this.getEggBlock().defaultBlockState().setValue(AlligatorEggBlock.EGGS, eggCount);
    }

    @Override
    public @NotNull TagKey<Block> getEggLayableBlockTag() {
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

    public boolean isDefensive() {
        return this.hasEgg() || this.isLayingEgg();
    }

    @Override
    public int getHuntingCooldown() {
        return this.huntingCooldown;
    }

    @Override
    public void setHuntingCooldown(int ticks) {
        this.huntingCooldown = ticks;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.saveVariant(compound);
        compound.putBoolean("HasEgg", this.hasEgg());
        this.saveHuntingCooldown(compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.loadVariant(compound);
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.loadHuntingCooldown(compound);
    }
    //endregion

    //region Spawning
    public static boolean checkAlligatorSpawnRules(EntityType<? extends Alligator> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.DIRT) && level.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Alligator baby = NaturalistEntityTypes.ALLIGATOR.get().create(serverLevel);
        if (baby != null) {
            baby.setVariantString(this.getOffspringVariantId(ageableMob, this.random));
        }
        return baby;
    }

    @Override
    public @NonNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.selectVariantForSpawn(level);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    //endregion

    //region Behavior
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new CloseMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2D));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new BabySniffFlowersGoal(this, 1.0D, 16, 4, SoundEvents.FOX_SNIFF));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> !this.isBaby() && (entity.isInWater() || this.isDefensive() || !this.level().isDay())));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> {
            if(entity instanceof Alligator) return false;
            boolean isEntityNearAlligatorEggs = false;
            for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().offset(-2, -2, -2), entity.blockPosition().offset(2, 2, 2))) {
                if (level().getBlockState(pos).is(NaturalistRegistry.ALLIGATOR_EGG.get())) {
                    isEntityNearAlligatorEggs = true;
                    break;
                }
            }
            return !this.isBaby() && isEntityNearAlligatorEggs;
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> !this.isBaby() && this.canHunt() && entity.getType().is(NaturalistTags.EntityTypes.ALLIGATOR_HOSTILES)));
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.0025, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return this.isInWater() && level.getFluidState(pos).is(FluidTags.WATER) ? 10.0F : super.getWalkTargetValue(pos, level);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (this.isBaby()) {
            super.knockback(strength / Math.max(1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.01), x, z);
        } else {
            super.knockback(strength, x, z);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel level, @NotNull LivingEntity killed) {
        boolean result = super.killedEntity(level, killed);
        if (result) {
            this.startHuntingCooldown();
        }
        return result;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.tickHuntingCooldown();
            NaturalistAnimal.leaveWater(this);
        }

        BlockPos pos = this.blockPosition();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0 && this.level().getBlockState(pos.below()).is(this.getEggLayableBlockTag())) {
            this.level().levelEvent(2001, pos, Block.getId(this.level().getBlockState(pos.below())));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_AMBIENT_BABY.get() : NaturalistSoundEvents.GATOR_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_HURT_BABY.get() : NaturalistSoundEvents.GATOR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? NaturalistSoundEvents.GATOR_DEATH_BABY.get() : NaturalistSoundEvents.GATOR_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return NaturalistAnimal.defaultVoicePitch(this.random);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.hasPassenger(entity -> entity instanceof Capybara)) {
            return false;
            }
        return super.canAttack(target);
    }
    
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof Capybara || super.canAddPassenger(passenger);
    }
    
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof Capybara && !this.level().isClientSide) {
            this.setTarget(null);
            this.setLastHurtByMob(null);
            this.getNavigation().stop();
       }
    }
    //endregion

    //region Animation
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.tickClientVisuals();
            this.setupAnimationStates();
        }
    }

    private void tickClientVisuals() {
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;

        Vec3 movement = new Vec3(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        boolean grounded = this.onGround() || !this.isInWater();
        float targetPitch = 0.0F;
        if (!grounded && movement.horizontalDistanceSqr() > 1.0E-7D) {
            targetPitch = Mth.clamp(-((float) (Mth.atan2(movement.y, movement.horizontalDistance()) * Mth.RAD_TO_DEG)), -MAX_SWIM_TILT, MAX_SWIM_TILT);
        }
        this.xBodyRot += (targetPitch - this.xBodyRot) * (grounded ? 0.25F : 0.07F);

        float yawStep = Mth.degreesDifference(this.yBodyRotO, this.yBodyRot);
        float targetRoll = this.isInWater() ? Mth.clamp(-yawStep * ROLL_PER_YAW, -MAX_ROLL, MAX_ROLL) : 0.0F;
        this.zBodyRot += (targetRoll - this.zBodyRot) * 0.1F;

        this.activeChain().tick(this.yBodyRot, this.xBodyRot, targetPitch);
    }

    private BodyChain activeChain() {
        return this.isBaby() ? this.babyChain : this.chain;
    }

    public float getXBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.xBodyRotO, this.xBodyRot);
    }

    public float getZBodyRot(float partialTick) {
        return Mth.lerp(partialTick, this.zBodyRotO, this.zBodyRot);
    }

    public float getSegmentPitchOffset(int index, float partialTick) {
        return this.activeChain().getSegmentPitchOffset(index, partialTick, this.getXBodyRot(partialTick));
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        return this.activeChain().getSegmentYawOffset(index, partialTick);
    }

    private void setupAnimationStates() {
        boolean moving = NaturalistAnimal.isVisiblyMoving(this);
        boolean inWater = this.isInWater();
        this.biteAnimationState.animateWhen(this.swinging, this.tickCount);
        this.swimAnimationState.animateWhen(inWater, this.tickCount);
        this.walkAnimationState.animateWhen(moving && !inWater, this.tickCount);
        this.idleAnimationState.animateWhen(!moving && !inWater, this.tickCount);
    }
    //endregion
}
