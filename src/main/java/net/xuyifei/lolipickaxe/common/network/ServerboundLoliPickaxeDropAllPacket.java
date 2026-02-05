package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.gui.ILoliInventory;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import org.jetbrains.annotations.NotNull;

public record ServerboundLoliPickaxeDropAllPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundLoliPickaxeDropAllPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "lolipickaxe_drop_all"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundLoliPickaxeDropAllPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ServerboundLoliPickaxeDropAllPacket decode(@NotNull RegistryFriendlyByteBuf buffer) {
            return new ServerboundLoliPickaxeDropAllPacket();
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ServerboundLoliPickaxeDropAllPacket value) {

        }
    };

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundLoliPickaxeDropAllPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack loli = player.getMainHandItem();
            if (loli.isEmpty() || !(loli.getItem() instanceof IContainer)) {
                loli = player.getOffhandItem();
            }
            if (!loli.isEmpty() && loli.getItem() instanceof IContainer && ((IContainer) loli.getItem()).hasInventory(loli)) {
                ILoliInventory inventory = ((IContainer) loli.getItem()).getInventory(loli, player.level().registryAccess());
                inventory.startOpen(player);
                NonNullList<ItemStack> stacks = NonNullList.create();
                for (int i = 0; i < inventory.getAllHasItemPageIndex(); i++) {
                    for (ItemStack stack : inventory.getPage(i)) {
                        if (!stack.isEmpty()) {
                            stacks.add(stack);
                        }
                    }
                }
                inventory.clearContent();
                inventory.stopOpen(player);
                for (ItemStack stack : stacks) {
                    ItemEntity entity = new ItemEntity(player.level(), player.getX() + 0.5, player.getY() + 0.5, player.getZ() + 0.5, stack);
                    entity.setPickUpDelay(80);
                    player.level().addFreshEntity(entity);
                }
            }
        });
    }
}
