package com.y_apocalypse_zombies.client;

import com.y_apocalypse_zombies.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

// Client Side Logic
public class ModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Registering custom Zombies with the Client
        // Vanilla Renderer for Base Zombie
        EntityRendererRegistry.register(
                ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE,
                ZombieRenderer::new
        );

        // GeckoLib Renderer Syntax
        EntityRendererRegistry.register(
                ModEntityTypes.TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE,
                context -> new GeoEntityRenderer<>(context, ModEntityTypes.TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE)
        );

        EntityRendererRegistry.register(
                ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE,
                context -> new GeoEntityRenderer<>(context, ModEntityTypes.RUSHER_APOCALYPSE_ZOMBIE_ENTITY_TYPE)
        );
    }
}