package com.daniel99j.cosmic.mixin;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.item.ExoskeletonArmor;
import com.daniel99j.cosmic.item.PhantomBlade;
import com.daniel99j.cosmic.misc.PlayerAccessor;
import com.google.common.collect.ImmutableList;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin
        extends LivingEntity implements PlayerAccessor {

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract PlayerInventory getInventory();

    private boolean isPhantomBlading;
    private int phantomBladingTicks;
    private boolean hadJetpack = false;
    public PlayerMixin(EntityType<?> entityType, World world, ElementHolder holder) {
        super((EntityType<? extends LivingEntity>) entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(!this.getWorld().isClient) {
            if (this.phantomBladingTicks > 0) {
                this.phantomBladingTicks--;
                this.isPhantomBlading = true;
                this.setSwimming(false);
                this.setLivingFlag(4, true);
                this.getPlayer().setPose(EntityPose.SPIN_ATTACK);

                acceleratePlayerFacing(this.getPlayer(), 1.7);

                Box box = this.getBoundingBox().expand(1.5);
                List<Entity> list = this.getWorld().getOtherEntities(this, box);
                if (!list.isEmpty()) {
                    for (Entity entity : list) {
                        if (entity instanceof LivingEntity living && living.isAlive() && living.hurtTime < 10) {
                            this.attackLivingEntity(living);
                            this.getPlayer().resetLastAttackedTicks();
                            break;
                        }
                    }
                } else if (this.horizontalCollision) {
                    this.deactivatePhantomBlading();
                }
            } else if(this.isPhantomBlading) {
                this.deactivatePhantomBlading();
            }

            if((!this.hadJetpack || !this.getPlayer().getAbilities().allowFlying) && !this.getInventory().getArmorStack(2).isEmpty() && this.getInventory().getArmorStack(2).getItem() instanceof ExoskeletonArmor) {
                this.getPlayer().getAbilities().allowFlying = true;
                this.getPlayer().networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.getPlayer().getAbilities()));
                this.hadJetpack = true;
            }
            if(this.hadJetpack && (this.getInventory().getArmorStack(2).isEmpty() || !(this.getInventory().getArmorStack(2).getItem() instanceof ExoskeletonArmor))) {
                this.hadJetpack = false;
                this.getPlayer().getAbilities().allowFlying = (this.getPlayer().interactionManager.getGameMode() == GameMode.SPECTATOR || this.getPlayer().interactionManager.getGameMode() == GameMode.CREATIVE);
                if(this.getPlayer().getAbilities().flying)
                    this.getPlayer().getAbilities().flying = this.getPlayer().getAbilities().allowFlying;
                this.getPlayer().networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(this.getPlayer().getAbilities()));
            }
        }
    }

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    protected void updatePose(CallbackInfo ci) {
        if(!this.getWorld().isClient) {
            if(this.isPhantomBlading) {
                this.setPose(EntityPose.SPIN_ATTACK);
                ci.cancel();
            }
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if(!this.getWorld().isClient && this.isPhantomBlading && onGround) {
            if(this.fallDistance > 10) {
                this.fallDistance = 0;
                CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, this.getWorld());
                creeper.setPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                this.getWorld().spawnEntity(creeper);
                this.deactivatePhantomBlading();
            }
        } else {
            super.fall(heightDifference, onGround, state, landedPosition);
        }
    }

    @Override
    public boolean isUsingRiptide() {
        if(this.isPhantomBlading && !this.getWorld().isClient) {
            return false;
        }
        return ((Byte)this.dataTracker.get(LIVING_FLAGS) & 4) != 0;
    }

    @Override
    public boolean getPhantomBlading() {
        return this.isPhantomBlading;
    };

    @Override
    public void activatePhantomBlading() {
        this.isPhantomBlading = true;
        this.phantomBladingTicks = 2;
    };

    @Unique
    private void deactivatePhantomBlading() {
        this.isPhantomBlading = false;
        this.phantomBladingTicks = 0;
        this.setLivingFlag(4, this.riptideTicks > 0);
        if(this.getPlayer().getActiveItem().getItem() instanceof PhantomBlade) {
            this.getPlayer().stopUsingItem();
            this.getPlayer().clearActiveItem();
        }
    };

    private ServerPlayerEntity getPlayer() {
        PlayerAccessor player = this;
        PlayerEntity player1 = (PlayerEntity) player;
        return (ServerPlayerEntity) player1;
    }

    @Unique
    private static void acceleratePlayerFacing(ServerPlayerEntity player, double acceleration) {
        float yaw = player.getYaw();
        float pitch = player.getPitch();

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        Vec3d accelerationVector = new Vec3d(x, y, z).normalize().multiply(acceleration);

        player.setVelocity(accelerationVector.x, accelerationVector.y, accelerationVector.z);
        player.velocityModified = true;
        player.velocityDirty = true;
    }
}