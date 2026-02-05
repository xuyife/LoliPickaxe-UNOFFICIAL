package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.jetbrains.annotations.NotNull;

public record ServerboundSetSlotPacket(short slotNum, ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundSetSlotPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "set_any_slot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetSlotPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT,
            ServerboundSetSlotPacket::slotNum,
            ItemStack.validatedStreamCodec(ItemStack.OPTIONAL_STREAM_CODEC),
            ServerboundSetSlotPacket::stack,
            ServerboundSetSlotPacket::new
    );

    public ServerboundSetSlotPacket(int slotNum, ItemStack stack) {
        this((short) slotNum, stack);
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundSetSlotPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack itemstack = packet.stack();
            if (!itemstack.isItemEnabled(player.level().enabledFeatures())) {
                return;
            }

            CustomData customdata = itemstack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
            if (customdata.contains("x") && customdata.contains("y") && customdata.contains("z")) {
                BlockPos blockpos = BlockEntity.getPosFromTag(customdata.getUnsafe());
                if (player.level().isLoaded(blockpos)) {
                    BlockEntity blockentity = player.level().getBlockEntity(blockpos);
                    if (blockentity != null) {
                        blockentity.saveToItem(itemstack, player.level().registryAccess());
                    }
                }
            }

            boolean flag1 = packet.slotNum() >= 1 && packet.slotNum() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getCount() <= itemstack.getMaxStackSize();
            if (flag1 && flag2) {
                player.inventoryMenu.getSlot(packet.slotNum()).setByPlayer(itemstack);
                player.inventoryMenu.broadcastChanges();
            }
        });
    }
}
