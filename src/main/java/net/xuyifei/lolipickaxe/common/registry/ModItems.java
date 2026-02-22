package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.entity.ModEntities;
import net.xuyifei.lolipickaxe.common.registry.custom.*;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, LoliPickaxe.MODID);

    public static final Supplier<Item> LOLI_PICKAXE = ITEMS.register("loli_pickaxe", () -> new net.xuyifei.lolipickaxe.common.registry.custom.LoliPickaxe(new Item.Properties()));

    public static final Supplier<Item> SMALL_LOLI_PICKAXE = ITEMS.register("small_loli_pickaxe", () -> new SmallLoliPickaxe(new Item.Properties()));


    public static final Supplier<Item> LOLI_COAL_ADDON_0 = ITEMS.register("loli_coal_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 0, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_1 = ITEMS.register("loli_coal_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 1, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_2 = ITEMS.register("loli_coal_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 2, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_3 = ITEMS.register("loli_coal_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 3, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_4 = ITEMS.register("loli_coal_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 4, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_5 = ITEMS.register("loli_coal_addon_5", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 5, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_6 = ITEMS.register("loli_coal_addon_6", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 6, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_7 = ITEMS.register("loli_coal_addon_7", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 7, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_8 = ITEMS.register("loli_coal_addon_8", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 8, "loli_coal_addon"));
    public static final Supplier<Item> LOLI_COAL_ADDON_MAX = ITEMS.register("loli_coal_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 9, "loli_coal_addon"));

    public static final Supplier<Item> LOLI_IRON_ADDON_0 = ITEMS.register("loli_iron_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 0, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_1 = ITEMS.register("loli_iron_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 1, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_2 = ITEMS.register("loli_iron_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 2, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_3 = ITEMS.register("loli_iron_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 3, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_4 = ITEMS.register("loli_iron_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 4, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_5 = ITEMS.register("loli_iron_addon_5", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 5, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_6 = ITEMS.register("loli_iron_addon_6", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 6, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_7 = ITEMS.register("loli_iron_addon_7", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 7, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_8 = ITEMS.register("loli_iron_addon_8", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 8, "loli_iron_addon"));
    public static final Supplier<Item> LOLI_IRON_ADDON_MAX = ITEMS.register("loli_iron_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 9, "loli_iron_addon"));

    public static final Supplier<Item> LOLI_GOLD_ADDON_0 = ITEMS.register("loli_gold_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 0, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_1 = ITEMS.register("loli_gold_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 1, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_2 = ITEMS.register("loli_gold_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 2, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_3 = ITEMS.register("loli_gold_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 3, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_4 = ITEMS.register("loli_gold_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 4, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_5 = ITEMS.register("loli_gold_addon_5", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 5, "loli_gold_addon"));
    public static final Supplier<Item> LOLI_GOLD_ADDON_MAX = ITEMS.register("loli_gold_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 6, "loli_gold_addon"));

    public static final Supplier<Item> LOLI_REDSTONE_ADDON_0 = ITEMS.register("loli_redstone_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 4, 0, "loli_redstone_addon"));
    public static final Supplier<Item> LOLI_REDSTONE_ADDON_1 = ITEMS.register("loli_redstone_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 4, 1, "loli_redstone_addon"));
    public static final Supplier<Item> LOLI_REDSTONE_ADDON_2 = ITEMS.register("loli_redstone_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 4, 2, "loli_redstone_addon"));
    public static final Supplier<Item> LOLI_REDSTONE_ADDON_MAX = ITEMS.register("loli_redstone_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 4, 3, "loli_redstone_addon"));

    public static final Supplier<Item> LOLI_LAPIS_ADDON_0 = ITEMS.register("loli_lapis_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 0, "loli_lapis_addon"));
    public static final Supplier<Item> LOLI_LAPIS_ADDON_1 = ITEMS.register("loli_lapis_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 1, "loli_lapis_addon"));
    public static final Supplier<Item> LOLI_LAPIS_ADDON_2 = ITEMS.register("loli_lapis_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 2, "loli_lapis_addon"));
    public static final Supplier<Item> LOLI_LAPIS_ADDON_3 = ITEMS.register("loli_lapis_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 3, "loli_lapis_addon"));
    public static final Supplier<Item> LOLI_LAPIS_ADDON_4 = ITEMS.register("loli_lapis_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 4, "loli_lapis_addon"));
    public static final Supplier<Item> LOLI_LAPIS_ADDON_MAX = ITEMS.register("loli_lapis_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 5, "loli_lapis_addon"));

    public static final Supplier<Item> LOLI_DIAMOND_ADDON_0 = ITEMS.register("loli_diamond_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 0, "loli_diamond_addon"));
    public static final Supplier<Item> LOLI_DIAMOND_ADDON_1 = ITEMS.register("loli_diamond_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 1, "loli_diamond_addon"));
    public static final Supplier<Item> LOLI_DIAMOND_ADDON_2 = ITEMS.register("loli_diamond_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 2, "loli_diamond_addon"));
    public static final Supplier<Item> LOLI_DIAMOND_ADDON_3 = ITEMS.register("loli_diamond_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 3, "loli_diamond_addon"));
    public static final Supplier<Item> LOLI_DIAMOND_ADDON_4 = ITEMS.register("loli_diamond_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 4, "loli_diamond_addon"));
    public static final Supplier<Item> LOLI_DIAMOND_ADDON_MAX = ITEMS.register("loli_diamond_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 6, 5, "loli_diamond_addon"));

    public static final Supplier<Item> LOLI_EMERALD_ADDON_0 = ITEMS.register("loli_emerald_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 0, "loli_emerald_addon"));
    public static final Supplier<Item> LOLI_EMERALD_ADDON_1 = ITEMS.register("loli_emerald_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 1, "loli_emerald_addon"));
    public static final Supplier<Item> LOLI_EMERALD_ADDON_2 = ITEMS.register("loli_emerald_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 2, "loli_emerald_addon"));
    public static final Supplier<Item> LOLI_EMERALD_ADDON_3 = ITEMS.register("loli_emerald_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 3, "loli_emerald_addon"));
    public static final Supplier<Item> LOLI_EMERALD_ADDON_MAX = ITEMS.register("loli_emerald_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 4, "loli_emerald_addon"));

    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_0 = ITEMS.register("loli_entity_soul_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 0, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_1 = ITEMS.register("loli_entity_soul_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 1, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_2 = ITEMS.register("loli_entity_soul_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 2, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_3 = ITEMS.register("loli_entity_soul_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 3, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_4 = ITEMS.register("loli_entity_soul_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 4, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_5 = ITEMS.register("loli_entity_soul_addon_5", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 5, "loli_entity_soul_addon"));
    public static final Supplier<Item> LOLI_ENTITY_SOUL_ADDON_MAX = ITEMS.register("loli_entity_soul_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 7, 6, "loli_entity_soul_addon"));

    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_0 = ITEMS.register("loli_obsidian_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 0, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_1 = ITEMS.register("loli_obsidian_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 1, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_2 = ITEMS.register("loli_obsidian_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 2, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_3 = ITEMS.register("loli_obsidian_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 3, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_4 = ITEMS.register("loli_obsidian_addon_4", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 4, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_5 = ITEMS.register("loli_obsidian_addon_5", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 5, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_6 = ITEMS.register("loli_obsidian_addon_6", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 6, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_7 = ITEMS.register("loli_obsidian_addon_7", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 7, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_8 = ITEMS.register("loli_obsidian_addon_8", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 8, "loli_obsidian_addon"));
    public static final Supplier<Item> LOLI_OBSIDIAN_ADDON_MAX = ITEMS.register("loli_obsidian_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 10, 9, "loli_obsidian_addon"));

    public static final Supplier<Item> LOLI_GLOW_ADDON_0 = ITEMS.register("loli_glow_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 0, "loli_glow_addon"));
    public static final Supplier<Item> LOLI_GLOW_ADDON_1 = ITEMS.register("loli_glow_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 1, "loli_glow_addon"));
    public static final Supplier<Item> LOLI_GLOW_ADDON_MAX = ITEMS.register("loli_glow_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 2, "loli_glow_addon"));

    public static final Supplier<Item> LOLI_QUARTZ_ADDON_0 = ITEMS.register("loli_quartz_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 0, "loli_quartz_addon"));
    public static final Supplier<Item> LOLI_QUARTZ_ADDON_1 = ITEMS.register("loli_quartz_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 1, "loli_quartz_addon"));
    public static final Supplier<Item> LOLI_QUARTZ_ADDON_MAX = ITEMS.register("loli_quartz_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 3, 2, "loli_quartz_addon"));

    public static final Supplier<Item> LOLI_NETHER_STAR_ADDON_0 = ITEMS.register("loli_nether_star_addon_0", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 0, "loli_nether_star_addon"));
    public static final Supplier<Item> LOLI_NETHER_STAR_ADDON_1 = ITEMS.register("loli_nether_star_addon_1", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 1, "loli_nether_star_addon"));
    public static final Supplier<Item> LOLI_NETHER_STAR_ADDON_2 = ITEMS.register("loli_nether_star_addon_2", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 2, "loli_nether_star_addon"));
    public static final Supplier<Item> LOLI_NETHER_STAR_ADDON_3 = ITEMS.register("loli_nether_star_addon_3", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 3, "loli_nether_star_addon"));
    public static final Supplier<Item> LOLI_NETHER_STAR_ADDON_MAX = ITEMS.register("loli_nether_star_addon_max", () -> new LoliPickaxeMaterial(new Item.Properties(), 5, 4, "loli_nether_star_addon"));

    public static final Supplier<Item> LOLI_AUTO_FURNACE_ADDON = ITEMS.register("loli_auto_furnace_addon", () -> new LoliPickaxeMaterial(new Item.Properties(), 1, 0, "loli_auto_furnace_addon"));

    public static final Supplier<Item> LOLI_FLY_ADDON = ITEMS.register("loli_fly_addon", () -> new LoliPickaxeMaterial(new Item.Properties(), 1, 0, "loli_fly_addon"));


    public static final Supplier<Item> LOLI_CARD = ITEMS.register("loli_card", () -> new LoliCard(new Item.Properties()));

    public static final Supplier<Item> LOLI_CARD_ALBUM = ITEMS.register("loli_card_album", () -> new LoliCardAlbum(new Item.Properties()));

    public static final Supplier<Item> LOLI_CARD_ONLINE = ITEMS.register("loli_card_online", () -> new LoliCardOnline(new Item.Properties()));


    public static final Supplier<Item> LOLI_RECORD = ITEMS.register("loli_record", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "lolirecord")))));


    public static final Supplier<Item> LOLI_ALTAR = ITEMS.register("loli_altar", () -> new BlockItem(ModBlocks.LOLI_ALTAR.get(), new Item.Properties()));


    public static final Supplier<Item> LOLI_DISPERSAL = ITEMS.register("loli_dispersal", () -> new LoliDispersal(new Item.Properties()));


    public static final Supplier<Item> LOLI_BLUE_SCREEN_TNT = ITEMS.register("loli_blue_screen_tnt", () -> new BlockItem(ModBlocks.LOLI_BLUE_SCREEN_TNT.get(), new Item.Properties()));

    public static final Supplier<Item> LOLI_EXIT_TNT = ITEMS.register("loli_exit_tnt", () -> new BlockItem(ModBlocks.LOLI_EXIT_TNT.get(), new Item.Properties()));

    public static final Supplier<Item> LOLI_FAIL_RESPOND_TNT = ITEMS.register("loli_fail_respond_tnt", () -> new BlockItem(ModBlocks.LOLI_FAIL_RESPOND_TNT.get(), new Item.Properties()));


    public static final Supplier<Item> LOLI_SPAWN_EGG = ITEMS.register("loli_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.LOLI_ENTITY, 0xFFFFFF, 0x000000, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
