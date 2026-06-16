package com.example;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class BaseApocalypseZombie extends Zombie {
    public BaseApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward = 2;
    }

    // Burning in the Sun
    @Override
    public boolean isSunSensitive() {
        return false;
    }

    // Baby variant
    @Override
    public void setBaby(boolean bl) {
        super.setBaby(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.ARMOR, 2)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.STEP_HEIGHT, 0.6)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 1);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (!super.hurtServer(serverLevel, damageSource, f)) {
            return false;
        } else {
            if (serverLevel.random.nextInt(20) == 0) {
                BaseApocalypseZombie reinforcement = ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                        .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                if (reinforcement != null
                        && serverLevel.getDifficulty() == Difficulty.HARD
                        || serverLevel.getDifficulty() == Difficulty.NORMAL) {
                    reinforcement.setHealth(reinforcement.getHealth() - 2);
                    reinforcement.setCanPickUpLoot(false);
                    reinforcement.xpReward -= 2;
                    reinforcement.setPos(
                            this.getX() + serverLevel.random.nextInt(7) - serverLevel.random.nextInt(7),
                            this.getY(),
                            this.getZ() + serverLevel.random.nextInt(7) - serverLevel.random.nextInt(7)
                    );
                    serverLevel.addFreshEntity(reinforcement);
                } else {
                    return false;
                }
            }
            return true;
        }
    }
}
