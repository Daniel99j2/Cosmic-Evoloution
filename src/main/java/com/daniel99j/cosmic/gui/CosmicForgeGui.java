package com.daniel99j.cosmic.gui;

import com.daniel99j.cosmic.block.blocks.CosmicForgeBlockEntity;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;

import java.util.Iterator;

import net.borisshoes.arcananovum.blocks.forge.StarlightForgeBlockEntity;
import net.borisshoes.arcananovum.gui.midnightenchanter.MidnightEnchanterInventory;
import net.borisshoes.arcananovum.gui.midnightenchanter.MidnightEnchanterSlot;
import net.borisshoes.arcananovum.items.normal.GraphicItems;
import net.borisshoes.arcananovum.items.normal.GraphicalItem;
import net.borisshoes.arcananovum.utils.MiscUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CosmicForgeGui extends SimpleGui {
    private final CosmicForgeBlockEntity blockEntity;
    private final DefaultedList<ItemStack> ingredients;
    private CosmicForgeInventory inv;

    public CosmicForgeGui(ServerPlayerEntity player, CosmicForgeBlockEntity blockEntity) {
        super(ScreenHandlerType.GENERIC_9X6, player, false);
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
        this.setTitle(Text.literal("Advanced Stardust Infusion"));

        Text name = this.inv.isEmpty() ? Text.literal("Insert an Infusable Item").formatted(Formatting.DARK_PURPLE) : Text.literal("Infuse or Disenfuse your item").formatted(Formatting.LIGHT_PURPLE);
        int color = this.inv.isEmpty() ? 2756918 : 478174;

        for(int i = 0; i < 9; ++i) {
            if (i == 0) {
                this.setSlot(i + 9, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_LEFT_CONNECTOR, color)).hideDefaultTooltip().setName(name));
            } else if (i == 8) {
                this.setSlot(i + 9, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_RIGHT_CONNECTOR, color)).hideDefaultTooltip().setName(name));
            } else if (i == 3) {
                this.setSlot(i, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_TOP_RIGHT, color)).hideDefaultTooltip().setName(name));
                this.setSlot(i + 9, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_BOTTOM_CONNECTOR, color)).hideDefaultTooltip().setName(name));
            } else if (i == 5) {
                this.setSlot(i, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_TOP_LEFT, color)).hideDefaultTooltip().setName(name));
                this.setSlot(i + 9, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_BOTTOM_CONNECTOR, color)).hideDefaultTooltip().setName(name));
            } else {
                this.setSlot(i + 9, GuiElementBuilder.from(GraphicalItem.withColor(GraphicItems.MENU_HORIZONTAL, color)).hideDefaultTooltip().setName(name));
            }
        }

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
