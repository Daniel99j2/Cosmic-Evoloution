package com.daniel99j.cosmic.block;

import com.daniel99j.cosmic.CosmicEvolution;
//import com.daniel99j.magmanetwork.block.blocks.PlopperBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
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

    public static void register() {
        CosmicEvolution.debug("Loading blocks");
    }

    private static final Map<Registry<?>, List<Pair<Identifier, ?>>> REG_CACHE = new HashMap<>();

    //public static final LightBeaconBlock LIGHT_BEACON = ModBlocks.register("light_beacon", new LightBeaconBlock(FabricBlockSettings.create().mapColor(MapColor.DIAMOND_BLUE).instrument(Instrument.HAT).strength(3.0f).nonOpaque().solidBlock(Blocks::never).pistonBehavior(PistonBehavior.BLOCK)));

    private static Block registerBlock(String name, Block block, Boolean hugo99j) {
        return Registry.register(Registries.BLOCK, Identifier.of(CosmicEvolution.MOD_ID, name), block);
    }

    public static Boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public static <B, T extends B> T register(Registry<B> registry, Identifier id, T obj) {
        REG_CACHE.computeIfAbsent(registry, (r) -> new ArrayList<>()).add(new Pair<>(id, obj));
        return obj;
    }
}
