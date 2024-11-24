package com.daniel99j.cosmic.block.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.CosmicEvolutionArcanaThings;
import com.daniel99j.cosmic.block.ModBlocks;
import com.daniel99j.cosmic.item.ModItems;
import eu.pb4.polymer.virtualentity.api.BlockWithMovingElementHolder;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.core.ArcanaBlock;
import net.borisshoes.arcananovum.core.ArcanaItem;
import net.borisshoes.arcananovum.core.Multiblock;
import net.borisshoes.arcananovum.core.MultiblockCore;
import net.borisshoes.arcananovum.core.polymer.ArcanaPolymerBlockEntity;
import net.borisshoes.arcananovum.core.polymer.ArcanaPolymerBlockItem;
import net.borisshoes.arcananovum.gui.arcanetome.TomeGui;
import net.borisshoes.arcananovum.gui.arcanetome.TomeGui.TomeFilter;
import net.borisshoes.arcananovum.items.normal.GraphicItems;
import net.borisshoes.arcananovum.items.normal.GraphicalItem;
import net.borisshoes.arcananovum.recipes.arcana.ArcanaRecipe;
import net.borisshoes.arcananovum.recipes.arcana.ExplainIngredient;
import net.borisshoes.arcananovum.recipes.arcana.ExplainRecipe;
import net.borisshoes.arcananovum.research.ResearchTasks;
import net.borisshoes.arcananovum.utils.ArcanaRarity;
import net.borisshoes.arcananovum.utils.TextUtils;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CosmicForge extends ArcanaBlock {
    public static final String ID = "cosmic_forge";

    public CosmicForge() {
        this.id = "cosmic_forge";
        this.name = "Cosmic Forge";
        this.rarity = ArcanaRarity.EMPOWERED;
        this.categories = new TomeGui.TomeFilter[]{TomeFilter.EMPOWERED, TomeFilter.BLOCKS, TomeFilter.FORGE};
        this.itemVersion = 0;
        this.vanillaItem = Items.SHROOMLIGHT;
        this.block = ModBlocks.registerBlock("cosmic_forge", new CosmicForge.CosmicForgeBlock(AbstractBlock.Settings.create().strength(2.5F, 1200.0F).sounds(BlockSoundGroup.WOOD)));
        this.item = ModItems.register("cosmic_forge", new CosmicForge.CosmicForgeItem(new Item.Settings().maxCount(1).fireproof().component(DataComponentTypes.ITEM_NAME, TextUtils.withColor(Text.literal("Cosmic Forge").formatted(Formatting.BOLD), 47226)).component(DataComponentTypes.LORE, new LoreComponent(CosmicForge.getItemLoreStatic((ItemStack)null))).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));
        ItemStack stack = new ItemStack(this.item);
        this.initializeArcanaTag(stack);
        stack.setCount(this.item.getMaxCount());
        this.setPrefStack(stack);
    }

    public List<Text> getItemLore(@Nullable ItemStack itemStack) {
        return getItemLoreStatic(itemStack);
    }


    public static List<Text> getItemLoreStatic(@Nullable ItemStack itemStack) {
        List<MutableText> lore = new ArrayList();
        lore.add(Text.literal("").append(Text.literal("With the ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("stars ").formatted(Formatting.WHITE)).append(Text.literal("as your witness...").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("Your ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("journey ").formatted(Formatting.WHITE)).append(Text.literal("of ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("forging ").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal("new ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Arcana ").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal("begins!").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal(""));
        lore.add(Text.literal("").append(Text.literal("The ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Forge").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" lets you craft ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Arcana Items").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" and ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("enhanced equipment").formatted(Formatting.DARK_AQUA)).append(Text.literal(".").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("The ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Forge").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" acts as a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("hub ").formatted(Formatting.DARK_AQUA)).append(Text.literal("for other ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Forge Structures").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(".").formatted(Formatting.DARK_PURPLE)));

        //forge structure lore
        lore.add(Text.literal("Forge Structures:").formatted(new Formatting[]{Formatting.BOLD, Formatting.LIGHT_PURPLE}));
        lore.add(Text.literal("").append(Text.literal("Are ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("multiblock structures").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" that must be ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("built").formatted(Formatting.AQUA)).append(Text.literal(" in the ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("world").formatted(Formatting.DARK_AQUA)).append(Text.literal(".").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("Must ").formatted(Formatting.DARK_AQUA)).append(Text.literal("be ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("placed ").formatted(Formatting.AQUA)).append(Text.literal("within a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("17x11x17").formatted(Formatting.DARK_AQUA)).append(Text.literal(" cube around a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Starlight Forge").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(".").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("Right Click").formatted(Formatting.DARK_AQUA)).append(Text.literal(" a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("completed ").formatted(Formatting.AQUA)).append(Text.literal("Forge Structure").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" to ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("use").formatted(Formatting.AQUA)).append(Text.literal(" it.").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("Right Click").formatted(Formatting.DARK_AQUA)).append(Text.literal(" a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Forge Structure").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(" to see a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("hologram ").formatted(Formatting.AQUA)).append(Text.literal("of the ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("structure").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal(".").formatted(Formatting.DARK_PURPLE)));

        return (List)lore.stream().map(TextUtils::removeItalics).collect(Collectors.toCollection(ArrayList::new));
    }

    protected ArcanaRecipe makeRecipe() {
        ExplainIngredient a = (new ExplainIngredient(GraphicalItem.withColor(GraphicItems.PAGE_BG, 2756918), 1, "", false)).withName(Text.literal("In World Recipe").formatted(Formatting.BLUE)).withLore(List.of(Text.literal("Do this in the World").formatted(Formatting.DARK_PURPLE)));
        ExplainIngredient s = (new ExplainIngredient(Items.SMITHING_TABLE, 1, "Smithing Table")).withName(Text.literal("Smithing Table").formatted(new Formatting[]{Formatting.BOLD, Formatting.GRAY})).withLore(List.of(Text.literal("Place a Smithing Table in the World").formatted(Formatting.DARK_PURPLE)));
        ExplainIngredient m = (new ExplainIngredient(Items.SEA_LANTERN, 1, "", false)).withName(Text.literal("Night of a New Moon").formatted(new Formatting[]{Formatting.BOLD, Formatting.GRAY})).withLore(List.of(Text.literal("Follow this Recipe under the darkness of a New Moon").formatted(Formatting.DARK_PURPLE)));
        ExplainIngredient g = (new ExplainIngredient(Items.ENCHANTED_GOLDEN_APPLE, 1, "Enchanted Golden Apple")).withName(Text.literal("Enchanted Golden Apple").formatted(new Formatting[]{Formatting.BOLD, Formatting.GOLD})).withLore(List.of(Text.literal("Place the apple upon the Smithing Table.").formatted(Formatting.DARK_PURPLE)));
        ExplainIngredient t = (new ExplainIngredient(ArcanaRegistry.ARCANE_TOME.getItem(), 1, "Tome of Arcana Novum")).withName(Text.literal("Tome of Arcana Novum").formatted(new Formatting[]{Formatting.BOLD, Formatting.DARK_AQUA})).withLore(List.of(Text.literal("Place the Tome upon the Smithing Table.").formatted(Formatting.DARK_PURPLE)));
        ExplainIngredient[][] ingredients = new ExplainIngredient[][]{{m, a, a, a, a}, {a, a, t, a, a}, {a, a, g, a, a}, {a, a, s, a, a}, {a, a, a, a, a}};
        return new ExplainRecipe(ingredients);
    }

    public List<List<Text>> getBookLore() {
        List<List<Text>> list = new ArrayList();
        list.add(List.of(Text.literal("This block was once the center of an ailen civilaisation. Not much is known about this.").formatted(Formatting.BLACK).formatted(Formatting.OBFUSCATED)));
        return list;
    }

    public static class CosmicForgeBlock extends ArcanaPolymerBlockEntity implements BlockWithMovingElementHolder {
        public CosmicForgeBlock(AbstractBlock.Settings settings) {
            super(settings);
        }

        public BlockState getPolymerBlockState(BlockState state) {
            return Blocks.SHROOMLIGHT.getDefaultState();
        }

        public static @Nullable CosmicForgeBlockEntity getEntity(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof CosmicForgeBlock)) {
                return null;
            } else {
                BlockEntity var4 = world.getBlockEntity(pos);
                CosmicForgeBlockEntity var10000;
                if (var4 instanceof CosmicForgeBlockEntity) {
                    CosmicForgeBlockEntity forge = (CosmicForgeBlockEntity)var4;
                    var10000 = forge;
                } else {
                    var10000 = null;
                }

                return var10000;
            }
        }

        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new CosmicForgeBlockEntity(pos, state);
        }

        public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
            return validateTicker(type, CosmicEvolutionArcanaThings.COSMIC_FORGE_BLOCK_ENTITY, CosmicForgeBlockEntity::ticker);
        }

        public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, BlockHitResult hit) {
            CosmicForgeBlockEntity forge = (CosmicForgeBlockEntity)world.getBlockEntity(pos);
            if (forge != null && playerEntity instanceof ServerPlayerEntity player) {
                forge.openGui(player);
            }

            return ActionResult.SUCCESS;
        }

        public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (placer instanceof ServerPlayerEntity player) {
                if (entity instanceof CosmicForgeBlockEntity forge) {
                    initializeArcanaBlock(stack, forge);
                }
            }

        }
    }

    public class CosmicForgeItem extends ArcanaPolymerBlockItem {
        public CosmicForgeItem(Item.Settings settings) {
            super(CosmicForge.this.getThis(), CosmicForge.this.getBlock(), settings);
        }

        public ItemStack getDefaultStack() {
            return CosmicForge.this.prefItem;
        }
    }
}
