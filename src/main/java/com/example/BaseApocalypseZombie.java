package com.example;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class BaseApocalypseZombie extends Zombie {
    public BaseApocalypseZombie(Level world) {
        this(ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE, world);
    }

    public BaseApocalypseZombie(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return BaseApocalypseZombie.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0);
    }
}
