package net.xuyifei.lolipickaxe.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;

public class EventUtil {
    public static HitResult rayTrace(Entity entity, double blockReachDistance, float partialTicks) {
        boolean stopOnLiquid = false;
        if (entity instanceof Player player) {
            ItemStack loli = LoliPickaxeUtil.getLoliPickaxe(player);
            if (!loli.isEmpty()) {
                stopOnLiquid = ConfigLoader.getBoolean(loli, "loliPickaxeStopOnLiquid");
                double distance = ConfigLoader.getDouble(loli, "loliPickaxeBlockReachDistance");
                if (distance > blockReachDistance) {
                    blockReachDistance = distance;
                }
            }
        }

        Vec3 vec3d = entity.getEyePosition(partialTicks);
        Vec3 vec3d1 = entity.getViewVector(partialTicks);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);

        return entity.level().clip(new ClipContext(vec3d, vec3d2,
                stopOnLiquid ? ClipContext.Block.OUTLINE : ClipContext.Block.COLLIDER,
                ClipContext.Fluid.ANY,
                entity));
    }

    public static boolean isInvisibleToPlayer(Entity entity, Player player) {
        ItemStack loli = LoliPickaxeUtil.getLoliPickaxe(player);

        if (!loli.isEmpty()) {
            if (ConfigLoader.getBoolean(loli, "loliPickaxeShowInvisible")) {
                return false;
            }
        }

        return entity.isInvisibleTo(player);
    }
}
