package com.daniel99j.cosmic.mixin;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.misc.EntityAccessor;
import net.borisshoes.arcananovum.ArcanaNovum;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArcanaNovum.class)
public abstract class ArcanaLoadsMixin {
    @Inject(method = "onInitialize", at = @At("TAIL"), remap = false)
    public void onInitialize(CallbackInfo ci) {
        CosmicEvolution.LOGGER.info("Loading Arcana first!");
    }
}