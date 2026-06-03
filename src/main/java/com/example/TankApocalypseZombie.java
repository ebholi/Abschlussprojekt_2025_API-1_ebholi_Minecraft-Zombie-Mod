package com.example;

import net.minecraft.server.level.ServerLevel;
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
                .add(Attributes.MAX_HEALTH, 40);
    }
}
