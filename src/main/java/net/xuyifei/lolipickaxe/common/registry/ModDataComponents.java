package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, LoliPickaxe.MODID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CustomData>> LOLI_CONFIG = register(
            "loli_config", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> LOLI_ADDON_LEVEL = register(
            "loli_addon_level", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CustomData>> SMALL_LOLI_ATTRIBUTE = register(
            "small_loli_attribute", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CustomData>> LOLI_INVENTORY_DATA = register(
            "loli_inventory_data", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CustomData>> LOLI_BLACK_LIST_DATA = register(
            "loli_black_list_data", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CustomData>> LOLI_CARD_DATA = register(
            "loli_card_data", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> LOLI_MINING_RANGE = register(
            "loli_mining_range", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    private static <T> DeferredHolder<DataComponentType<?>,DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(name,()->  builder.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus){
        DATA_COMPONENTS.register(bus);
    }
}
