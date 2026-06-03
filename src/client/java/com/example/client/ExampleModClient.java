package com.example.client;

import com.example.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ZombieRenderer;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EntityRendererRegistry.register(
                ModEntityTypes.BASE_APOCALYPSE_ZOMBIE_ENTITY_TYPE,
                ZombieRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntityTypes.TANK_APOCALYPSE_ZOMBIE_ENTITY_TYPE,
                ZombieRenderer::new
        );
    }
}