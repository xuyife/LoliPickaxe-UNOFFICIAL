package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ContainerLoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundLoliPickaxeContainerPacket(boolean next) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundLoliPickaxeContainerPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "lolipickaxe_container"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundLoliPickaxeContainerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ServerboundLoliPickaxeContainerPacket::next,
            ServerboundLoliPickaxeContainerPacket::new
    );

    public boolean isNext() {
        return next;
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundLoliPickaxeContainerPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();

            if (player.containerMenu instanceof ContainerLoliPickaxe container) {
                if (packet.isNext()) {
                    container.nextPage();
                } else {
                    container.prePage();
                }
            }
        });
    }
}
