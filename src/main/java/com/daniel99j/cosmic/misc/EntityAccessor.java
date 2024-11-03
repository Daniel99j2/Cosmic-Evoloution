package com.daniel99j.cosmic.misc;

import net.minecraft.server.network.ServerPlayerEntity;

public interface EntityAccessor {
    void markToExplode(ServerPlayerEntity explodeSource);
}
