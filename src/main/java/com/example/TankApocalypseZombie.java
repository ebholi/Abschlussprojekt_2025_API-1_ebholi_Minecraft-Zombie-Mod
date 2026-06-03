package com.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class TankApocalypseZombie extends BaseApocalypseZombie{
    public TankApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    // Special Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return TankApocalypseZombie.createAttributes()
                .add(Attributes.ARMOR, 6)
                .add(Attributes.MAX_HEALTH, 40);
    }
}
