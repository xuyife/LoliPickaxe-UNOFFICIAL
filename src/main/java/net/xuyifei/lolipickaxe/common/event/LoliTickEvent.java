package net.xuyifei.lolipickaxe.common.event;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoliTickEvent {
    private static final Map<UUID, Integer> pendingTasks = new HashMap<>();

    public static void schedule(Runnable task, int ticksToWait) {
        UUID taskId = UUID.randomUUID();
        pendingTasks.put(taskId, ticksToWait);

        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> {
            Integer remaining = pendingTasks.get(taskId);
            if (remaining != null) {
                if (remaining <= 0) {
                    task.run();
                    pendingTasks.remove(taskId);
                } else {
                    pendingTasks.put(taskId, remaining - 1);
                }
            }
        });
    }
}
