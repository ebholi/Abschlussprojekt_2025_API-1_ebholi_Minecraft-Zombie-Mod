package com.example;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static final EntityType<BaseApocalypseZombie> BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "base_apocalypse_zombie",
            EntityType.Builder.<BaseApocalypseZombie>of(BaseApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f) // Default: 0.6f | 1.95f
    );

    public static final EntityType<TankApocalypseZombie> TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "tank_apocalypse_zombie",
            EntityType.Builder.<TankApocalypseZombie>of(TankApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.5f)
    );

    public static final EntityType<RusherApocalypseZombie> RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "rusher_apocalypse_zombie",
            EntityType.Builder.<RusherApocalypseZombie>of(RusherApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ExampleMod.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        ExampleMod.LOGGER.info("Registering EntityTypes for " + ExampleMod.MOD_ID);
    }

    public static void registerYZombieAttributes() {
        FabricDefaultAttributeRegistry.register(BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE, BaseApocalypseZombie.createAttributes());
        FabricDefaultAttributeRegistry.register(TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE, TankApocalypseZombie.createAttributes());
        FabricDefaultAttributeRegistry.register(RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE, RusherApocalypseZombie.createAttributes());
    }
}
