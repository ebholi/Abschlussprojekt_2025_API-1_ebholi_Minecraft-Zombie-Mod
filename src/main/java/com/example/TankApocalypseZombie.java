package com.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class TankApocalypseZombie extends BaseApocalypseZombie{
    public TankApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);

        this.xpReward+= 12;
    }

    // Special Attributes, bases itself on BaseApocalypseZombie class.
    public static AttributeSupplier.Builder createAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.ARMOR, 8)
                .add(Attributes.JUMP_STRENGTH, 0.725)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.3)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.STEP_HEIGHT, 1.125)
                .add(Attributes.SAFE_FALL_DISTANCE, 5)
                .add(Attributes.MAX_HEALTH, 50);
    }
}
