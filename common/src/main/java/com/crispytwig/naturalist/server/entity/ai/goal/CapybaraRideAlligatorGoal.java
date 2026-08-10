import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import java.util.EnumSet;
import java.util.List;

public class CapybaraRideAlligatorGoal extends Goal {
    private final Capybara capybara;
    private Alligator targetAlligator;
    private final double speedModifier;

    public CapybaraRideAlligatorGoal(Capybara capybara, double speedModifier) {
        this.capybara = capybara;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.capybara.isVehicle() || this.capybara.isPassenger() || this.capybara.isBaby()) {
            return false;
        }

        if (this.capybara.isTame()) {
            return false;
        }
        
        if (this.capybara.getRandom().nextInt(20) != 0) {
            return false;
        }

        AABB box = this.capybara.getBoundingBox().inflate(10.0D, 4.0D, 10.0D);
        List<Alligator> alligators = this.capybara.level().getEntitiesOfClass(
            Alligator.class, box, 
            gator -> !gator.isVehicle() && gator.isAlive()
        );

        if (alligators.isEmpty()) {
            return false;
        }

        this.targetAlligator = alligators.get(this.capybara.getRandom().nextInt(alligators.size()));
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetAlligator != null 
            && this.targetAlligator.isAlive() 
            && !this.capybara.isPassenger() 
            && !this.targetAlligator.isVehicle();
    }

    @Override
    public void start() {
        this.capybara.getNavigation().moveTo(this.targetAlligator, this.speedModifier);
    }

    @Override
    public void stop() {
        this.targetAlligator = null;
        this.capybara.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetAlligator == null) return;

        this.capybara.getLookControl().setLookAt(this.targetAlligator, 10.0F, (float) this.capybara.getMaxHeadXRot());
        this.capybara.getNavigation().moveTo(this.targetAlligator, this.speedModifier);

        if (this.capybara.distanceToSqr(this.targetAlligator) < 4.0D) {
            this.capybara.startRiding(this.targetAlligator);
        }
    }
}
