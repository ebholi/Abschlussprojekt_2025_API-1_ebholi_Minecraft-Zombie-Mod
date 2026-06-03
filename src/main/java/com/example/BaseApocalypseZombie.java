package com.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class BaseApocalypseZombie extends Zombie {
    public BaseApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes () {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 40.0);
    }
}
