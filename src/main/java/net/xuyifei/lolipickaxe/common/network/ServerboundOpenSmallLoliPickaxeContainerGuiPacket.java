package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.GUIOpenWrapper;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenSmallLoliPickaxeContainerGuiPacket() implements CustomPacketPayload {
    public static final Type<ServerboundOpenSmallLoliPickaxeContainerGuiPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "open_small_loli_pickaxe_container_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenSmallLoliPickaxeContainerGuiPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundOpenSmallLoliPickaxeContainerGuiPacket decode(@NotNull RegistryFriendlyByteBuf buffer) {
            return new ServerboundOpenSmallLoliPickaxeContainerGuiPacket();
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ServerboundOpenSmallLoliPickaxeContainerGuiPacket value) {

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenSmallLoliPickaxeContainerGuiPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            // 自动检测手持物品
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            ItemStack stack = ItemStack.EMPTY;

            int slotIndex = -2;

            // 检查主手物品（替换为你的物品检查逻辑）
            if (mainHand.getItem() instanceof SmallLoliPickaxe) {
                slotIndex = player.getInventory().selected;
                stack = mainHand;
            }
            // 检查副手物品
            else if (offHand.getItem() instanceof SmallLoliPickaxe) {
                slotIndex = -1;
                stack = offHand;
            }
            GUIOpenWrapper.OpenSmallContainerGUI(player, stack, slotIndex);
        });
    }
}
