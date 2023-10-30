package com.starfish_studios.naturalist.common.entity.core;

import com.starfish_studios.naturalist.common.entity.Whale;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WhalePart extends Entity {
    public final Whale parentMob;
    public final String name;
    private final EntityDimensions size;

    public WhalePart(Whale whale, String string, float f, float g) {
        super(whale.getType(), whale.level());
        this.size = EntityDimensions.scalable(f, g);
        this.refreshDimensions();
        this.parentMob = whale;
        this.name = string;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public boolean isPickable() {
        return true;
    }

    @Nullable
    public ItemStack getPickResult() {
        return this.parentMob.getPickResult();
    }

    public boolean hurt(DamageSource damageSource, float f) {
        return !this.isInvulnerableTo(damageSource) && this.parentMob.hurt(damageSource, f);
    }

    public boolean is(Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    public boolean shouldBeSaved() {
        return false;
    }
}