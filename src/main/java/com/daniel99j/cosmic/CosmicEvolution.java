package com.daniel99j.cosmic;

import com.daniel99j.cosmic.block.ModBlocks;
import com.daniel99j.cosmic.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CosmicEvolution implements ModInitializer {
	public static final String MOD_ID = "cosmic";
	public static final Logger DEV_LOGGER = LogManager.getLogger("Cosmic Evolution");
	public static final Logger LOGGER = LogManager.getLogger("Cosmic Evolution Debug");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CosmicEvolution.LOGGER.info("Starting to load Cosmic Evolution...");
		ModItems.registerModItems();
		ModBlocks.register();

		UseEntityCallback.EVENT.register(this::onEntityInteract);

		CosmicEvolutionArcanaThings.initialize();

		CosmicEvolution.LOGGER.info("Loaded Cosmic Evolution!");
		//TODO: 1.21.2
	}

	public static void debug(String text) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			CosmicEvolution.DEV_LOGGER.info(text);
		}
	}

	private ActionResult onEntityInteract(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
		if (world.isClient()) {
			return ActionResult.PASS;
		}

		if (entity instanceof EndermiteEntity && player.getStackInHand(hand).getItem() == Items.DRAGON_BREATH) {
			ItemUsage.exchangeStack(player.getStackInHand(hand), player, ModItems.CONCENTRATED_DRAGONS_BREATH.getDefaultStack());
			player.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH);
			entity.remove(Entity.RemovalReason.DISCARDED);
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}
}