package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.misc.PlayerAccessor;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class PhantomBlade extends SwordItem implements PolymerItem {
    public PhantomBlade(Settings settings) {
        super(ToolMaterials.NETHERITE, settings);
    }
    private static final HashMap<ItemStack, Integer> usesMap = new HashMap<>();

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.NETHERITE_SWORD;
        //enchant my leggings
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!user.getWorld().isClient && !user.horizontalCollision) {
            ((PlayerAccessor) user).activatePhantomBlading();
            user.setCurrentHand(hand);
        }
        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(!usesMap.containsKey(stack))
            usesMap.put(stack, 0);
        if(usesMap.get(stack) >= 3) {
            user.clearActiveItem();
            this.onStoppedUsing(stack, world ,user, remainingUseTicks);
        }
        if(remainingUseTicks <= 1) {
            this.onStoppedUsing(stack, world ,user, remainingUseTicks);
            user.clearActiveItem();
        }
        if(!user.getWorld().isClient && !user.horizontalCollision) {
            ((PlayerAccessor) user).activatePhantomBlading();
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 60;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        usesMap.put(stack, usesMap.get(stack)+1);
        if(usesMap.get(stack) >= 3) {
            ((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), 120);
            usesMap.put(stack, 0);
        } else {
            ((PlayerEntity) user).getItemCooldownManager().set(stack.getItem(), 10);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }
}
