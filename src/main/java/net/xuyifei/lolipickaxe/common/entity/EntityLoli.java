package net.xuyifei.lolipickaxe.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.xuyifei.lolipickaxe.common.entity.ai.EntityAILoliAttack;
import net.xuyifei.lolipickaxe.common.entity.ai.EntityAILoliSwimming;
import net.xuyifei.lolipickaxe.common.entity.ai.EntityLoliMoveHelper;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModDamageSources;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.jetbrains.annotations.NotNull;

public class EntityLoli extends PathfinderMob implements IEntityLoli {
    private static final EntityDataAccessor<Boolean> DISPERSAL = SynchedEntityData.defineId(EntityLoli.class, EntityDataSerializers.BOOLEAN);

    public boolean dimChangeing;

    public EntityLoli(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new EntityLoliMoveHelper(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.dimChangeing = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.SUBMERGED_MINING_SPEED, 1.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ATTACK_DAMAGE, Float.MAX_VALUE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DISPERSAL, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EntityAILoliAttack(this));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));

        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 16.0F));

        this.goalSelector.addGoal(6, new EntityAILoliSwimming(this));

        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                (target) -> {
                    if (target == null) return false;
                    if (target == this) return false;
                    if (target.getType() == ModEntities.LOLI_ENTITY.get()) return false;
                    if (!target.isAlive()) return false;
                    return LoliPickaxeUtil.getLoliPickaxe(target).isEmpty();
                }
        ));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        if (entity instanceof LivingEntity le) {
            if (!LoliPickaxeUtil.getLoliPickaxe(le).isEmpty()) return false;

            if (!this.level().isClientSide()) {
                ServerLevel level = (ServerLevel) this.level();
                level.getServer().execute(() -> {
                    le.hurt(ModDamageSources.createLoliDamage(this.level(), this), Float.MAX_VALUE);
                    le.remove(RemovalReason.KILLED);
                });
            }
        }
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (ModConfigs.INSTANCE != null) {
            try {
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(ModConfigs.INSTANCE.loliSpeed.get());
                this.getAttribute(Attributes.SUBMERGED_MINING_SPEED).setBaseValue(ModConfigs.INSTANCE.loliSpeed.get() * 15);
                this.getAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY).setBaseValue(ModConfigs.INSTANCE.loliSpeed.get() * 15);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater() && this.getTarget() != null && this.getTarget().isInWater()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return super.shouldDespawnInPeaceful();
    }

    @Override
    protected int getBaseExperienceReward() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    protected void handlePortal() {
        super.handlePortal();
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    @Override
    public void onRemovedFromLevel() {
        if (!this.isDispersal() && !this.level().isClientSide()) {
            EntityLoli loli = new EntityLoli(ModEntities.LOLI_ENTITY.get(), this.level());
            loli.copyPosition(this);
            this.level().addFreshEntity(loli);
            this.setDispersal(true);
        }
        super.onRemovedFromLevel();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Dispersal", this.isDispersal());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDispersal(compound.getBoolean("Dispersal"));
    }

    @Override
    public boolean isDispersal() {
        return this.entityData.get(DISPERSAL);
    }

    @Override
    public void setDispersal(boolean value) {
        this.entityData.set(DISPERSAL, value);
    }
}
