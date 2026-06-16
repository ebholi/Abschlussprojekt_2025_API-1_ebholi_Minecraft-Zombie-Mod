package com.example.client;

import com.example.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Registering custom Zombies
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