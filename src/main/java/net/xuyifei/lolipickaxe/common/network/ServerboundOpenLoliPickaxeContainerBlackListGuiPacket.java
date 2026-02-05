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
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.jetbrains.annotations.NotNull;

public record ServerboundOpenLoliPickaxeContainerBlackListGuiPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundOpenLoliPickaxeContainerBlackListGuiPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "open_loli_pickaxe_container_black_list_gui"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenLoliPickaxeContainerBlackListGuiPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundOpenLoliPickaxeContainerBlackListGuiPacket decode(@NotNull RegistryFriendlyByteBuf buffer) {
            return new ServerboundOpenLoliPickaxeContainerBlackListGuiPacket();
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ServerboundOpenLoliPickaxeContainerBlackListGuiPacket value) {

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundOpenLoliPickaxeContainerBlackListGuiPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            // 自动检测手持物品
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();

            int slotIndex = -2;

            // 检查主手物品（替换为你的物品检查逻辑）
            if (mainHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                slotIndex = player.getInventory().selected;
            }
            // 检查副手物品
            else if (offHand.getItem() instanceof net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe) {
                slotIndex = -1;
            }
            GUIOpenWrapper.OpenContainerBlackListGUI(player, LoliPickaxeUtil.getLoliPickaxe(player), slotIndex);
        });
    }

}
