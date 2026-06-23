/**
 * Author: Oliver Ebhardt
 * Created: 01.06.2026
 * Version: 0.2.1
 * Description: Registers things added to the Mod. For this one, it's all Entities.
 */

package com.y_apocalypse_zombies;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YApocalypseZombies implements ModInitializer {
    public static final String MOD_ID = "y_apocalypse_zombies";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // This code runs as soon as Minecraft is in a mod-load-ready state.
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing y_apocalypse_zombies Entity Types");
        ModEntityTypes.registerModEntityTypes();
        LOGGER.info("Initializing y_apocalypse_zombies Attributes");
        ModEntityTypes.registerYZombieAttributes();
        LOGGER.info("Initializing y_apocalypse_zombies Spawn Spawn Rules");
        ModEntitySpawns.addSpawns();
    }
}