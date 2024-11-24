package com.daniel99j.cosmic.mixin;

import com.daniel99j.cosmic.misc.EntityAccessor;
import com.daniel99j.cosmic.misc.ShadowStalkersGlaiveAccessor;
import net.borisshoes.arcananovum.core.EnergyItem;
import net.borisshoes.arcananovum.items.ShadowStalkersGlaive;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShadowStalkersGlaive.ShadowStalkersGlaiveItem.class)
public abstract class ShadowStalkersGlaiveMixin implements ShadowStalkersGlaiveAccessor {
    @SuppressWarnings("shadow")
    @Shadow @Final
    ShadowStalkersGlaive this$0;

    @Override
    public ShadowStalkersGlaive getGlaive() {
        return this$0;
    };
}