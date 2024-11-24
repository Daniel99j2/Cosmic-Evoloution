package com.daniel99j.cosmic.block;

import com.daniel99j.cosmic.CosmicEvolution;
//import com.daniel99j.magmanetwork.block.blocks.PlopperBlock;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {
//    public static final Block MAGIC_REGENERATING_BLOCK = registerBlock("regenerating_block",
//            new MagicRegeneratingBlock(FabricBlockSettings.copyOf(Blocks.REINFORCED_DEEPSLATE).sounds(BlockSoundGroup.STONE).mapColor(MapColor.CLEAR).strength(-1.0F, 3600000.8F).dropsNothing().noBlockBreakParticles().pistonBehavior(PistonBehavior.BLOCK).noCollision()));
//    public static BlockEntityType<? extends BlockEntity> MAGIC_REGENERATING_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(MagicRegeneratingBlockEntity::new, ModBlocks.MAGIC_REGENERATING_BLOCK).build(null);;

    public static void register() {
        CosmicEvolution.debug("Loading blocks");
//        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CosmicEvolution.MOD_ID, "regenerating_block"), MAGIC_REGENERATING_BLOCK_ENTITY);
//        PolymerBlockUtils.registerBlockEntity(MAGIC_REGENERATING_BLOCK_ENTITY);
    }

    private static final Map<Registry<?>, List<Pair<Identifier, ?>>> REG_CACHE = new HashMap<>();

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(CosmicEvolution.MOD_ID, name), block);
    }

    public static <B, T extends B> T register(Registry<B> registry, Identifier id, T obj) {
        REG_CACHE.computeIfAbsent(registry, (r) -> new ArrayList<>()).add(new Pair<>(id, obj));
        return obj;
    }

    public static Boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }
}
