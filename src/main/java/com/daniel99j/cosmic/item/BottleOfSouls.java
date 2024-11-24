package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.misc.ShadowStalkersGlaiveAccessor;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.cardinalcomponents.ArcanaProfileComponent;
import net.borisshoes.arcananovum.core.EnergyItem;
import net.borisshoes.arcananovum.items.ShadowStalkersGlaive;
import net.borisshoes.arcananovum.utils.SoundUtils;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BottleOfSouls extends Item implements PolymerItem {
    public BottleOfSouls(Settings settings) {
        super(settings);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.OMINOUS_BOTTLE;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipType tooltipType, RegistryWrapper.WrapperLookup lookup, @Nullable ServerPlayerEntity player) {
        ItemStack stack = PolymerItemUtils.createItemStack(itemStack, tooltipType, lookup, player);
        stack.set(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, 1);
        stack.set(DataComponentTypes.FOOD, FoodComponents.OMINOUS_BOTTLE);
        return stack;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (user instanceof PlayerEntity player) {
            boolean worked = false;
            for(ItemStack stack1 : player.getInventory().main) {
                if(stack1.getItem() instanceof ShadowStalkersGlaive.ShadowStalkersGlaiveItem glaiveItem) {
                    ((ShadowStalkersGlaiveAccessor)glaiveItem).getGlaive().setEnergy(stack1, ((ShadowStalkersGlaiveAccessor)glaiveItem).getGlaive().getMaxEnergy(stack1));
                    player.sendMessage(Text.literal("Glaive Charges: ✦ ✦ ✦ ✦ ✦ ").formatted(Formatting.BLACK), true);
                    stack.decrementUnlessCreative(1, user);
                    worked = true;
                    break;
                }
            }
            SoundUtils.playSound(player.getWorld(), user.getBlockPos(), SoundEvents.ITEM_OMINOUS_BOTTLE_DISPOSE, user.getSoundCategory(), 1.0F, 1.0F);
            if(worked) {
                SoundUtils.playSound(player.getWorld(), user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), user.getSoundCategory(), 1.0F, 0.5F);
            } else {
                SoundUtils.playSound(player.getWorld(), user.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, user.getSoundCategory(), 1.0F, 1.0F);
            }
        }

        return stack;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 16;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
