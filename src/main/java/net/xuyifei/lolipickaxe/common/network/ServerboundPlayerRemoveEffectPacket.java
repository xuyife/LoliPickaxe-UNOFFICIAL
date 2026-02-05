package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundPlayerRemoveEffectPacket (Holder<MobEffect> effectHolder) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundPlayerRemoveEffectPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "player_remove_effect"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundPlayerRemoveEffectPacket> STREAM_CODEC = StreamCodec.composite(
            MobEffect.STREAM_CODEC,
            ServerboundPlayerRemoveEffectPacket::effectHolder,
            ServerboundPlayerRemoveEffectPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundPlayerRemoveEffectPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            player.removeEffect(packet.effectHolder);
        });
    }
}
