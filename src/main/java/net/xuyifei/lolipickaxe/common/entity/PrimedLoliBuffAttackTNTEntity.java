package net.xuyifei.lolipickaxe.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.xuyifei.lolipickaxe.common.network.ClientboundLoliDeadPacket;
import net.xuyifei.lolipickaxe.common.registry.ModBlocks;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PrimedLoliBuffAttackTNTEntity extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedLoliBuffAttackTNTEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedLoliBuffAttackTNTEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Boolean> DATA_IS_BLUE_SCREEN = SynchedEntityData.defineId(PrimedLoliBuffAttackTNTEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_EXIT = SynchedEntityData.defineId(PrimedLoliBuffAttackTNTEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_FAIL_RESPOND = SynchedEntityData.defineId(PrimedLoliBuffAttackTNTEntity.class, EntityDataSerializers.BOOLEAN);
    private static final int DEFAULT_FUSE_TIME = 80;
    private static final String TAG_BLOCK_STATE = "block_state";
    private static final String TAG_IS_BLUE_SCREEN = "is_blue_screen";
    private static final String TAG_IS_EXIT = "is_exit";
    private static final String TAG_IS_FAIL_RESPOND = "is_fail_respond";
    public static final String TAG_FUSE = "fuse";
    private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
        @Override
        public boolean shouldBlockExplode(@NotNull Explosion explosion, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, BlockState blockState, float power) {
            return !blockState.is(Blocks.NETHER_PORTAL) && super.shouldBlockExplode(explosion, blockGetter, blockPos, blockState, power);
        }

        @Override
        public @NotNull Optional<Float> getBlockExplosionResistance(
                @NotNull Explosion explosion, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, BlockState blockState, @NotNull FluidState fluidState
        ) {
            return blockState.is(Blocks.NETHER_PORTAL)
                    ? Optional.empty()
                    : super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState);
        }
    };

    @Nullable
    private LivingEntity owner;
    private boolean usedPortal;

    public PrimedLoliBuffAttackTNTEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public PrimedLoliBuffAttackTNTEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner, boolean isBlueScreen, boolean isExit, boolean isFailRespond) {
        this(ModEntities.LOLI_BUFF_ATTACK_TNT.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (float) (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;

        this.setIsBlueScreen(isBlueScreen);
        this.setIsExit(isExit);
        this.setIsFailRespond(isFailRespond);

        this.updateBlockState();
    }

    public void setFuse(int life) {
        this.entityData.set(DATA_FUSE_ID, life);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public void setBlockState(BlockState blockState) {
        this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    private void setUsedPortal(boolean usedPortal) {
        this.usedPortal = usedPortal;
    }

    public boolean isBlueScreen() {
        return this.entityData.get(DATA_IS_BLUE_SCREEN);
    }

    public void setIsBlueScreen(boolean isBlueScreen) {
        this.entityData.set(DATA_IS_BLUE_SCREEN, isBlueScreen);
    }

    public boolean isExit() {
        return this.entityData.get(DATA_IS_EXIT);
    }

    public void setIsExit(boolean isExit) {
        this.entityData.set(DATA_IS_EXIT, isExit);
    }

    public boolean isFailRespond() {
        return this.entityData.get(DATA_IS_FAIL_RESPOND);
    }

    public void setIsFailRespond(boolean isFailRespond) {
        this.entityData.set(DATA_IS_FAIL_RESPOND, isFailRespond);
    }

    private void updateBlockState() {
        if (this.isBlueScreen()) {
            this.setBlockState(ModBlocks.LOLI_BLUE_SCREEN_TNT.get().defaultBlockState());
        } else if (this.isExit()) {
            this.setBlockState(ModBlocks.LOLI_EXIT_TNT.get().defaultBlockState());
        } else if (this.isFailRespond()) {
            this.setBlockState(ModBlocks.LOLI_FAIL_RESPOND_TNT.get().defaultBlockState());
        } else {
            this.setBlockState(Blocks.TNT.defaultBlockState());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, DEFAULT_FUSE_TIME);
        builder.define(DATA_BLOCK_STATE_ID, Blocks.TNT.defaultBlockState());
        builder.define(DATA_IS_BLUE_SCREEN, false);
        builder.define(DATA_IS_EXIT, false);
        builder.define(DATA_IS_FAIL_RESPOND, false);
    }

    @Override
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level().isClientSide()) {
                this.explode();
                if (ModConfigs.INSTANCE.loliEnableBuffAttackTNT.get()) {
                    List<Player> playerList = this.level().getEntitiesOfClass(Player.class, new AABB(this.position().x - 5, this.position().y - 5, this.position().z - 5, this.position().x + 5, this.position().y + 5, this.position().z + 5));
                    for (Player player : playerList) {
                        ((ServerPlayer) player).connection.send(new ClientboundLoliDeadPacket(false, this.isBlueScreen(), this.isExit(), this.isFailRespond()));
                    }
                }
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    protected void explode() {
        float f = 4.0F;
        this.level()
                .explode(
                        this,
                        Explosion.getDefaultDamageSource(this.level(), this),
                        this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null,
                        this.getX(),
                        this.getY(0.0625),
                        this.getZ(),
                        f,
                        false,
                        Level.ExplosionInteraction.TNT
                );
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setFuse(compound.getShort(TAG_FUSE));
        if (compound.contains(TAG_BLOCK_STATE, 10)) {
            this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound(TAG_BLOCK_STATE)));
        }
        if (compound.contains(TAG_IS_BLUE_SCREEN)) {
            this.setIsBlueScreen(compound.getBoolean(TAG_IS_BLUE_SCREEN));
        }
        if (compound.contains(TAG_IS_EXIT)) {
            this.setIsExit(compound.getBoolean(TAG_IS_EXIT));
        }
        if (compound.contains(TAG_IS_FAIL_RESPOND)) {
            this.setIsFailRespond(compound.getBoolean(TAG_IS_FAIL_RESPOND));
        }

        this.updateBlockState();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort(TAG_FUSE, (short) this.getFuse());
        compound.put(TAG_BLOCK_STATE, NbtUtils.writeBlockState(this.getBlockState()));
        compound.putBoolean(TAG_IS_BLUE_SCREEN, this.isBlueScreen());
        compound.putBoolean(TAG_IS_EXIT, this.isExit());
        compound.putBoolean(TAG_IS_FAIL_RESPOND, this.isFailRespond());
    }

    @Override
    public @Nullable Entity getOwner() {
        return this.owner;
    }

    @Override
    public void restoreFrom(@NotNull Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof PrimedLoliBuffAttackTNTEntity primedLoliBuffAttackTNTEntity) {
            this.owner = primedLoliBuffAttackTNTEntity.owner;
            this.setIsBlueScreen(primedLoliBuffAttackTNTEntity.isBlueScreen());
            this.setIsExit(primedLoliBuffAttackTNTEntity.isExit());
            this.setIsFailRespond(primedLoliBuffAttackTNTEntity.isFailRespond());
            this.setBlockState(primedLoliBuffAttackTNTEntity.getBlockState());
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(@NotNull DimensionTransition transition) {
        Entity entity = super.changeDimension(transition);
        if (entity instanceof PrimedLoliBuffAttackTNTEntity primedLoliBuffAttackTNTEntity) {
            primedLoliBuffAttackTNTEntity.setUsedPortal(true);
        }

        return entity;
    }
}
