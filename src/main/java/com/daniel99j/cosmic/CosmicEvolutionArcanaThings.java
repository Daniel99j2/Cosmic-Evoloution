package com.daniel99j.cosmic;

import com.daniel99j.cosmic.block.blocks.CosmicForge;
import com.daniel99j.cosmic.block.blocks.CosmicForgeBlockEntity;
import com.daniel99j.cosmic.block.blocks.SpaceAltar;
import com.daniel99j.cosmic.block.blocks.SpaceAltarBlockEntity;
import com.mojang.serialization.Lifecycle;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import net.borisshoes.arcananovum.core.ArcanaBlock;
import net.borisshoes.arcananovum.core.ArcanaItem;
import net.borisshoes.arcananovum.gui.arcanetome.ArcanaItemCompendiumEntry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import static com.daniel99j.cosmic.CosmicEvolution.MOD_ID;
import static net.borisshoes.arcananovum.ArcanaRegistry.*;

public class CosmicEvolutionArcanaThings {
    public static ArcanaItem COSMIC_FORGE;
    public static BlockEntityType<? extends BlockEntity> COSMIC_FORGE_BLOCK_ENTITY;
    public static ArcanaItem SPACE_ALTAR;
    public static BlockEntityType<? extends BlockEntity> SPACE_ALTAR_BLOCK_ENTITY;
    public static final Registry<ArcanaItem> CUSTOM_ARCANA_ITEMS = new SimpleRegistry(RegistryKey.ofRegistry(Identifier.of("cosmic", "cosmic_arcana_item")), Lifecycle.stable());

    public static void initialize() {
    }

    static {
        COSMIC_FORGE = (CosmicForge) CosmicEvolutionArcanaThings.register(new CosmicForge());
        COSMIC_FORGE_BLOCK_ENTITY = registerBlockEntity(COSMIC_FORGE.getId(), net.minecraft.block.entity.BlockEntityType.Builder.create(CosmicForgeBlockEntity::new, new Block[]{((ArcanaBlock)COSMIC_FORGE).getBlock()}).build());
        RECOMMENDED_LIST.addLast(new ArcanaItemCompendiumEntry(COSMIC_FORGE));
        SPACE_ALTAR = CosmicEvolutionArcanaThings.register(new SpaceAltar());
        SPACE_ALTAR_BLOCK_ENTITY = registerBlockEntity(SPACE_ALTAR.getId(), net.minecraft.block.entity.BlockEntityType.Builder.create(SpaceAltarBlockEntity::new, new Block[]{((ArcanaBlock)SPACE_ALTAR).getBlock()}).build());
        RECOMMENDED_LIST.addLast(new ArcanaItemCompendiumEntry(SPACE_ALTAR));
    }

    private static ArcanaItem register(ArcanaItem arcanaItem) {
        Registry.register(CUSTOM_ARCANA_ITEMS, Identifier.of(MOD_ID, arcanaItem.getId()), arcanaItem);
        return arcanaItem;
    }

    public static BlockEntityType<? extends BlockEntity> registerBlockEntity(String id, BlockEntityType<? extends BlockEntity> blockEntityType) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("cosmic", id), blockEntityType);
        PolymerBlockUtils.registerBlockEntity(new BlockEntityType[]{blockEntityType});
        return blockEntityType;
    }
}
