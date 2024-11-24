package com.daniel99j.cosmic;

import com.daniel99j.cosmic.block.ModBlocks;
import com.daniel99j.cosmic.item.ModItems;
import com.daniel99j.cosmic.misc.CosmicCommand;
import de.tomalbrc.sandstorm.Particles;
import de.tomalbrc.sandstorm.component.ParticleComponents;
import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.callbacks.PlayerDeathCallback;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class CosmicEvolution implements ModInitializer {
	public static final String MOD_ID = "cosmic";
	public static final Logger DEV_LOGGER = LogManager.getLogger("Cosmic Evolution Debug");
	public static final Logger LOGGER = LogManager.getLogger("Cosmic Evolution");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CosmicEvolution.LOGGER.info("Starting to load Cosmic Evolution...");
		CosmicCommand.registerCommands();

		ArcanaNovum.log(0, "Cosmic Arcana things loading!");
		ArcanaRegistry.getModelData("plsworkie");

		ModItems.registerModItems();
		ModBlocks.register();

		CosmicEvolutionArcanaThings.initialize();

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			if(!player.isSneaking()) {

				spawnBlockOutlineParticles((ServerWorld) world, state, pos);
				return false;
			};
			return true;
			}
		);

		ParticleComponents.init();
		InputStream stream = CosmicEvolution.class.getResourceAsStream("/particle/space_altar_magic.json");
		Particles.loadEffect(stream);
		InputStream stream1 = CosmicEvolution.class.getResourceAsStream("/particle/space_altar_ambient.json");
		Particles.loadEffect(stream1);

		CosmicEvolution.LOGGER.info("Loaded Cosmic Evolution!");
		//TODO: 1.21.2
	}

	public static void debug(String text) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			CosmicEvolution.DEV_LOGGER.info(text);
		}
	}

	private static void spawnBlockOutlineParticles(ServerWorld world, BlockState state, BlockPos blockPos) {
		VoxelShape shape = state.getOutlineShape(world, blockPos);

		if (!shape.isEmpty()) {
			shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
				Vec3d min = Vec3d.of(blockPos).add(minX, minY, minZ);
				Vec3d max = Vec3d.of(blockPos).add(maxX, maxY, maxZ);
				spawnParticlesOnAllEdges(world, min, max);
			});
		}
	}

	private static void spawnParticlesOnAllEdges(ServerWorld world, Vec3d min, Vec3d max) {
		// Spawn particles on the bottom and top faces (horizontal edges)
		for (double x = min.x; x <= max.x; x += 0.2) {
			for (double z = min.z; z <= max.z; z += 0.2) {
				world.spawnParticles(ParticleTypes.END_ROD, x, min.y, z, 1, 0, 0, 0, 0); // Bottom face
				world.spawnParticles(ParticleTypes.END_ROD, x, max.y, z, 1, 0, 0, 0, 0); // Top face
			}
		}

		// Spawn particles on the vertical edges
		for (double y = min.y; y <= max.y; y += 0.2) {
			for (double x = min.x; x <= max.x; x += 0.2) {
				world.spawnParticles(ParticleTypes.END_ROD, x, y, min.z, 1, 0, 0, 0, 0); // North face
				world.spawnParticles(ParticleTypes.END_ROD, x, y, max.z, 1, 0, 0, 0, 0); // South face
			}
			for (double z = min.z; z <= max.z; z += 0.2) {
				world.spawnParticles(ParticleTypes.END_ROD, min.x, y, z, 1, 0, 0, 0, 0); // West face
				world.spawnParticles(ParticleTypes.END_ROD, max.x, y, z, 1, 0, 0, 0, 0); // East face
			}
		}
	}
}