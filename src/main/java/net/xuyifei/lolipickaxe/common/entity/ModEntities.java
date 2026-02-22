package net.xuyifei.lolipickaxe.common.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.entity.render.EntityLoliRenderer;
import net.xuyifei.lolipickaxe.common.entity.render.ModelLoli;
import net.xuyifei.lolipickaxe.common.entity.render.PrimedLoliBuffAttackTNTEntityRenderer;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, LoliPickaxe.MODID);

    public static final Supplier<EntityType<EntityLoli>> LOLI_ENTITY = ENTITIES.register("loli_entity", () -> EntityType.Builder.of(EntityLoli::new, MobCategory.MONSTER)
            .sized(0.6F, 1.8F)
            .build("loli_entity"));

    public static final Supplier<EntityType<PrimedLoliBuffAttackTNTEntity>> LOLI_BUFF_ATTACK_TNT = ENTITIES.register("loli_buff_attack_tnt", () -> EntityType.Builder.<PrimedLoliBuffAttackTNTEntity>of(PrimedLoliBuffAttackTNTEntity::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10).build("loli_buff_attack_tnt"));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);

        bus.addListener(ModEntities::registerAttributes);
        bus.addListener(ModEntities::registerLayerDefinitions);
        bus.addListener(ModEntities::onClientSetup);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(LOLI_ENTITY.get(), EntityLoli.createAttributes().build());
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLoli.LAYER_LOCATION, ModelLoli::createBodyLayer);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.LOLI_ENTITY.get(), EntityLoliRenderer::new);
            EntityRenderers.register(ModEntities.LOLI_BUFF_ATTACK_TNT.get(), PrimedLoliBuffAttackTNTEntityRenderer::new);
        });
    }
}
