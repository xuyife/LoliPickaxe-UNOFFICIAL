package net.xuyifei.lolipickaxe.common.event;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.xuyifei.lolipickaxe.common.util.LoliCardUtil;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class LoliCardEvents {
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ClientResourceReloadListener());
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientResourceReloadListener implements ResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
            LoliCardUtil.updateCustomArtDatas();
        }
    }
}
