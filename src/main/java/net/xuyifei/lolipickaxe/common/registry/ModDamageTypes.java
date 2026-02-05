package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.xuyifei.lolipickaxe.LoliPickaxe;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> LOLI_PICKAXE =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "lolipickaxe"));
}
