package com.daniel99j.cosmic.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class CosmicCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        LiteralCommandNode<ServerCommandSource> rootCommand = CommandManager.literal("cosmic")
                .then(CommandManager.literal("pvp")
                        .then(CommandManager.literal("set-battle-state").requires(source -> source.hasPermissionLevel(2))
                                .then(CommandManager.argument("players", EntityArgumentType.players())
                                        .then(CommandManager.argument("state", BoolArgumentType.bool())
                                            .executes(CosmicCommand::setInBattle))))
                        .then(CommandManager.literal("set-animation-ticks").requires(source -> source.hasPermissionLevel(2))
                                .then(CommandManager.argument("players", EntityArgumentType.players())
                                        .then(CommandManager.argument("ticks", IntegerArgumentType.integer(0, 32767))
                                                .executes(CosmicCommand::setAnimationTicks))))
                        .then(CommandManager.literal("debug-player").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .executes(CosmicCommand::debugPlayer))))
                .build();

        dispatcher.getRoot().addChild(rootCommand);
    }

    private static int debugPlayer(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(context, "player");
        } catch (CommandSyntaxException e) {
            return 0;
        }
        source.sendFeedback(() -> Text.literal("PVP Data for:"), true);
        source.sendFeedback(() -> Text.literal("In Battle: "+ModEntityComponents.PVP_DATA.get(player).getInBattle()), true);
        source.sendFeedback(() -> Text.literal("Animation Ticks: "+ModEntityComponents.PVP_DATA.get(player).getArcanaDeathTicks()), true);
        return 1;
    }

    private static int setInBattle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        Collection<ServerPlayerEntity> players;
        try {
            players = EntityArgumentType.getPlayers(context, "players");
        } catch (CommandSyntaxException e) {
            return 0;
        }
        for (ServerPlayerEntity player : players) {
            ModEntityComponents.PVP_DATA.get(player).setInBattle(BoolArgumentType.getBool(context, "state"));
        }
        source.sendFeedback(() -> Text.literal("Set "+players.size()+" player(s) battle state to "+BoolArgumentType.getBool(context, "state")), true);
        return 1;
    }

    private static int setAnimationTicks(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        Collection<ServerPlayerEntity> players;
        try {
            players = EntityArgumentType.getPlayers(context, "players");
        } catch (CommandSyntaxException e) {
            return 0;
        }
        for (ServerPlayerEntity player : players) {
            ModEntityComponents.PVP_DATA.get(player).setArcanaDeathTicks(IntegerArgumentType.getInteger(context, "ticks"));
        }
        source.sendFeedback(() -> Text.literal("Set "+players.size()+" player(s) arcana death animation ticks to "+ IntegerArgumentType.getInteger(context, "ticks")), true);
        return 1;
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            register(dispatcher, registryAccess);
        });
    }
}
