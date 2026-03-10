package com.starfish_studios.naturalist.common.entity.core.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class SmartBodyHelper extends BodyRotationControl {
    private static final int HISTORY_SIZE = 10;
    private static final double MOVE_THRESHOLD = 2.5e-7;

    public float bodyLagMoving;
    public float headLag;
    public float bodyLagStill;
    public float bodyMax;
    public float headMax;

    private final double[] histPosX = new double[HISTORY_SIZE];
    private final double[] histPosZ = new double[HISTORY_SIZE];
    protected final Mob entity;

    public SmartBodyHelper(Mob entity) {
        super(entity);
        this.entity = entity;
        this.bodyLagMoving = 0.3F;
        this.headLag = 0.2F;
        this.bodyLagStill = 0.05F;
        this.bodyMax = 45F;
        this.headMax = 22.5F;
    }

    @Override
    public void clientTick() {
        for (int i = HISTORY_SIZE - 1; i > 0; i--) {
            histPosX[i] = histPosX[i - 1];
            histPosZ[i] = histPosZ[i - 1];
        }
        histPosX[0] = entity.getX();
        histPosZ[0] = entity.getZ();

        double dx = avgDelta(histPosX);
        double dz = avgDelta(histPosZ);
        double distSq = dx * dx + dz * dz;

        if (entity.getTarget() != null) {
            double tx = entity.getTarget().getX() - entity.getX();
            double tz = entity.getTarget().getZ() - entity.getZ();
            float targetAngle = (float)(Mth.atan2(tz, tx) * (180F / Math.PI)) - 90F;
            entity.yBodyRot = approachAngle(entity.yBodyRot, targetAngle, bodyLagMoving, bodyMax);
            entity.yHeadRot = approachAngle(entity.yHeadRot, targetAngle, headLag, headMax);
            clampHeadBodyDifference();
        } else if (distSq > MOVE_THRESHOLD) {
            float moveAngle = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
            entity.yBodyRot = approachAngle(entity.yBodyRot, moveAngle, bodyLagMoving, bodyMax);
            entity.yHeadRot = approachAngle(entity.yHeadRot, entity.yBodyRot, headLag, headMax);
            clampHeadBodyDifference();
        } else {
            entity.yBodyRot = approachAngle(entity.yBodyRot, entity.yHeadRot, bodyLagStill, bodyMax);
            clampHeadBodyDifference();
        }
    }

    private double avgDelta(double[] arr) {
        return mean(arr, 0) - mean(arr, HISTORY_SIZE / 2);
    }

    private double mean(double[] arr, int start) {
        double s = 0;
        int half = HISTORY_SIZE / 2;
        for (int i = 0; i < half; i++) {
            s += arr[start + i];
        }
        return s / half;
    }

    private float approachAngle(float current, float target, float factor, float maxDelta) {
        float d = Mth.wrapDegrees(target - current);
        if (d < -maxDelta) {
            d = -maxDelta;
        } else if (d > maxDelta) {
            d = maxDelta;
        }
        return current + d * factor;
    }

    private void clampHeadBodyDifference() {
        float diff = Mth.wrapDegrees(entity.yHeadRot - entity.yBodyRot);
        float clamped = Mth.clamp(diff, -headMax, headMax);
        entity.yHeadRot = entity.yBodyRot + clamped;
    }
}
