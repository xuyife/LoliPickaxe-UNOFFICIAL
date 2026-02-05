package net.xuyifei.lolipickaxe.common.menu;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.xuyifei.lolipickaxe.client.gui.GUIContainerBlackListLoliPickaxe;
import net.xuyifei.lolipickaxe.client.gui.GUIContainerLoliPickaxe;

@EventBusSubscriber
public class MenuLoader {
    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.LOLI_PICKAXE_CONTAINER.get(), GUIContainerLoliPickaxe::new);
        event.register(ModMenuTypes.LOLI_PICKAXE_BLACK_LIST.get(), GUIContainerBlackListLoliPickaxe::new);
        event.register(ModMenuTypes.SMALL_LOLI_PICKAXE_CONTAINER.get(), GUIContainerLoliPickaxe::new);
    }
}
