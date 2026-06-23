package com.y_apocalypse_zombies;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

// Defines the Builders for the Entities from the Mod
// A Builder is used whenever a new Entity is summoned manually or naturally
public class ModEntityTypes {
    // Builder for the Base Zombie
    public static final EntityType<BaseApocalypseZombie> BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "base_apocalypse_zombie",
            EntityType.Builder.<BaseApocalypseZombie>of(BaseApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f) // Hitbox Size
    );

    // Builder for the Tank
    public static final EntityType<TankApocalypseZombie> TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "tank_apocalypse_zombie",
            EntityType.Builder.<TankApocalypseZombie>of(TankApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.5f)
    );

    // Builder for the Tank
    public static final EntityType<RusherApocalypseZombie> RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE = register(
            "rusher_apocalypse_zombie",
            EntityType.Builder.<RusherApocalypseZombie>of(RusherApocalypseZombie::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
    );

    // Creates a ResourceKey and adds it to Minecraft's Registry to allow the Zombies to appear in-game.
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(YApocalypseZombies.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        YApocalypseZombies.LOGGER.info("Registering EntityTypes for " + YApocalypseZombies.MOD_ID);
    }

    // Registering the Entities' Attributes
    public static void registerYZombieAttributes() {
        FabricDefaultAttributeRegistry.register(BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE, BaseApocalypseZombie.createAttributes());
        FabricDefaultAttributeRegistry.register(TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE, TankApocalypseZombie.createAttributes());
        FabricDefaultAttributeRegistry.register(RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE, RusherApocalypseZombie.createAttributes());
    }
}
