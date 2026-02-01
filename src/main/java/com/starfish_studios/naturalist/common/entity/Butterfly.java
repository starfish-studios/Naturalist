package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.Catchable;
import com.starfish_studios.naturalist.common.entity.core.NaturalistAnimal;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.FlyingWanderGoal;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.mojang.serialization.Codec;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("null")
public class Butterfly extends NaturalistAnimal implements NaturalistGeoEntity, FlyingAnimal, Catchable {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Butterfly.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_NECTAR = SynchedEntityData.defineId(Butterfly.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POLLINATING = SynchedEntityData.defineId(Butterfly.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Butterfly.class,
            EntityDataSerializers.BOOLEAN);

    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Butterfly(@NotNull EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ButterflyPollinateGoal(this, 1.0D, 8, 4));
        this.goalSelector.addGoal(3, new ButterflyFlyingWanderGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);// setCanPassDoors removed in 1.21
        return navigation;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
        builder.define(HAS_NECTAR, false);
        builder.define(POLLINATING, false);
        builder.define(FROM_HAND, false);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Variant", Codec.INT, getVariant().getId());
        output.store("HasNectar", Codec.BOOL, this.hasNectar());
        output.store("FromHand", Codec.BOOL, this.fromHand());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setVariant(Butterfly.Variant.BY_ID[input.read("Variant", Codec.INT).orElse(0)]);
        this.setHasNectar(input.read("HasNectar", Codec.BOOL).orElse(false));
        input.read("FromHand", Codec.BOOL).ifPresent(this::setFromHand);
    }

