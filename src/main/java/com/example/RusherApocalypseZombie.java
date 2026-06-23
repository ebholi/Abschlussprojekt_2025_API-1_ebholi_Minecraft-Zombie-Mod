package com.example;

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
import software.bernie.geckolib.animatable.GeoAnimatable;
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

    public RusherApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward = 6;
        this.spawnBaseReinforcements = false;
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache((GeoAnimatable) this);

    public static AttributeSupplier.Builder createAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.ARMOR, 1);
    }

    private static final EntityDataAccessor<Boolean> IS_RUSHING =
            SynchedEntityData.defineId(RusherApocalypseZombie.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HIT_RUSH =
            SynchedEntityData.defineId(RusherApocalypseZombie.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_RUSHING, false);
        builder.define(HIT_RUSH, false);
    }

    public void setRushing(boolean value) {
        this.entityData.set(IS_RUSHING, value);
    }

    public boolean isRushing() {
        return this.entityData.get(IS_RUSHING);
    }

    public void setHitRush(boolean value) {
        this.entityData.set(HIT_RUSH, value);
    }

    public boolean isHitRush() {
        return this.entityData.get(HIT_RUSH);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity target) {
        boolean value = super.doHurtTarget(serverLevel, target);
        if (value) {
            this.triggerAnim("attack", "attack");
        }
        if (isRushing()) {
            this.entityData.set(HIT_RUSH, true);
        }
        return value;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("walk", 2, this::walkAnimController));
        controllers.add(new AnimationController<>("rush", 2, this::rushAnimController));
        controllers.add(new AnimationController<>("attack", 0, animTest -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("attack")));
    }

    protected PlayState walkAnimController(AnimationTest<RusherApocalypseZombie> rusher) {
        if (rusher.isMoving()) {
            return rusher.setAndContinue(RawAnimation.begin().thenLoop("walk_loop"));
        }
        rusher.controller().reset();
        return PlayState.STOP;
    }

    protected PlayState rushAnimController(AnimationTest<RusherApocalypseZombie> rusher) {
        if (rusher.isMoving() && this.isRushing()) {
            return rusher.setAndContinue(RawAnimation.begin().thenLoop("rush_loop"));
        }
        rusher.controller().reset();
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
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
        private boolean gotToTarget = false;

        // Checks for a target, which will trigger the Ability.
        @Override
        public boolean canUse() {
            target = RusherApocalypseZombie.this.getTarget();
            return target != null && rushCooldown <= 0 && RusherApocalypseZombie.this.distanceTo(target) >= 8;
        }

        @Override
        public void start() {
            RusherApocalypseZombie.this.setRushing(true);
            gotToTarget = false;
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
            RusherApocalypseZombie.this.setRushing(false);
            // Shorter Cooldown if Zombie didn't get to its Target
            if (gotToTarget) {
                rushCooldown = 400;
            } else {
                rushCooldown = 260;
            }
        }

        // Control what happens every tick (0.05s)
        @Override
        public void tick() {
            target = RusherApocalypseZombie.this.getTarget();
            if (target != null) {
                RusherApocalypseZombie.this.getNavigation().moveTo(target, 0.8);
            }
        }

        @Override
        public boolean canContinueToUse() {
            target = RusherApocalypseZombie.this.getTarget();
            if (target == null || isHitRush() || !isRushing()) {
                gotToTarget = true;
                setHitRush(false);
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

    // Reinforcement Spawning
    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        Difficulty difficulty = serverLevel.getDifficulty();
        if (!super.hurtServer(serverLevel, damageSource, f)) {
            return false;
        } else if (!(damageSource.getEntity() instanceof LivingEntity) || this.isReinforcement()) {
            return true;
        } else {
            setRushing(false);
            if (serverLevel.random.nextInt(12) == 0) {
                BaseApocalypseZombie reinforcement = ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE
                        .create(serverLevel, EntitySpawnReason.REINFORCEMENT);
                if (reinforcement != null
                        && (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)) {
                    reinforcement.setIsReinforcement(true);
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

                    reinforcement.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    serverLevel.addFreshEntity(reinforcement);
                }
            }
            return true;
        }
    }
}
