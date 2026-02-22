package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, LoliPickaxe.MODID);

    public static final Supplier<SoundEvent> LOLI_SUCCESS = register("loli_success", () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "loli_success"),16));
    public static final Supplier<SoundEvent> LOLIRECORD = register("music_disc.lolirecord", () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "music_disc.lolirecord"),16));

    public static DeferredHolder<SoundEvent,SoundEvent> register(String name, Supplier<SoundEvent> supplier){
        return SOUNDS.register(name, supplier);
    }

    public static void register(IEventBus modBus){
        SOUNDS.register(modBus);
    }
}
