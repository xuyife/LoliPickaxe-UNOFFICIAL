package net.xuyifei.lolipickaxe;

import net.neoforged.fml.config.ModConfig;
import net.xuyifei.lolipickaxe.common.entity.ModEntities;
import net.xuyifei.lolipickaxe.common.menu.ModMenuTypes;
import net.xuyifei.lolipickaxe.common.registry.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(LoliPickaxe.MODID)
public class LoliPickaxe {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "lolipickaxe";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LoliPickaxe(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC, "lolipickaxe.toml");

        ModItems.register(modEventBus);

        ModSounds.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModDataComponents.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModEntities.register(modEventBus);
    }
}
