package com.daniel99j.cosmic.misc;

import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.utils.GenericTimer;
import net.borisshoes.arcananovum.utils.ParticleEffectUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PvpData implements Component, ServerTickingComponent {

    private final PlayerEntity player;
    private Boolean in_battle = false;
    private int arcana_death_ticks = 0;

    public PvpData(PlayerEntity player) {
        this.player = player;
    }

    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        in_battle = nbt.getBoolean("in_battle");
        arcana_death_ticks = nbt.getInt("arcana_death_ticks");
    }

    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putBoolean("in_battle", in_battle);
        nbt.putInt("arcana_death_ticks", arcana_death_ticks);
    }

    public Boolean getInBattle() {
        return in_battle;
    }

    public void setInBattle(Boolean in_battle) {
        this.in_battle = in_battle;
    }

    public int getArcanaDeathTicks() {
        return arcana_death_ticks;
    }

    public void setArcanaDeathTicks(int arcana_death_ticks) {
        this.arcana_death_ticks = arcana_death_ticks;
    }

    public boolean isArcanaDeathAnimation() {
        return this.arcana_death_ticks > 0;
    }

    @Override
    public void serverTick() {
        if(this.isArcanaDeathAnimation()) {
            this.deathAnimationParticles();
            this.arcana_death_ticks = this.arcana_death_ticks-1;
        }
    }

    private void deathAnimationParticles() {
            World world1 = this.player.getEntityWorld();
            if (world1 instanceof ServerWorld) {
                ServerWorld world = (ServerWorld)world1;
                double eHeight = (double)this.player.getHeight();
                double eWidth = (double)this.player.getWidth();
                double circleHeight = eHeight * 0.6;
                double circleRadius = eWidth / 1.6;
                Vec3d circleCenter = this.player.getPos().add(0.0, eHeight / 1.8, 0.0);
                DustParticleEffect purple = new DustParticleEffect(Vec3d.unpackRgb(10551526).toVector3f(), 0.7F);
                int intervals = (int)(15.0 * Math.sqrt(circleRadius * circleRadius + circleHeight * circleHeight));
                double dA = 6.283185307179586 / (double)intervals;

                for(int i = 0; i < intervals; ++i) {
                    double angle = dA * (double)i + (double)this.player.getWorld().getTime() / Math.PI;
                    double xOff = circleRadius * Math.cos(angle);
                    double zOff = circleRadius * Math.sin(angle);
                    double yOff = (xOff + zOff) * 0.3536 * circleHeight / circleRadius;
                    world.spawnParticles(purple, xOff + circleCenter.x, yOff + circleCenter.y, zOff + circleCenter.z, 1, 0.0, 0.0, 0.0, 0.0);
                    world.spawnParticles(purple, xOff + circleCenter.x, -yOff + circleCenter.y, zOff + circleCenter.z, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
        }
}
