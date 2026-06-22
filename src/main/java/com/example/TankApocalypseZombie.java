package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TankApocalypseZombie extends BaseApocalypseZombie implements GeoEntity {
    public TankApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward = 17;
        this.spawnBaseReinforcements = false;
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    // Special Attributes, bases itself on BaseApocalypseZombie class.
    public static AttributeSupplier.Builder createAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.ARMOR, 8)
                .add(Attributes.JUMP_STRENGTH, 0.725)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.STEP_HEIGHT, 1.125)
                .add(Attributes.SAFE_FALL_DISTANCE, 5)
                .add(Attributes.MAX_HEALTH, 50);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    // Reinforcement Spawning
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (!super.hurtServer(serverLevel, damageSource, f)) {
            return false;
        } else if (!(damageSource.getEntity() instanceof LivingEntity)) {
            return true;
        } else {
            if (serverLevel.random.nextInt(4) == 0) {
                int reinforcementPercent = serverLevel.random.nextInt(100);
                if (reinforcementPercent < 5) {
                    BaseApocalypseZombie reinforcement = ModEntityTypes.TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                            .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                    verifyReinforcement(serverLevel, reinforcement);
                } else if (reinforcementPercent > 5 && reinforcementPercent < 40) {
                    BaseApocalypseZombie reinforcement = ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                            .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                    verifyReinforcement(serverLevel, reinforcement);
                } else {
                    BaseApocalypseZombie reinforcement = ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                            .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                    verifyReinforcement(serverLevel, reinforcement);
                }
            }
        }
        return true;
    }

    private void verifyReinforcement(ServerLevel serverLevel, BaseApocalypseZombie reinforcement) {
        Difficulty difficulty = serverLevel.getDifficulty();
        if (reinforcement != null
                && (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)) {
            if (difficulty == Difficulty.NORMAL) {
                reinforcement.setCanPickUpLoot(false);
                reinforcement.reduceXpReward(3);
            } else {
                reinforcement.reduceXpReward(1);
            }
            // Spawn Position which guarantees that Zombies don't spawn in the floor
            BlockPos spawnPos = serverLevel.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    new BlockPos(
                            (int) this.getX() + serverLevel.random.nextInt(21) - serverLevel.random.nextInt(21),
                            (int) this.getY(),
                            (int) this.getZ() + serverLevel.random.nextInt(21) - serverLevel.random.nextInt(21)
                    )
            );

            reinforcement.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            serverLevel.addFreshEntity(reinforcement);
        }
    }
}
