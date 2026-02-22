package net.xuyifei.lolipickaxe.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.xuyifei.lolipickaxe.common.network.ClientboundLoliDeadPacket;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;

import java.util.Collection;

public class LoliBuffAttackCommand {
    private static final SimpleCommandExceptionType BUFF_ATTACK_HAS_BEEN_DISABLED = new SimpleCommandExceptionType(
            Component.literal("特效攻击已被禁用！").withStyle(ChatFormatting.RED)
    );
    private static final SimpleCommandExceptionType TYPE_NOT_FOUND = new SimpleCommandExceptionType(
            Component.literal("未找到此攻击特效！").withStyle(ChatFormatting.RED)
    );

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("loliattack")
                .then(Commands.argument("target", EntityArgument.players())
                        .then(Commands.literal("loliPickaxeBlueScreenAttack")
                                .executes(context -> execute(context, "loliPickaxeBlueScreenAttack", EntityArgument.getPlayers(context, "target"))))
                        .then(Commands.literal("loliPickaxeExitAttack")
                                .executes(context -> execute(context, "loliPickaxeExitAttack", EntityArgument.getPlayers(context, "target"))))
                        .then(Commands.literal("loliPickaxeFailRespondAttack")
                                .executes(context -> execute(context, "loliPickaxeFailRespondAttack", EntityArgument.getPlayers(context, "target")))));

    }

    private static int execute(CommandContext<CommandSourceStack> ctx, String key, Collection<ServerPlayer> players) throws CommandSyntaxException {
        if (!ModConfigs.INSTANCE.loliEnableBuffAttackTNT.get()) {
            throw BUFF_ATTACK_HAS_BEEN_DISABLED.create();
        }
        for (ServerPlayer player : players) {
            switch (key) {
                case "loliPickaxeBlueScreenAttack":
                    player.connection.send(new ClientboundLoliDeadPacket(false, true, false, false));
                    break;
                case "loliPickaxeExitAttack":
                    player.connection.send(new ClientboundLoliDeadPacket(false, false, true, false));
                    break;
                case "loliPickaxeFailRespondAttack":
                    player.connection.send(new ClientboundLoliDeadPacket(false, false, false, true));
                    break;
                default:
                    throw TYPE_NOT_FOUND.create();
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
