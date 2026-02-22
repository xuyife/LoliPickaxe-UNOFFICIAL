package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.util.CommonUtil;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.jetbrains.annotations.NotNull;

public record ServerboundLoliKillFacingPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundLoliKillFacingPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "loli_kill_facing"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundLoliKillFacingPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundLoliKillFacingPacket decode(@NotNull RegistryFriendlyByteBuf buffer) {
            return new ServerboundLoliKillFacingPacket();
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ServerboundLoliKillFacingPacket value) {

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundLoliKillFacingPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            LoliPickaxeUtil.killFacing(player);
            CommonUtil.playLoliSuccessSound(player);
        });
    }
}
