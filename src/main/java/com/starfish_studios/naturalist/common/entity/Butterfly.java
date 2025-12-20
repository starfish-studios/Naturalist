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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("null")
public class Butterfly extends NaturalistAnimal implements NaturalistGeoEntity, FlyingAnimal, Catchable {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_NECTAR = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POLLINATING = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

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
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    @NotNull
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(HAS_NECTAR, false);
        this.entityData.define(POLLINATING, false);
        this.entityData.define(FROM_HAND, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant().getId());
        compound.putBoolean("HasNectar", this.hasNectar());
        compound.putBoolean("FromHand", this.fromHand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Butterfly.Variant.BY_ID[compound.getInt("Variant")]);
        this.setHasNectar(compound.getBoolean("HasNectar"));
        if (compound.contains("FromHand")) {
            this.setFromHand(compound.getBoolean("FromHand"));
        }
    }

    @SuppressWarnings("deprecation")
    public void saveToHandTag(@NotNull ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Variant", this.getVariant().getId());
        tag.putInt("Age", this.getAge());
        tag.putBoolean("FromHand", true);
    }

    @SuppressWarnings("deprecation")
    public void loadFromHandTag(@NotNull CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        int variantId = tag.getInt("Variant");
        if (variantId >= 0 && variantId < Butterfly.Variant.BY_ID.length) {
            this.setVariant(Butterfly.Variant.BY_ID[variantId]);
        }
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
        if (tag.contains("FromHand")) {
            this.setFromHand(tag.getBoolean("FromHand"));
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
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.BUCKET) {
            return spawnData;
        }
        RandomSource random = level.getRandom();
        this.setVariant(Variant.getRandom(random));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @SuppressWarnings("unused")
    public static boolean checkButterflySpawnRules(EntityType<? extends Butterfly> type, ServerLevelAccessor level, MobSpawnType reason, @NotNull BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(NaturalistTags.BlockTags.BUTTERFLIES_SPAWNABLE_ON);
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
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull net.minecraft.world.damagesource.DamageSource source, int lootingMultiplier, boolean hitByPlayer) {
        super.dropCustomDeathLoot(source, lootingMultiplier, hitByPlayer);
        this.spawnAtLocation(getVariantDye());
    }

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
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    protected <E extends Butterfly> PlayState predicate(final AnimationState<E> event) {
        if (this.isPollinating()) {
            event.getController().setAnimation(IDLE);
            event.getController().setAnimationSpeed(1.0F);
        } else if (!this.onGround() && !this.isPollinating()) {
            event.getController().setAnimation(FLY);
            double vy = this.getDeltaMovement().y;
            double targetSpeed = Mth.clamp(1.0D + vy * 2.0D, 0.8D, 1.2D);
            double currentSpeed = event.getController().getAnimationSpeed();
            double lerpedSpeed = Mth.lerp(0.2F, currentSpeed, targetSpeed);
            event.getController().setAnimationSpeed(lerpedSpeed);
        }
        return PlayState.CONTINUE;
    }

    static class ButterflyPollinateGoal extends MoveToBlockGoal {
        private final Butterfly butterfly;
        private boolean atFlower;
        private int pollinateTime;

        public ButterflyPollinateGoal(@NotNull Butterfly butterfly, double speedModifier, int searchRange, int verticalRange) {
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
            Vec3 target = new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + shapeTop + 0.01D, targetPos.getZ() + 0.5D);

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
            if (state.getBlock() instanceof DoublePlantBlock && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                BlockPos above = pos.above();
                BlockState aboveState = level.getBlockState(above);
                if (aboveState.getBlock() instanceof DoublePlantBlock) {
                    return above;
                }
            }
            return pos;
        }

        private boolean isFlowerOccupied(LevelReader level, BlockPos pos) {
            if (!(level instanceof Level world)) return false;
            AABB box = new AABB(pos).inflate(0.2D);
            return !world.getEntitiesOfClass(Butterfly.class, box, b -> b != this.butterfly && b.isPollinating() && b.blockPosition().equals(pos)).isEmpty();
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

        public static final Butterfly.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
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
