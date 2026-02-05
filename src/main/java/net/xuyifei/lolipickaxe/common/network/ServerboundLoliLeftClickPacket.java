package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundLoliLeftClickPacket(BlockPos blockPos, ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundLoliLeftClickPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "loli_left_click"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundLoliLeftClickPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ServerboundLoliLeftClickPacket::blockPos,
            ItemStack.STREAM_CODEC,
            ServerboundLoliLeftClickPacket::stack,
            ServerboundLoliLeftClickPacket::new
    );

    public BlockPos getPos() {
        return blockPos;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundLoliLeftClickPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            Level level = player.level();
            ((net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) packet.getStack().getItem()).onLeftClick(level, player, packet.getPos());
        });
    }
}