    // Implementation for Catchable interface
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        net.minecraft.world.item.component.CustomData.update(
                net.minecraft.core.component.DataComponents.BUCKET_ENTITY_DATA,
                stack, tag -> {
                    tag.putInt("Variant", this.getVariant().getId());
                    tag.putBoolean("HasNectar", this.hasNectar());
                    tag.putBoolean("FromHand", this.fromHand());
                    tag.putInt("Age", this.getAge());
                });
    }

    @Override
    public void loadFromHandTag(@NotNull ItemStack stack) {
        Catchable.loadDefaultDataFromHandTag(this, stack);
        net.minecraft.world.item.component.CustomData customData = stack
                .get(net.minecraft.core.component.DataComponents.BUCKET_ENTITY_DATA);
        if (customData != null) {
            net.minecraft.nbt.CompoundTag tag = customData.copyTag();
            // In 1.21, CompoundTag returns Optional - use orElse to unwrap
            int variantId = tag.getInt("Variant").orElse(0);
            if (variantId >= 0 && variantId < Butterfly.Variant.BY_ID.length) {
                this.setVariant(Butterfly.Variant.BY_ID[variantId]);
            }
            if (tag.contains("Age")) {
                this.setAge(tag.getInt("Age").orElse(0));
            }
            if (tag.contains("FromHand")) {
                this.setFromHand(tag.getBoolean("FromHand").orElse(false));
            }
        }
    }

    @Override
    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    @Override
    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public Butterfly.Variant getVariant() {
        return Butterfly.Variant.BY_ID[this.entityData.get(DATA_VARIANT)];
    }

    public void setVariant(Butterfly.@NotNull Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.getId());
    }

    public boolean hasNectar() {
        return this.entityData.get(HAS_NECTAR);
    }

    void setHasNectar(boolean hasNectar) {
        this.entityData.set(HAS_NECTAR, hasNectar);
    }

    public boolean isPollinating() {
        return this.entityData.get(POLLINATING);
    }

    void setPollinating(boolean pollinating) {
        this.entityData.set(POLLINATING, pollinating);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ItemTags.FLOWERS);
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
        if (reason == EntitySpawnReason.BUCKET) {
            return spawnData;
        }
        RandomSource random = level.getRandom();
        this.setVariant(Variant.getRandom(random));
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @SuppressWarnings("unused")
    public static boolean checkButterflySpawnRules(EntityType<Butterfly> entityType, ServerLevelAccessor levelAccessor,
            EntitySpawnReason spawnType, BlockPos blockPos, RandomSource randomSource) {
        return levelAccessor.getBlockState(blockPos.below()).is(NaturalistTags.BlockTags.BUTTERFLIES_SPAWNABLE_ON)
                && isBrightEnoughToSpawn(levelAccessor, blockPos);
    }

    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    // getStandingEyeHeight removed in 1.21 - eye height is now part of
    // EntityDimensions

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    // dropCustomDeathLoot signature changed in 1.21
    // Override dropped - custom loot should be handled differently

    private ItemStack getVariantDye() {
        return switch (this.getVariant()) {
            case MONARCH -> new ItemStack(Items.ORANGE_DYE);
            case CLOUDED_YELLOW -> new ItemStack(Items.YELLOW_DYE);
            case BLUE_MORPHO -> new ItemStack(Items.BLUE_DYE);
            case GREEN_SWALLOWTAIL -> new ItemStack(Items.GREEN_DYE);
            case JADE_GREEN_SWALLOWTAIL -> new ItemStack(Items.CYAN_DYE);
            case PURPLE_EMPEROR -> new ItemStack(Items.PURPLE_DYE);
            case RED_ADMIRAL -> new ItemStack(Items.RED_DYE);
        };
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.onGround() && this.getNavigation().isDone() && !this.isPollinating()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, Math.max(motion.y, -0.05D), motion.z);
            this.fallDistance = 0.0F;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Butterfly>("controller", 5, this::predicate));
    }

    protected <E extends software.bernie.geckolib.animatable.GeoAnimatable> PlayState predicate(
            final AnimationTest<E> state) {
        AnimationController<E> controller = state.controller();
        if (this.isPollinating()) {
            controller.setAnimation(IDLE);
            controller.setAnimationSpeed(1.0F);
        } else if (!this.onGround() && !this.isPollinating()) {
            controller.setAnimation(FLY);
            double vy = this.getDeltaMovement().y;
            double targetSpeed = Mth.clamp(1.0D + vy * 2.0D, 0.8D, 1.2D);
            double currentSpeed = controller.getAnimationSpeed();
            double lerpedSpeed = Mth.lerp(0.2F, currentSpeed, targetSpeed);
            controller.setAnimationSpeed(lerpedSpeed);
        }
        return PlayState.CONTINUE;
    }

    static class ButterflyPollinateGoal extends MoveToBlockGoal {
        private final Butterfly butterfly;
        private boolean atFlower;
        private int pollinateTime;

        public ButterflyPollinateGoal(@NotNull Butterfly butterfly, double speedModifier, int searchRange,
                int verticalRange) {
            super(butterfly, speedModifier, searchRange, verticalRange);
            this.butterfly = butterfly;
        }

        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(BlockTags.FLOWERS) && !isFlowerOccupied(level, pos);
        }

        @Override
        public double acceptedDistance() {
            return 1.0D;
        }

        @Override
        public void start() {
            super.start();
            this.pollinateTime = 40 + this.butterfly.getRandom().nextInt(40);
            this.atFlower = false;
            this.butterfly.setPollinating(false);
            this.butterfly.setNoGravity(false);
        }

        @Override
        public void stop() {
            super.stop();
            this.butterfly.setPollinating(false);
            this.butterfly.setNoGravity(false);
            this.butterfly.getNavigation().stop();
            this.atFlower = false;
        }

        @Override
        public void tick() {
            Level level = this.butterfly.level();
            BlockPos targetPos = resolveTargetPos(level, this.blockPos);
            BlockState state = level.getBlockState(targetPos);
            if (!state.is(BlockTags.FLOWERS) || isFlowerOccupied(level, targetPos)) {
                this.stop();
                return;
            }

            double shapeTop = state.getShape(level, targetPos).max(net.minecraft.core.Direction.Axis.Y);
            Vec3 target = new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + shapeTop + 0.01D,
                    targetPos.getZ() + 0.5D);

            if (!this.atFlower) {
                this.mob.getNavigation().moveTo(target.x, target.y + 0.2D, target.z, this.speedModifier);
                if (this.mob.position().closerThan(target, this.acceptedDistance() + 0.1D)) {
                    this.atFlower = true;
                    this.butterfly.setPollinating(true);
                    this.butterfly.setNoGravity(true);
                    this.butterfly.getNavigation().stop();
                    this.butterfly.setDeltaMovement(Vec3.ZERO);
                }
            } else {
                this.butterfly.setPos(target.x, target.y, target.z);
                this.butterfly.setDeltaMovement(Vec3.ZERO);
                if (--this.pollinateTime <= 0) {
                    this.butterfly.setHasNectar(true);
                    this.stop();
                }
            }
        }

        private BlockPos resolveTargetPos(Level level, BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof DoublePlantBlock
                    && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                BlockPos above = pos.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.getBlock() instanceof DoublePlantBlock) {
                    return above;
                }
            }
            return pos;
        }

        private boolean isFlowerOccupied(LevelReader level, BlockPos pos) {
            if (!(level instanceof Level world))
                return false;
            AABB box = new AABB(pos).inflate(0.2D);
            return !world.getEntitiesOfClass(Butterfly.class, box,
                    b -> b != this.butterfly && b.isPollinating() && b.blockPosition().equals(pos)).isEmpty();
        }
    }

    static class ButterflyFlyingWanderGoal extends FlyingWanderGoal {
        public ButterflyFlyingWanderGoal(@NotNull Butterfly butterfly) {
            super(butterfly);
        }
    }

    public enum Variant {
        MONARCH(0, "monarch"),
        CLOUDED_YELLOW(1, "clouded_yellow"),
        BLUE_MORPHO(2, "blue_morpho"),
        GREEN_SWALLOWTAIL(3, "green_swallowtail"),
        JADE_GREEN_SWALLOWTAIL(4, "jade_green_swallowtail"),
        PURPLE_EMPEROR(5, "purple_emperor"),
        RED_ADMIRAL(6, "red_admiral");

        public static final Butterfly.Variant[] BY_ID = Arrays.stream(values())
                .sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final String name;

        Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static @NotNull Variant getTypeById(int id) {
            if (id >= 0 && id < BY_ID.length) {
                return BY_ID[id];
            }
            return MONARCH;
        }

        public static @NotNull Variant byId(int id) {
            return getTypeById(id);
        }

        public static Butterfly.@NotNull Variant getRandom(RandomSource random) {
            return Util.getRandom(BY_ID, random);
        }
    }
}
