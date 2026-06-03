package com.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;

public class BaseApocalypseZombie extends Zombie {
    public BaseApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    // Burning in the Sun
    @Override
    public boolean isSunSensitive() {
        return false;
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return BaseApocalypseZombie.createAttributes()                                 // Default Values:
                .add(Attributes.ARMOR, 2)                                           // 2
                .add(Attributes.ATTACK_DAMAGE, 3)                                   // 3
                .add(Attributes.ATTACK_KNOCKBACK, 0)                                // 0
                .add(Attributes.FOLLOW_RANGE, 35)                                   // 35
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)                            // 0
                .add(Attributes.MAX_HEALTH, 20)                                     // 20 (40-100 for Leaders)
                .add(Attributes.MOVEMENT_SPEED, 0.23)                               // 0.23
                .add(Attributes.STEP_HEIGHT, 0.6)                                   // 0.6
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0)                       // 0
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, Math.random() * 0.1);  // 0.1
    }
}
