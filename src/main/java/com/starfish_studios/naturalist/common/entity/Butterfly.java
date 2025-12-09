package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.FlyingWanderGoal;
import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Comparator;

public class Butterfly extends NaturalistAnimal implements NaturalistGeoEntity, FlyingAnimal, Catchable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final EntityDataAccessor<Boolean> HAS_NECTAR = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private int numCropsGrownSincePollination;

    protected static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.fly");

    @Override
    @NotNull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }


    public Butterfly(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(ItemTags.FLOWERS), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new ButterflyGrowCropGoal(this, 1.0D, 16, 4));
        this.goalSelector.addGoal(5, new ButterflyPollinateGoal(this, 1.0D, 16, 4));
        this.goalSelector.addGoal(6, new FlyingWanderGoal(this));
        this.goalSelector.addGoal(7, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return !level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 1);
        this.entityData.define(FROM_HAND, false);
        this.entityData.define(HAS_NECTAR, false);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant().getId());
        compound.putBoolean("FromHand", this.fromHand());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Butterfly.Variant.BY_ID[compound.getInt("Variant")]);
        this.setFromHand(compound.getBoolean("FromHand"));
    }

    public Butterfly.Variant getVariant() {
        return Butterfly.Variant.BY_ID[this.entityData.get(DATA_VARIANT)];
    }

    public void setVariant(Butterfly.@NotNull Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.getId());
    }


    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public boolean hasNectar() {
        return this.entityData.get(HAS_NECTAR);
    }

    void setHasNectar(boolean hasNectar) {
        this.entityData.set(HAS_NECTAR, hasNectar);
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    @SuppressWarnings("deprecation")
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putInt("Variant", this.getVariant().getId());
        compoundTag.putInt("Age", this.getAge());

    }

    @SuppressWarnings("deprecation")
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        int i = tag.getInt("Variant");
        if (i >= 0 && i < Butterfly.Variant.BY_ID.length) {
            this.setVariant(Butterfly.Variant.BY_ID[i]);
        } else {
            LOGGER.error("Invalid variant: {}", i);
        }

        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }

        if (tag.contains("HuntingCooldown")) {
            this.getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, tag.getLong("HuntingCooldown"));
        }

    }



    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions size) {
        return size.height * 0.5F;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.BUCKET) {
            return spawnData;
        } else {
            RandomSource randomSource = level.getRandom();
            {
                spawnData = new Butterfly.ButterflyGroupData(Variant.getCommonSpawnVariant(randomSource), Variant.getCommonSpawnVariant(randomSource));
            }

            this.setVariant(((Butterfly.ButterflyGroupData)spawnData).getVariant(randomSource));

            return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        }
    }

    @SuppressWarnings("unused")
    public static boolean checkButterflySpawnRules(EntityType<? extends Butterfly> type, ServerLevelAccessor level, MobSpawnType reason, @NotNull BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistTags.BlockTags.BUTTERFLIES_SPAWNABLE_ON);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ItemTags.FLOWERS);
    }

    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(NaturalistItems.BUTTERFLY.get());
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getCropsGrownSincePollination() >= 10) {
            this.resetNumCropsGrownSincePollination();
            this.setHasNectar(false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getY(0.5D));
            }
        }
    }

    private void spawnFluidParticle(@NotNull Level level, double x1, double x2, double z1, double z2, double y) {
        level.addParticle(ParticleTypes.FALLING_NECTAR, Mth.lerp(level.random.nextDouble(), x1, x2), y, Mth.lerp(level.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return NaturalistEntityTypes.CATERPILLAR.get().create(level);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float pMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Butterfly> PlayState predicate(final AnimationState<E> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    public enum Variant {
        CABBAGE_WHITE(0, "cabbage_white", true),
        MONARCH(1, "monarch", true),
        CLOUDED_YELLOW(2, "clouded_yellow", true),
        SWALLOWTAIL(3, "swallowtail", true),
        BLUE_MORPHO(4, "blue_morpho", true);

        public static final Butterfly.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final String name;
        private final boolean common;

        private Variant(int j, String string2, boolean bl) {
            this.id = j;
            this.name = string2;
            this.common = bl;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static @NotNull Variant getTypeById(int id) {
            for (Variant type : values()) {
                if (type.id == id) return type;
            }
            return Variant.MONARCH;
        }

        public static Butterfly.@NotNull Variant getCommonSpawnVariant(RandomSource random) {
            return getSpawnVariant(random);
        }

        private static Butterfly.@NotNull Variant getSpawnVariant(RandomSource random) {
            Butterfly.Variant[] variants = Arrays.stream(BY_ID).filter((variant)
                    -> variant.common).toArray(Variant[]::new);
            return Util.getRandom(variants, random);
        }
    }

    public static class ButterflyGroupData extends AgeableMob.AgeableMobGroupData {
        public final Butterfly.Variant[] types;

        public ButterflyGroupData(Butterfly.Variant... variants) {
            super(false);
            this.types = variants;
        }

        public Butterfly.Variant getVariant(RandomSource random) {
            return this.types[random.nextInt(this.types.length)];
        }
    }

    static class ButterflyPollinateGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        private final Butterfly butterfly;

        public ButterflyPollinateGoal(@NotNull Butterfly mob, double speedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(mob, speedModifier, pSearchRange, pVerticalSearchRange);
            this.butterfly = mob;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(BlockTags.FLOWERS);
        }

        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
        }

        protected void onReachedTarget() {
            BlockState state = butterfly.level().getBlockState(blockPos);
            if (state.is(BlockTags.FLOWERS)) {
                butterfly.setHasNectar(true);
                this.stop();
            }
        }

        @Override
        public boolean canUse() {
            return !butterfly.hasNectar() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !butterfly.hasNectar() && super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.ticksWaited = 0;
        }
    }

    static class ButterflyGrowCropGoal extends MoveToBlockGoal {
        private final Butterfly butterfly;

        public ButterflyGrowCropGoal(Butterfly mob, double speedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(mob, speedModifier, pSearchRange, pVerticalSearchRange);
            this.butterfly = mob;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.getBlock() instanceof CropBlock cropBlock && cropBlock.getAge(state) < cropBlock.getMaxAge();
        }

        public void tick() {
            BlockPos blockpos = this.getMoveToTarget();
            if (!blockpos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    this.mob.getNavigation().moveTo((double)((float)blockpos.getX()) + 0.5D, blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speedModifier);
                }
            } else {
                this.onReachedTarget();
            }

        }

        protected void onReachedTarget() {
            BlockState state = butterfly.level().getBlockState(blockPos);
            if (state.getBlock() instanceof CropBlock cropBlock) {
                cropBlock.growCrops(butterfly.level(), blockPos, state);
                butterfly.incrementNumCropsGrownSincePollination();
                this.stop();
            }
        }

        @Override
        public boolean canUse() {
            return butterfly.hasNectar() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return butterfly.hasNectar() && super.canContinueToUse();
        }
    }
}
