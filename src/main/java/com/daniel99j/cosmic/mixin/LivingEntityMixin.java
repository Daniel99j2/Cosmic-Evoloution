package com.daniel99j.cosmic.mixin;

import com.daniel99j.cosmic.misc.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(source.getSource() instanceof ServerPlayerEntity player && ModEntityComponents.PVP_DATA.get(player).isArcanaDeathAnimation() && (!source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))) {
            player.setHealth(1);
            cir.cancel();
        }

    }
}