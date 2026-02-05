package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundPlayerAddEffectPacket(MobEffectInstance mobEffectInstance) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundPlayerAddEffectPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "player_add_effect"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundPlayerAddEffectPacket> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC,
            ServerboundPlayerAddEffectPacket::mobEffectInstance,
            ServerboundPlayerAddEffectPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundPlayerAddEffectPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            player.addEffect(packet.mobEffectInstance);
        });
    }
}
