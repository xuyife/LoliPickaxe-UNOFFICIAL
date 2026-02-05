package net.xuyifei.lolipickaxe.common.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.*;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xuyifei.lolipickaxe.LoliPickaxe;

@EventBusSubscriber(modid = LoliPickaxe.MODID)
public class PacketLoader {
    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                ServerboundSetSlotPacket.TYPE,
                ServerboundSetSlotPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundSetSlotPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundLoliPickaxeContainerPacket.TYPE,
                ServerboundLoliPickaxeContainerPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundLoliPickaxeContainerPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundLoliKillFacingPacket.TYPE,
                ServerboundLoliKillFacingPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundLoliKillFacingPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundLoliLeftClickPacket.TYPE,
                ServerboundLoliLeftClickPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundLoliLeftClickPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundOpenLoliPickaxeContainerGuiPacket.TYPE,
                ServerboundOpenLoliPickaxeContainerGuiPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundOpenLoliPickaxeContainerGuiPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundLoliPickaxeDropAllPacket.TYPE,
                ServerboundLoliPickaxeDropAllPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundLoliPickaxeDropAllPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundOpenLoliPickaxeContainerBlackListGuiPacket.TYPE,
                ServerboundOpenLoliPickaxeContainerBlackListGuiPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundOpenLoliPickaxeContainerBlackListGuiPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundPlayerAddEffectPacket.TYPE,
                ServerboundPlayerAddEffectPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundPlayerAddEffectPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundPlayerRemoveEffectPacket.TYPE,
                ServerboundPlayerRemoveEffectPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundPlayerRemoveEffectPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundOpenSmallLoliPickaxeContainerGuiPacket.TYPE,
                ServerboundOpenSmallLoliPickaxeContainerGuiPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundOpenSmallLoliPickaxeContainerGuiPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundUpdateLoliCardPacket.TYPE,
                ServerboundUpdateLoliCardPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundUpdateLoliCardPacket::handle
                )
        );
        registrar.playBidirectional(
                ServerboundUpdateLoliCardOnlinePacket.TYPE,
                ServerboundUpdateLoliCardOnlinePacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        null,
                        ServerboundUpdateLoliCardOnlinePacket::handle
                )
        );
        registrar.playBidirectional(
                ClientboundLoliDeadPacket.TYPE,
                ClientboundLoliDeadPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientboundLoliDeadPacket::handle,
                        null
                )
        );
    }
}
