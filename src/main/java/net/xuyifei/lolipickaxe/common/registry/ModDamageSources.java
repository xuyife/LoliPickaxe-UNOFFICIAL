package net.xuyifei.lolipickaxe.common.registry;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;


public class ModDamageSources {
    private final RegistryAccess registryAccess;

    public ModDamageSources(RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }

    public DamageSource loliPickaxe(Entity attacker) {
        return new DamageSource(
                registryAccess.registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ModDamageTypes.LOLI_PICKAXE),
                attacker
        );
    }

    public static DamageSource createLoliDamage(Level level, Entity attacker) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ModDamageTypes.LOLI_PICKAXE),
                attacker
        );
    }

    public static DamageSource createLoliDamage(Level level, Entity directEntity, Entity causingEntity) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ModDamageTypes.LOLI_PICKAXE),
                directEntity,
                causingEntity
        );
    }
}
