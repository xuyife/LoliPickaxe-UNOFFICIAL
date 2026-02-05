package net.xuyifei.lolipickaxe.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.xuyifei.lolipickaxe.common.registry.ModSounds;

public class CommonUtil {
    public static void playLoliSuccessSound(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.server.execute(() -> {
                BlockPos pos = serverPlayer.blockPosition();

                serverPlayer.connection.send(new ClientboundSoundPacket(
                        Holder.direct(ModSounds.LOLI_SUCCESS.get()),
                        SoundSource.BLOCKS,
                        pos.getX(), pos.getY(), pos.getZ(),
                        1.0F, 1.0F,
                        serverPlayer.getRandom().nextLong()
                ));
            });
        }
    }
}
