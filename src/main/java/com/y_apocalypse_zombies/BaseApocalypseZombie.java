/**
 * Author: Oliver Ebhardt
 * Created: 03.06.2026
 * Version: 0.2.2-alpha
 * Description: Base Zombie Class used for other Zombie Types.
 */

package com.y_apocalypse_zombies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

// Base Zombie which the other Zombies base their Stats & Behaviors on
public class BaseApocalypseZombie extends Zombie {
    public BaseApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward = 3;
    }

    // Burning in the Sun is disabled
    @Override
    public boolean isSunSensitive() {
        return false;
    }

    // Disabling Baby variant
    @Override
    public void setBaby(boolean bl) {
        super.setBaby(false);
    }

    // Default Attributes for the Apocalypse Zombies
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
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0);
    }

    // Reinforcement Spawning
    // Prevents Base Reinforcements from spawning when other Zombies which Extend this one are hurt
    protected boolean spawnBaseReinforcements = true;

    // Functions which are activated when taking Damage
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        Difficulty difficulty = serverLevel.getDifficulty();
        if (!super.hurtServer(serverLevel, damageSource, damage)) {
            // Returns nothing if no damage was taken
            return false;
        } else if (!(damageSource.getEntity() instanceof LivingEntity) || this.isReinforcement()) {
            // Cancels Reinforcement Logic if the damage source isn't a living Entity like a Player or Iron Golem
            return true;
        } else {
            // 1 in 10 Chance of spawning a Reinforcement
            if (spawnBaseReinforcements && serverLevel.random.nextInt(10) == 0) {
                BaseApocalypseZombie reinforcement = ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                        .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                if (reinforcement != null
                        && (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)) {
                    // Reinforcements can't spawn more reinforcements
                    reinforcement.setIsReinforcement(true);
                    if (difficulty == Difficulty.NORMAL) {
                        // Reinforcements are slightly weaker
                        reinforcement.setHealth(reinforcement.getHealth() - 4);
                        reinforcement.setCanPickUpLoot(false);
                        reinforcement.xpReward -= 2;
                    } else {
                        reinforcement.setHealth(reinforcement.getHealth() - 2);
                        reinforcement.xpReward -= 1;
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
                    // Spawning the Reinforcement in the World
                    reinforcement.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    serverLevel.addFreshEntity(reinforcement);
                }
            }
            return true;
        }
    }

    // Helper function since xpReward can't be set on Reinforcements outside the Base Zombie
    public void reduceXpReward(int amount) {
        this.xpReward = amount;
    }

    // Custom Spawn Rules to allow Daytime spawning
    public static boolean checkSpawnRules(EntityType<? extends Monster>entityType, ServerLevelAccessor serverLevel, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        int blockLight = serverLevel.getBrightness(LightLayer.BLOCK, pos);
        return blockLight < 11 && serverLevel.getBlockState(pos.below()).isSolid();
    }

    // Reinforcements shouldn't be able to spawn more Reinforcements
    private boolean isReinforcement = false;

    // Funtions to set an Entity as a Reinforcement & get the Value.
    public void setIsReinforcement(boolean value) {
        this.isReinforcement = value;
    }
    public boolean isReinforcement() {
        return this.isReinforcement;
    }
}
