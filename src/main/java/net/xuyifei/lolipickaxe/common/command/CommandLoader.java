package net.xuyifei.lolipickaxe.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class CommandLoader {
    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(ConfigCommand.register()
                .requires((commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ADMINS)))
        );

        dispatcher.register(LoliBuffAttackCommand.register()
                .requires((commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ADMINS)))
        );
    }
}
