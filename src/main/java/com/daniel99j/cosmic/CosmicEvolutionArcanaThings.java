package com.daniel99j.cosmic;

import com.daniel99j.cosmic.block.blocks.CosmicForge;
import com.daniel99j.cosmic.block.blocks.CosmicForgeBlockEntity;
import com.mojang.serialization.Lifecycle;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.areaeffects.AreaEffectTracker;
import net.borisshoes.arcananovum.blocks.FractalSpongeBlockEntity;
import net.borisshoes.arcananovum.core.ArcanaBlock;
import net.borisshoes.arcananovum.core.ArcanaItem;
import net.borisshoes.arcananovum.core.Multiblock;
import net.borisshoes.arcananovum.core.MultiblockCore;
import net.borisshoes.arcananovum.core.polymer.NormalPolymerItem;
import net.borisshoes.arcananovum.entities.DragonPhantomEntity;
import net.borisshoes.arcananovum.entities.DragonWizardEntity;
import net.borisshoes.arcananovum.entities.NulConstructEntity;
import net.borisshoes.arcananovum.gui.arcanetome.ArcanaItemCompendiumEntry;
import net.borisshoes.arcananovum.gui.arcanetome.CompendiumEntry;
import net.borisshoes.arcananovum.gui.arcanetome.IngredientCompendiumEntry;
import net.borisshoes.arcananovum.gui.arcanetome.TransmutationRecipesCompendiumEntry;
import net.borisshoes.arcananovum.items.normal.*;
import net.borisshoes.arcananovum.research.ResearchTasks;
import net.borisshoes.arcananovum.utils.MiscUtils;
import net.borisshoes.arcananovum.world.structures.FabricStructurePoolRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.StairShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

import static com.daniel99j.cosmic.CosmicEvolution.MOD_ID;
import static net.borisshoes.arcananovum.ArcanaRegistry.*;

public class CosmicEvolutionArcanaThings {
    public static final ArcanaItem COSMIC_FORGE;
    public static final BlockEntityType<? extends BlockEntity> COSMIC_FORGE_BLOCK_ENTITY;

    public static void initialize() {
    }

    static {
        COSMIC_FORGE = CosmicEvolutionArcanaThings.register(new CosmicForge());
        COSMIC_FORGE_BLOCK_ENTITY = registerBlockEntity(COSMIC_FORGE.getId(), net.minecraft.block.entity.BlockEntityType.Builder.create(FractalSpongeBlockEntity::new, new Block[]{((ArcanaBlock)COSMIC_FORGE).getBlock()}).build());
        //RECOMMENDED_LIST.add(new ArcanaItemCompendiumEntry(COSMIC_FORGE));
    }

    private static ArcanaItem register(ArcanaItem arcanaItem){
        Registry.register(ARCANA_ITEMS, Identifier.of(MOD_ID, arcanaItem.getId()), arcanaItem);
        return arcanaItem;
    }
}
