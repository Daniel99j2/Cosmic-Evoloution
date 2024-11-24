package com.daniel99j.cosmic.misc;

import com.daniel99j.cosmic.CosmicEvolution;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class ModChunkComponents implements ChunkComponentInitializer {
//    public static final ComponentKey<RegeneratingBlockComponent> REGENERATING_BLOCK = ComponentRegistry.getOrCreate(Identifier.of(CosmicEvolution.MOD_ID, "regenerating_blocks"), RegeneratingBlockComponent.class);

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
//        registry.register(REGENERATING_BLOCK, RegeneratingBlockComponent::new);
    }
}
