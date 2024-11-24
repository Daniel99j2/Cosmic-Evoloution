package com.daniel99j.cosmic.gui;

import com.daniel99j.cosmic.block.blocks.SpaceAltarBlockEntity;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.borisshoes.arcananovum.items.normal.GraphicItems;
import net.borisshoes.arcananovum.items.normal.GraphicalItem;
import net.borisshoes.arcananovum.utils.MiscUtils;
import net.borisshoes.arcananovum.utils.TextUtils;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Iterator;

public class SpaceAltarGui extends SimpleGui {
    private final SpaceAltarBlockEntity blockEntity;
    private final DefaultedList<ItemStack> ingredients;
    private CosmicForgeInventory inv;

    public SpaceAltarGui(ServerPlayerEntity player, SpaceAltarBlockEntity blockEntity) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.blockEntity = blockEntity;
        this.ingredients = DefaultedList.of();
    }

    public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
        return true;
    }

    public void onTick() {
        World world = this.blockEntity.getWorld();
        if (world == null || world.getBlockEntity(this.blockEntity.getPos()) != this.blockEntity || !this.blockEntity.isAssembled()) {
            this.close();
        }
        super.onTick();
    }

    public void buildGui() {
        if (this.inv == null) {
            this.inv = new CosmicForgeInventory();
        }

        for(int i = 0; i < this.getSize(); ++i) {
            this.clearSlot(i);
            if (i % 9 < 4) {
                this.setSlot(i, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_LEFT, 7996617)).setName(Text.literal("Place Recipe Here >").formatted(Formatting.DARK_PURPLE)));
            } else if (i % 9 == 4) {
                this.setSlot(i, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_RIGHT, 7996617)).setName(Text.literal("< Place Recipe Here").formatted(Formatting.DARK_PURPLE)));
            } else {
                this.setSlot(i, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.PAGE_BG, 2756918)).setName(Text.empty()).hideTooltip());
            }
        }

        GuiElementBuilder bookItem = new GuiElementBuilder(Items.KNOWLEDGE_BOOK);
        bookItem.setName(Text.literal("").append(Text.literal("Read About Stardust Infusion").formatted(Formatting.GREEN)));
        this.setSlot(17, bookItem);
        GuiElementBuilder craftingItem = new GuiElementBuilder(Items.CRAFTING_TABLE);
        craftingItem.setName(Text.literal("").append(Text.literal("Forge Item").formatted(Formatting.AQUA)));
        craftingItem.addLoreLine(TextUtils.removeItalics(Text.literal("").append(Text.literal("Click Here ").formatted(Formatting.GREEN)).append(Text.literal("to forge an item once a recipe is loaded!").formatted(Formatting.DARK_AQUA))));
        craftingItem.addLoreLine(TextUtils.removeItalics(Text.literal("").append(Text.literal("").formatted(Formatting.DARK_AQUA))));
        craftingItem.addLoreLine(TextUtils.removeItalics(Text.literal("").append(Text.literal("This slot will show an item once a valid recipe is loaded.").formatted(Formatting.LIGHT_PURPLE))));
        this.setSlot(15, craftingItem);

        this.setTitle(Text.literal("Advanced Stardust Infusion"));

        this.setSlotRedirect(4, new CosmicForgeSlot(this.inv, 0, 0, 0));
    }

    public void close() {
        super.close();
    }

    public void onClose() {
        DefaultedList<ItemStack> list = this.ingredients;
        SimpleInventory returnInv = new SimpleInventory(list.size() + 1);
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            ItemStack stack = (ItemStack)var3.next();
            returnInv.addStack(stack);
        }

        MiscUtils.returnItems(returnInv, this.player);
    }
}
