/**
 * Author: Oliver Ebhardt
 * Created: 08.06.2026
 * Version: 0.2.1
 * Description: Rusher Zombie Type with his own Ability implemented using a Goal.
 */

package com.y_apocalypse_zombies;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RusherApocalypseZombie extends BaseApocalypseZombie implements GeoEntity {
    private int rushCooldown = 0; // 400 ticks / 20s of cooldown

    // Entity Constructor
    public RusherApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward = 6;
        this.spawnBaseReinforcements = false;
    }

    // Cache required by GeckoLib
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    // Required function for GeckoLib to function, gives GeckoLib the entity's Cache
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    // Creating the Rusher's Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.ARMOR, 1);
    }

    // Data Fields to be shared / synced between Client & Server
    private static final EntityDataAccessor<Boolean> IS_RUSHING =
            SynchedEntityData.defineId(RusherApocalypseZombie.class, EntityDataSerializers.BOOLEAN);
    // Whether or not the Rusher hit his Target after the Rush
    private static final EntityDataAccessor<Boolean> HIT_RUSH =
            SynchedEntityData.defineId(RusherApocalypseZombie.class, EntityDataSerializers.BOOLEAN);

    // Initial Values of shared fields
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_RUSHING, false);
        builder.define(HIT_RUSH, false);
    }

    // Functions to change the value of a shared field
    public void setRushing(boolean value) {
        this.entityData.set(IS_RUSHING, value);
    }

    public void setHitRush(boolean value) {
        this.entityData.set(HIT_RUSH, value);
    }

    // Getting the value of a shared field
    public boolean isRushing() {
        return this.entityData.get(IS_RUSHING);
    }

    public boolean isHitRush() {
        return this.entityData.get(HIT_RUSH);
    }

    // This function triggers whenever a Rusher hits its target
    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        boolean value = super.doHurtTarget(serverLevel, target);
        // Plays attack animation
        if (value) {
            this.triggerAnim("attack", "attack");
        }
        // Stops the Rush animation when the Rusher hits its target
        if (isRushing() && value) {
            this.entityData.set(HIT_RUSH, true);
        }
        return value;
    }

    // Registering Animations
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("walk", 2, this::walkAnimController));
        controllers.add(new AnimationController<>("rush", 2, this::rushAnimController));
        controllers.add(new AnimationController<>("attack", 0, animTest -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("attack")));
    }

    // Normal walking
    protected PlayState walkAnimController(AnimationTest<RusherApocalypseZombie> rusher) {
        if (rusher.isMoving()) {
            return rusher.setAndContinue(RawAnimation.begin().thenLoop("walk_loop"));
        }
        // Stops the animation
        rusher.controller().reset();
        return PlayState.STOP;
    }

    // Rush animation
    protected PlayState rushAnimController(AnimationTest<RusherApocalypseZombie> rusher) {
        if (rusher.isMoving() && this.isRushing()) {
            return rusher.setAndContinue(RawAnimation.begin().thenLoop("rush_loop"));
        }
        // Stops the animation
        rusher.controller().reset();
        return PlayState.STOP;
    }

    // Adds Rush Ability in form of a new Goal
    @Override
    protected void registerGoals() {
        super.registerGoals();
        // 1 = #1 Priority, overrides most other goals
        this.goalSelector.addGoal(1, new RushGoal());
    }

    // Rush Goal definition
    public class RushGoal extends Goal {
        private static final Identifier RUSH_SPEED = Identifier.fromNamespaceAndPath(YApocalypseZombies.MOD_ID, "rush_speed");
        private static final Identifier RUSH_DAMAGE = Identifier.fromNamespaceAndPath(YApocalypseZombies.MOD_ID, "rush_damage");
        private LivingEntity target;
        private boolean gotToTarget = false;

        // Checks for a target, which will trigger the Ability.
        @Override
        public boolean canUse() {
            target = RusherApocalypseZombie.this.getTarget();
            return target != null && rushCooldown <= 0 && RusherApocalypseZombie.this.distanceTo(target) >= 8;
        }

        // Starts the Ability
        @Override
        public void start() {
            RusherApocalypseZombie.this.setRushing(true);
            gotToTarget = false;
            // Increases Speed
            RusherApocalypseZombie.this.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(
                    new AttributeModifier(
                            RUSH_SPEED,
                            1.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            );
            // Increases Damage
            RusherApocalypseZombie.this.getAttribute(Attributes.ATTACK_DAMAGE).addOrReplacePermanentModifier(
                    new AttributeModifier(
                            RUSH_DAMAGE,
                            1.5,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            );
        }

        // Stops the Rush and resets the Rusher to normal
        @Override
        public void stop() {
            RusherApocalypseZombie.this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(RUSH_SPEED);
            RusherApocalypseZombie.this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(RUSH_DAMAGE);
            RusherApocalypseZombie.this.setRushing(false);
            // Shorter Cooldown if Zombie didn't get to its Target
            if (gotToTarget) {
                rushCooldown = 400;
            } else {
                rushCooldown = 260;
            }
        }

        // Control what happens every tick
        @Override
        public void tick() {
            target = RusherApocalypseZombie.this.getTarget();
            // Navigating towards Target
            if (target != null) {
                RusherApocalypseZombie.this.getNavigation().moveTo(target, 0.8);
            }
        }

        @Override
        public boolean canContinueToUse() {
            target = RusherApocalypseZombie.this.getTarget();
            // Checks if the target was lost, if the Rush was successful or if Rushing is false
            if (target == null || isHitRush() || !isRushing()) {
                gotToTarget = true;
                setHitRush(false);
                return false;
            }
            return true;
        }
    }

    // Runs for every Tick, decreases the Rush Cooldown
    @Override
    public void tick() {
        super.tick();
        if (rushCooldown > 0) {
            rushCooldown--;
        }
    }

    // Functions that happen when the Rusher takes damage
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        Difficulty difficulty = serverLevel.getDifficulty();
        if (!super.hurtServer(serverLevel, damageSource, f)) {
            // Cancels the hurtServer if the Rusher isn't taking damage
            return false;
        } else if (!(damageSource.getEntity() instanceof LivingEntity)) {
            // Simply returns the Damage if the Damage source isn't an Entity (like fall damage).
            return true;
        } else if (this.isReinforcement()) {
            setRushing(false); // Cancels Rush after taking damage
            return true;
        } else {
            setRushing(false);
            // 1 in 12 Chance to spawn a reinforcement
            if (serverLevel.random.nextInt(12) == 0) {
                BaseApocalypseZombie reinforcement = ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                        .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                // Only spawns the reinforcement on Normal or Hard difficulty
                if (reinforcement != null
                        && (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)) {
                    reinforcement.setIsReinforcement(true);
                    // Nerfing reinforcements
                    if (difficulty == Difficulty.NORMAL) {
                        reinforcement.setHealth(reinforcement.getHealth() - 4);
                        reinforcement.setCanPickUpLoot(false);
                        reinforcement.reduceXpReward(3);
                    } else {
                        reinforcement.setHealth(reinforcement.getHealth() - 2);
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
                    // Placing the reinforcement in the game
                    reinforcement.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    serverLevel.addFreshEntity(reinforcement);
                }
            }
            return true;
        }
    }
}
