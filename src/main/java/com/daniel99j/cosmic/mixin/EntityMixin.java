package com.daniel99j.cosmic.mixin;

import com.daniel99j.cosmic.misc.EntityAccessor;
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

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccessor {

    @Shadow public abstract World getWorld();

    @Shadow public abstract Vec3d getPos();

    @Shadow public abstract Vec3d getVelocity();

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    private ExplosionBehavior NO_BLOCK_BREAK = new ExplosionBehavior() {
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            return false;
        }
    };

    private boolean willExplode;
    private ServerPlayerEntity explodeSource;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(willExplode && this.getVelocity().getY() < 0.05) {
            this.getWorld().createExplosion(explodeSource, Explosion.createDamageSource(this.getWorld(), explodeSource), NO_BLOCK_BREAK, this.getX(), this.getY(), this.getZ(), 3.5F, false, World.ExplosionSourceType.MOB);
            willExplode = false;
        }
    }

    @Override
    public void markToExplode(ServerPlayerEntity explodeSource) {
        this.willExplode = true;
        this.explodeSource = explodeSource;
    };

    private Entity getEntity() {
        EntityAccessor entity = this;
        return (Entity) entity;
    }
}