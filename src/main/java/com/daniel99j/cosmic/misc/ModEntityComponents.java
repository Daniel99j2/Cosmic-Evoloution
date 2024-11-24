package com.daniel99j.cosmic.misc;
import com.daniel99j.cosmic.CosmicEvolution;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<PvpData> PVP_DATA = ComponentRegistry.getOrCreate(Identifier.of(CosmicEvolution.MOD_ID, "pvp_data"), PvpData.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, PVP_DATA).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PvpData::new);
    }
}