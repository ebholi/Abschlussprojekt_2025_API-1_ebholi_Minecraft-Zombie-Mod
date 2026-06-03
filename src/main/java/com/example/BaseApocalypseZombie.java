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

    // Default Attributes
    public static AttributeSupplier.Builder createCubeAttributes() {
        return BaseApocalypseZombie.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0);
    }
}
