package com.starfish_studios.naturalist.common.entity.core;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;

public abstract class TamableNaturalistAnimal extends NaturalistAnimal implements OwnableEntity {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID;
    private boolean orderedToSit;

    protected TamableNaturalistAnimal(EntityType<? extends TamableNaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.reassessTameGoals();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }

        compound.putBoolean("Sitting", this.orderedToSit);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uUID;
        if (compound.hasUUID("Owner")) {
            uUID = compound.getUUID("Owner");
        } else {
            String string = compound.getString("Owner");
            uUID = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
        }

        if (uUID != null) {
            try {
                this.setOwnerUUID(uUID);
                this.setTame(true);
            } catch (Throwable var4) {
                this.setTame(false);
            }
        }

        this.orderedToSit = compound.getBoolean("Sitting");
        this.setInSittingPose(this.orderedToSit);
    }

    public boolean canBeLeashed(Player player) {
        return !this.isLeashed();
    }

    protected void spawnTamingParticles(boolean tamed) {
        ParticleOptions particleOptions = ParticleTypes.HEART;
        if (!tamed) {
            particleOptions = ParticleTypes.SMOKE;
        }

        for(int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particleOptions, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
        }

    }

    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.spawnTamingParticles(true);
        } else if (id == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(id);
        }

    }

    public boolean isTame() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean tamed) {
        byte b = (Byte)this.entityData.get(DATA_FLAGS_ID);
        if (tamed) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b & -5));
        }

        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
    }

    public boolean isInSittingPose() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean sitting) {
        byte b = (Byte)this.entityData.get(DATA_FLAGS_ID);
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(b & -2));
        }

    }

    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)((Optional)this.entityData.get(DATA_OWNERUUID_ID)).orElse((Object)null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uuid));
    }

    public void tame(Player player) {
        this.setTame(true);
        this.setOwnerUUID(player.getUUID());
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)player, this);
        }

    }

    public boolean canAttack(LivingEntity target) {
        return this.isOwnedBy(target) ? false : super.canAttack(target);
    }

    public boolean isOwnedBy(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return true;
    }

    public PlayerTeam getTeam() {
        if (this.isTame()) {
            LivingEntity livingEntity = this.getOwner();
            if (livingEntity != null) {
                return livingEntity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity entity) {
        if (this.isTame()) {
            LivingEntity livingEntity = this.getOwner();
            if (entity == livingEntity) {
                return true;
            }

            if (livingEntity != null) {
                return livingEntity.isAlliedTo(entity);
            }
        }

        return super.isAlliedTo(entity);
    }

    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }

        super.die(damageSource);
    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean orderedToSit) {
        this.orderedToSit = orderedToSit;
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(TamableNaturalistAnimal.class, EntityDataSerializers.BYTE);
        DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableNaturalistAnimal.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}
