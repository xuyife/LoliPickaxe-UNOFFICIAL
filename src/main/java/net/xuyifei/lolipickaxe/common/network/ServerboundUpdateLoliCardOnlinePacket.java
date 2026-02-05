package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliCardOnline;
import org.jetbrains.annotations.NotNull;

public record ServerboundUpdateLoliCardOnlinePacket(String name) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundUpdateLoliCardOnlinePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "update_loli_card_online"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundUpdateLoliCardOnlinePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ServerboundUpdateLoliCardOnlinePacket::name,
            ServerboundUpdateLoliCardOnlinePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundUpdateLoliCardOnlinePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() instanceof LoliCardOnline) {
                CompoundTag tag = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag();
                tag.putString("ImageUrl", packet.name);
                stack.set(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.of(tag));
            }
        });
    }
}
