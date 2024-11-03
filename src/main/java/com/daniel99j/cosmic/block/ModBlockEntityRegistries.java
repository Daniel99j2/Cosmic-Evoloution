package com.daniel99j.cosmic.block;

import com.daniel99j.cosmic.CosmicEvolution;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntityRegistries {

//    public static final BlockEntityType<SleepingBagBlockEntity> SLEEPING_BAG_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(SleepingBagBlockEntity::new,
//            com.daniel99j.magmanetwork.block.ModBlocks.SLEEPING_BAG
//    ).build(null);

    public static void registerBlockEntities() {
//        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CosmicEvolution.MOD_ID, "sleeping_bag"), SLEEPING_BAG_BLOCK_ENTITY);
//        PolymerBlockUtils.registerBlockEntity(SLEEPING_BAG_BLOCK_ENTITY);
    }

}