package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliAltar;
import net.xuyifei.lolipickaxe.common.registry.custom.LoliBuffAttackTNT;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, LoliPickaxe.MODID);

    public static final Supplier<Block> LOLI_ALTAR = BLOCKS.register("loli_altar", () -> new LoliAltar(BlockBehaviour.Properties.of()));

    public static final Supplier<Block> LOLI_BLUE_SCREEN_TNT = BLOCKS.register("loli_blue_screen_tnt", () -> new LoliBuffAttackTNT(BlockBehaviour.Properties.of(), true, false, false));

    public static final Supplier<Block> LOLI_EXIT_TNT = BLOCKS.register("loli_exit_tnt", () -> new LoliBuffAttackTNT(BlockBehaviour.Properties.of(), false, true, false));

    public static final Supplier<Block> LOLI_FAIL_RESPOND_TNT = BLOCKS.register("loli_fail_respond_tnt", () -> new LoliBuffAttackTNT(BlockBehaviour.Properties.of(), false, false, true));

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
