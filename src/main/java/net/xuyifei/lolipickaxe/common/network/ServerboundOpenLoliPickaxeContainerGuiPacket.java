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
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenLoliPickaxeContainerGuiPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundOpenLoliPickaxeContainerGuiPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "open_loli_pickaxe_container_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenLoliPickaxeContainerGuiPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundOpenLoliPickaxeContainerGuiPacket decode(@NotNull RegistryFriendlyByteBuf buffer) {
            return new ServerboundOpenLoliPickaxeContainerGuiPacket();
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ServerboundOpenLoliPickaxeContainerGuiPacket value) {

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenLoliPickaxeContainerGuiPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            // 自动检测手持物品
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            ItemStack stack = ItemStack.EMPTY;

            int slotIndex = -2;

            // 检查主手物品（替换为你的物品检查逻辑）
            if (mainHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                slotIndex = player.getInventory().selected;
                stack = mainHand;
            }
            // 检查副手物品
            else if (offHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                slotIndex = -1;
                stack = offHand;
            }
            GUIOpenWrapper.OpenContainerGUI(player, stack, slotIndex);
        });
    }
}
