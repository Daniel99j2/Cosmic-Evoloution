package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.misc.EntityAccessor;
import com.daniel99j.cosmic.misc.PlayerAccessor;
import eu.pb4.polymer.core.api.item.PolymerItem;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.List;

public class AMPE extends Item implements PolymerItem {
    public AMPE(Settings settings) {
        super(settings);
    }
    private static final HashMap<ItemStack, Integer> itemUsageTicksMap = new HashMap<>();

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.BRICK;
        //enchant my leggings
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!user.getWorld().isClient) {
            user.setCurrentHand(hand);
        }
        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(user instanceof ServerPlayerEntity player) {
            if (!itemUsageTicksMap.containsKey(stack)) itemUsageTicksMap.put(stack, 1);
            itemUsageTicksMap.put(stack, itemUsageTicksMap.get(stack) + 1);

            String message = "=====";

            int amount = itemUsageTicksMap.get(stack);
            amount = Math.max(0, Math.min(100, amount));

            float ratio = amount / 100.0f;

            int red = (int) (255 * (1 - ratio));
            int green = (int) (255 * ratio);

            int color = (red << 16) | (green << 8);

            player.sendMessage(Text.of(message).copy().setStyle(Style.EMPTY.withColor(color)), true);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 100000000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(user instanceof ServerPlayerEntity player && itemUsageTicksMap.get(stack) > 60) {
            Box box = user.getBoundingBox().expand(3);
            box.offset(moveBox(player));
            List<Entity> list = world.getOtherEntities(user, box);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if(entity instanceof LivingEntity) {
                        accelerateEntityFacing(entity, player, 2.2);
                        ((EntityAccessor) entity).markToExplode(player);
                        if (entity.getVelocity().getY() < 0.5)
                            entity.setVelocity(entity.getVelocity().getX(), 1.4, entity.getVelocity().getZ());
                    }
                }
            }
        }
        itemUsageTicksMap.put(stack, 0);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    private static void accelerateEntityFacing(Entity entity, ServerPlayerEntity origin, double acceleration){
        float yaw = origin.getYaw();
        float pitch = origin.getPitch();

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(acceleration);

        entity.addVelocity(accelerationVector.x, accelerationVector.y, accelerationVector.z);
        entity.velocityModified = true;
        entity.velocityDirty = true;
    }

    private static Vec3d moveBox(ServerPlayerEntity origin){
        float yaw = origin.getYaw();
        float pitch = origin.getPitch();

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3d(x, y, z).normalize().multiply(4);
    }
}
