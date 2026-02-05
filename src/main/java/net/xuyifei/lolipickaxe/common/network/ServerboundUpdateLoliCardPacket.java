package net.xuyifei.lolipickaxe.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliCard;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliCardAlbum;
import org.jetbrains.annotations.NotNull;

public record ServerboundUpdateLoliCardPacket(int slot, ItemType itemType, String name) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundUpdateLoliCardPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "update_loli_card"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundUpdateLoliCardPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ServerboundUpdateLoliCardPacket::slot,
            ByteBufCodecs.INT.map(ItemType::fromId, ItemType::getId), ServerboundUpdateLoliCardPacket::itemType,
            ByteBufCodecs.STRING_UTF8, ServerboundUpdateLoliCardPacket::name,
            ServerboundUpdateLoliCardPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundUpdateLoliCardPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            Inventory inventory = player.getInventory();

            if (packet.slot >= 0 && packet.slot < inventory.getContainerSize()) {
                ItemStack stack = inventory.getItem(packet.slot);

                switch (packet.itemType) {
                    case LOLICARD -> {
                        if (stack.getItem() instanceof LoliCard) {
                            CompoundTag customData = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag();
                            if (!customData.contains("picture")) {
                                customData.putString("picture", packet.name);
                                stack.set(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.of(customData));
                            }
                        }
                    }
                    case LOLICARDALBUM -> {
                        if (stack.getItem() instanceof LoliCardAlbum) {
                            CompoundTag customData = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag();
                            if (!customData.contains("PictureGroup")) {
                                customData.putString("PictureGroup", packet.name);
                                stack.set(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.of(customData));
                            }
                        }
                    }
                }
            }
        });
    }

    public enum ItemType {
        LOLICARD(0),
        LOLICARDALBUM(1);

        private final int id;

        ItemType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ItemType fromId(int id) {
            return switch (id) {
                case 0 -> LOLICARD;
                case 1 -> LOLICARDALBUM;
                default -> throw new IllegalArgumentException("Unknown item itemType id: " + id);
            };
        }
    }
}
