package com.example;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class RusherApocalypseZombie extends BaseApocalypseZombie {
    private int rushCooldown = 0; // 400 ticks / 20s of cooldown

    public RusherApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward += 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.05)
                .add(Attributes.ARMOR, 1);
    }

    // Adds custom Rush Goal
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RushGoal());
    }

    // Actual Rush Goal Logic
    public class RushGoal extends Goal {
        private static final Identifier RUSH_SPEED = Identifier.fromNamespaceAndPath(ExampleMod.MOD_ID, "rush_speed");
        private LivingEntity target;

        // Checks for a target, which will trigger the Ability.
        @Override
        public boolean canUse() {
            target = RusherApocalypseZombie.this.getTarget();
            return target != null && rushCooldown <= 0;
        }

        @Override
        public void start() {
            RusherApocalypseZombie.this.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(
                    new AttributeModifier(
                            RUSH_SPEED,
                            1.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            );
        }

        @Override
        public void stop() {
            RusherApocalypseZombie.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(RUSH_SPEED);
            rushCooldown = 400;
        }

        // Control what happens every tick (0.05s)
        @Override
        public void tick() {
            target = RusherApocalypseZombie.this.getTarget();
            if (target != null) {
                RusherApocalypseZombie.this.getNavigation().moveTo(target, 1);
            }
        }

        @Override
        public boolean canContinueToUse() {
            target = RusherApocalypseZombie.this.getTarget();
            if (target == null || RusherApocalypseZombie.this.distanceTo(target) < 4) {
                return false;
            }
            return true;
        }
    }

    // Runs for every Tick, even when Ability is inactive
    @Override
    public void tick() {
        super.tick();
        if (rushCooldown > 0) {
            rushCooldown--;
        }
    }
}
