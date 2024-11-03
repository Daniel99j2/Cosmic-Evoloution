package com.daniel99j.cosmic.gui;

import net.borisshoes.arcananovum.ArcanaRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;

public class CosmicForgeSlot extends Slot {
    public CosmicForgeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof PickaxeItem || stack.getItem() instanceof AxeItem || stack.getItem() instanceof ShovelItem || stack.getItem() instanceof HoeItem || stack.getItem() instanceof ArmorItem;
    }

    public int getMaxItemCount() {
        return 1;
    }
}
