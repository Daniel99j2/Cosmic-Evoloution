package com.daniel99j.cosmic.block.blocks;

import com.daniel99j.cosmic.CosmicEvolutionArcanaThings;
import com.daniel99j.cosmic.block.ModBlocks;
import com.daniel99j.cosmic.item.ModItems;
import eu.pb4.factorytools.api.virtualentity.BlockModel;
import eu.pb4.factorytools.api.virtualentity.LodItemDisplayElement;
import eu.pb4.polymer.virtualentity.api.BlockWithMovingElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.core.ArcanaBlock;
import net.borisshoes.arcananovum.core.polymer.ArcanaPolymerBlockEntity;
import net.borisshoes.arcananovum.core.polymer.ArcanaPolymerBlockItem;
import net.borisshoes.arcananovum.gui.arcanetome.TomeGui.TomeFilter;
import net.borisshoes.arcananovum.items.normal.GraphicItems;
import net.borisshoes.arcananovum.items.normal.GraphicalItem;
import net.borisshoes.arcananovum.recipes.arcana.ArcanaRecipe;
import net.borisshoes.arcananovum.recipes.arcana.ExplainIngredient;
import net.borisshoes.arcananovum.recipes.arcana.ExplainRecipe;
import net.borisshoes.arcananovum.utils.ArcanaRarity;
import net.borisshoes.arcananovum.utils.TextUtils;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpaceAltar extends ArcanaBlock {
    public static final String ID = "space_altar";

    public SpaceAltar() {
        this.id = "space_altar";
        this.name = "Space Altar";
        this.rarity = ArcanaRarity.DIVINE;
        this.categories = new TomeFilter[]{TomeFilter.DIVINE, TomeFilter.BLOCKS, TomeFilter.ALTARS};
        this.itemVersion = 0;
        this.vanillaItem = Items.HEAVY_CORE;
        this.block = ModBlocks.registerBlock("space_altar", new SpaceAltar.SpaceAltarBlock(Settings.create().strength(2.5F, 1200.0F).sounds(BlockSoundGroup.WOOD)));
        this.item = ModItems.register("space_altar", new SpaceAltar.SpaceAltarItem(new Item.Settings().maxCount(1).fireproof().component(DataComponentTypes.ITEM_NAME, TextUtils.withColor(Text.literal("Space Altar").formatted(Formatting.BOLD), 47226)).component(DataComponentTypes.LORE, new LoreComponent(SpaceAltar.getItemLoreStatic((ItemStack)null))).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));
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
        //altar lore
        lore.add(Text.literal("").append(Text.literal("Altars ").formatted(Formatting.AQUA)).append(Text.literal("are ").formatted(Formatting.BLUE)).append(Text.literal("multiblock structures").formatted(Formatting.DARK_PURPLE)).append(Text.literal(" that must be ").formatted(Formatting.BLUE)).append(Text.literal("built ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("in the world.").formatted(Formatting.BLUE)));
        lore.add(Text.literal("").append(Text.literal("Left click a block").formatted(Formatting.AQUA)).append(Text.literal(" with an ").formatted(Formatting.BLUE)).append(Text.literal("Altar ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("to see a ").formatted(Formatting.BLUE)).append(Text.literal("hologram ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("of the ").formatted(Formatting.BLUE)).append(Text.literal("structure").formatted(Formatting.DARK_PURPLE)).append(Text.literal(".").formatted(Formatting.BLUE)));
        lore.add(Text.literal("").append(Text.literal("Right click").formatted(Formatting.AQUA)).append(Text.literal(" a ").formatted(Formatting.BLUE)).append(Text.literal("completed ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("Altar ").formatted(Formatting.AQUA)).append(Text.literal("setup to ").formatted(Formatting.BLUE)).append(Text.literal("activate ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("the ").formatted(Formatting.BLUE)).append(Text.literal("Altar").formatted(Formatting.AQUA)).append(Text.literal(".").formatted(Formatting.BLUE)));
        lore.add(Text.literal(""));
        //my lore
        lore.add((MutableText) TextUtils.withColor(Text.literal("Space Altar:").formatted(Formatting.BOLD), 47226));
        lore.add(Text.literal("").append(Text.literal("Forged from the ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("stars").formatted(Formatting.WHITE)).append(Text.literal(", ").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("This was once apart of a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("spaceship ").formatted(Formatting.WHITE)).append(Text.literal("of ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("great power. ").formatted(Formatting.LIGHT_PURPLE)));
        lore.add(Text.literal(""));
        lore.add(Text.literal("").append(Text.literal("Now repurposed as a ").formatted(Formatting.DARK_PURPLE)).append(Text.literal("home utility system ").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal("you can control ").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal("").append(Text.literal("your home ").formatted(Formatting.LIGHT_PURPLE)).append(Text.literal("using arcane powers!").formatted(Formatting.DARK_PURPLE)));
        lore.add(Text.literal(""));
        lore.add(Text.literal("").append(ArcanaRarity.getColoredLabel(ArcanaRarity.DIVINE, true)).append(" Arcana Item").formatted(Formatting.DARK_PURPLE));

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

    public static class SpaceAltarBlock extends ArcanaPolymerBlockEntity implements BlockWithMovingElementHolder {
        public SpaceAltarBlock(Settings settings) {
            super(settings);
        }

        public BlockState getPolymerBlockState(BlockState state) {
            return Blocks.HEAVY_CORE.getDefaultState();
        }

        public static @Nullable SpaceAltarBlockEntity getEntity(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof SpaceAltarBlock)) {
                return null;
            } else {
                BlockEntity var4 = world.getBlockEntity(pos);
                SpaceAltarBlockEntity var10000;
                if (var4 instanceof SpaceAltarBlockEntity) {
                    SpaceAltarBlockEntity altar = (SpaceAltarBlockEntity)var4;
                    var10000 = altar;
                } else {
                    var10000 = null;
                }

                return var10000;
            }
        }

        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new SpaceAltarBlockEntity(pos, state);
        }

        public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
            return validateTicker(type, CosmicEvolutionArcanaThings.SPACE_ALTAR_BLOCK_ENTITY, SpaceAltarBlockEntity::ticker);
        }

        public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, BlockHitResult hit) {
            SpaceAltarBlockEntity altar = (SpaceAltarBlockEntity)world.getBlockEntity(pos);
            if (altar != null && playerEntity instanceof ServerPlayerEntity player) {
                altar.openGui(player);
            }

            return ActionResult.SUCCESS;
        }

        public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (placer instanceof ServerPlayerEntity player) {
                if (entity instanceof SpaceAltarBlockEntity altar) {
                    initializeArcanaBlock(stack, altar);
                }
            }
        }

        @Override
        public ElementHolder createElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
            return new Model(world, initialBlockState);
        }

        @Override
        public boolean tickElementHolder(ServerWorld world, BlockPos pos, BlockState initialBlockState) {
            return true;
        }

        public static final class Model extends BlockModel {
            private final ItemDisplayElement mainElement;

            private Model(ServerWorld world, BlockState state) {
                this.mainElement = LodItemDisplayElement.createSimple(Items.PAPER.getDefaultStack(), this.getUpdateRate(), 0.3f, 0.6f);
                this.mainElement.setTeleportDuration(0);
                this.addElement(this.mainElement);
                this.updateAnimation(world);
            }

            private int getUpdateRate() {
                return 1;
            }


            private void updateAnimation(World world) {
                ItemStack itemStack = Items.STONE_BRICK_WALL.getDefaultStack();
                this.mainElement.setItem(itemStack);
                this.mainElement.setInterpolationDuration(0);
                this.mainElement.setTeleportDuration(0);
                this.mainElement.setInvisible(true);
                this.mainElement.setYaw((world.getTime() * 2.25F)*-1);
                this.mainElement.setOffset(new Vec3d(0, 3, 0));
                this.mainElement.setScale(new Vector3f(5));
                this.mainElement.startInterpolation();
            }

            @Override
            protected void onTick() {
                if (!Objects.requireNonNull(this.blockAware()).isPartOfTheWorld()) {
                    return;
                }
                var tick = Objects.requireNonNull(this.blockAware()).getWorld().getTime();
                if (tick % this.getUpdateRate() == 0) {
                    this.updateAnimation(Objects.requireNonNull(Objects.requireNonNull(this.blockAware()).getWorld()));
                    this.mainElement.startInterpolationIfDirty();
                }
            }
        }
    }

    public class SpaceAltarItem extends ArcanaPolymerBlockItem {
        public SpaceAltarItem(Settings settings) {
            super(SpaceAltar.this.getThis(), SpaceAltar.this.getBlock(), settings);
        }

        public ItemStack getDefaultStack() {
            return SpaceAltar.this.prefItem;
        }
    }
}
