package com.y_apocalypse_zombies;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

public class ModEntitySpawns {
    // New Record to easily save all the info on each Zombie's spawning stats
    // min & max refer to minGroupSize & maxGroupSize.
    // Weight determines the Spawn Weight compared to other Mobs in the same Category.
    private record SpawnEntry(EntityType<? extends Monster> type, int weight, int min, int max) {}

    private static final List<SpawnEntry> ENTITIES = List.of(
            new SpawnEntry(ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE, 320, 2, 7),
            new SpawnEntry(ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE, 180, 1, 4),
            new SpawnEntry(ModEntityTypes.TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE, 100, 1, 2)
    );

    // Registers Spawn Places
    public static void addSpawns() {
        // Loops through the Record
        for (int i = 0; i < ENTITIES.size(); i++) {
            BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(),
                    MobCategory.MONSTER, ENTITIES.get(i).type,
                    ENTITIES.get(i).weight, ENTITIES.get(i).min, ENTITIES.get(i).max);

            // Registers Spawn Places
            SpawnPlacements.register(
                    ENTITIES.get(i).type,
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    BaseApocalypseZombie::checkSpawnRules
            );
        }
    }
}
