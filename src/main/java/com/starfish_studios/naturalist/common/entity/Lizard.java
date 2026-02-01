package com.starfish_studios.naturalist.common.entity;

import net.minecraft.world.item.Items;

import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.starfish_studios.naturalist.common.entity.core.NaturalistGeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Lizard extends TamableAnimal implements NaturalistGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Lizard.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_TAIL = SynchedEntityData.defineId(Lizard.class,
            EntityDataSerializers.BOOLEAN);
    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.SPIDER_EYE); // TODO: Fix
                                                                                        // Ingredient.of(NaturalistTags.ItemTags.LIZARD_TEMPT_ITEMS);
    private LizardAvoidEntityGoal<Player> avoidPlayersGoal;
    private int tailRegrowCooldown = 0;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.lizard.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.lizard.walk");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.sf_nba.lizard.sit");

    public Lizard(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.TEMPT_RANGE, 10.0D);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return null;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LizardTemptGoal(this, 0.6, TEMPT_INGREDIENT, true));
        // 1.21: FollowOwnerGoal constructor is (animal, speed, startDistance,
        // stopDistance)
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0f, 2.0f));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    // @Override
    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new LizardAvoidEntityGoal<>(this, Player.class, 16.0f, 0.8, 1.33);
        }
        this.goalSelector.removeGoal(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.addGoal(4, this.avoidPlayersGoal);
        }
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.store("Variant", Codec.INT, this.getVariant());
        valueOutput.store("HasTail", Codec.BOOL, this.hasTail());
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setVariant(valueInput.read("Variant", Codec.INT).orElse(0));
        this.setHasTail(valueInput.read("HasTail", Codec.BOOL).orElse(true));
    }

    // TODO: Implement tail dropping logic in a way that doesn't override final hurt
    // method or check why it's final
    // @Override
    // public void hurt(@NotNull DamageSource source, float amount) { ... }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level().isClientSide()) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || TEMPT_INGREDIENT.test(stack) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (TEMPT_INGREDIENT.test(stack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(5);
                if (!this.hasTail() && this.getHealth() >= this.getMaxHealth()) {
                    this.setHasTail(true);
                    this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player))
                return interactionResult;
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        if (!TEMPT_INGREDIENT.test(stack))
            return super.mobInteract(player, hand);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte) 7);
            return InteractionResult.SUCCESS;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 3);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    public boolean hasTail() {
        return this.entityData.get(HAS_TAIL);
    }

    public void setHasTail(boolean hasTail) {
        this.entityData.set(HAS_TAIL, hasTail);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT_ID, 0);
        builder.define(HAS_TAIL, true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.hasTail() && !this.level().isClientSide()) {
            if (this.tailRegrowCooldown > 0) {
                --this.tailRegrowCooldown;
            } else {
                this.playSound(SoundEvents.SLIME_SQUISH, 1.0f, 1.0f);
                this.setHasTail(true);
            }
        }
    }

    // @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason reason,
            @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(Biomes.SAVANNA)) {
            this.setVariant(3);
        } else if (holder.is(BiomeTags.IS_JUNGLE)) {
            this.setVariant(0);
        } else if (holder.is(Biomes.DESERT)) {
            this.setVariant(2);
        } else {
            this.setVariant(1);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // GeckoLib 5: predicate uses explicit Lizard type instead of generic E
    private PlayState predicate(final AnimationTest<Lizard> state) {
        if (this.isInSittingPose()) {
            state.controller().setAnimation(SIT);
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            state.controller().setAnimation(WALK);
            state.controller().setAnimationSpeed(2.0D);
            return PlayState.CONTINUE;
        }
        state.controller().forceAnimationReset();

        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        // GeckoLib 5: constructor without "this" parameter
        controllers.add(new AnimationController<Lizard>("controller", 0, this::predicate));
    }

    static class LizardTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Lizard lizard;

        public LizardTemptGoal(Lizard lizard, double speedModifier, Ingredient ingredient, boolean canScare) {
            super(lizard, speedModifier, ingredient, canScare);
            this.lizard = lizard;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.player;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canScare() {
            if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player)) {
                return false;
            }
            return super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.lizard.isTame();
        }
    }

    static class LizardAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final @NotNull Lizard lizard;

        public LizardAvoidEntityGoal(@NotNull Lizard lizard, Class<T> class_, float f, double d, double e) {
            super(lizard, class_, f, d, e, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.lizard = lizard;
        }

        @Override
        public boolean canUse() {
            return !this.lizard.isTame() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.lizard.isTame() && super.canContinueToUse();
        }
    }
}
