package net.xuyifei.lolipickaxe.common.registry.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.xuyifei.lolipickaxe.common.entity.PrimedLoliBuffAttackTNTEntity;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class LoliBuffAttackTNT extends Block {
    public static final MapCodec<LoliBuffAttackTNT> CODEC = simpleCodec(LoliBuffAttackTNT::new);
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
    public static final BooleanProperty BLUE_SCREEN = BooleanProperty.create("blue_screen");
    public static final BooleanProperty EXIT = BooleanProperty.create("exit");
    public static final BooleanProperty FAIL_RESPOND = BooleanProperty.create("fail_respond");

    private final boolean blueScreen;
    private final boolean exit;
    private final boolean failRespond;

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public LoliBuffAttackTNT(Properties properties) {
        super(properties
                .sound(SoundType.GRASS));
        this.blueScreen = false;
        this.exit = false;
        this.failRespond = false;
        this.defaultBlockState().setValue(BLUE_SCREEN, false).setValue(EXIT, false).setValue(FAIL_RESPOND, false).setValue(UNSTABLE, false);
    }

    public LoliBuffAttackTNT(Properties properties, boolean isBlueScreen, boolean isExit, boolean isFailRespond) {
        super(properties
                .sound(SoundType.GRASS));
        this.blueScreen = isBlueScreen;
        this.exit = isExit;
        this.failRespond = isFailRespond;
        this.defaultBlockState().setValue(BLUE_SCREEN, isBlueScreen).setValue(EXIT, isExit).setValue(FAIL_RESPOND, isFailRespond).setValue(UNSTABLE, false);
    }

    @Override
    public void onCaughtFire(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        explode(level, pos, igniter);
    }

    @Override
    protected void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos)) {
                onCaughtFire(state, level, pos, null, null);
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            onCaughtFire(state, level, pos, null, null);
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void wasExploded(Level level, @NotNull BlockPos pos, @NotNull Explosion explosion) {
        if (!level.isClientSide) {
            PrimedLoliBuffAttackTNTEntity primedLoliBuffAttackTNTEntity = new PrimedLoliBuffAttackTNTEntity(
                    level, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, explosion.getIndirectSourceEntity(), this.blueScreen, this.exit, this.failRespond
            );
            int i = primedLoliBuffAttackTNTEntity.getFuse();
            primedLoliBuffAttackTNTEntity.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedLoliBuffAttackTNTEntity);
        }
    }

    private void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {
        if (!level.isClientSide()) {
            PrimedLoliBuffAttackTNTEntity primedLoliBuffAttackTNTEntity = new PrimedLoliBuffAttackTNTEntity(level, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, entity, this.blueScreen, this.exit, this.failRespond);
            level.addFreshEntity(primedLoliBuffAttackTNTEntity);
            level.playSound(null, primedLoliBuffAttackTNTEntity.getX(), primedLoliBuffAttackTNTEntity.getY(), primedLoliBuffAttackTNTEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        } else {
            onCaughtFire(state, level, pos, hitResult.getDirection(), player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            Item item = stack.getItem();
            if (stack.is(Items.FLINT_AND_STEEL)) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
                stack.consume(1, player);
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    protected void onProjectileHit(Level level, @NotNull BlockState state, @NotNull BlockHitResult hit, @NotNull Projectile projectile) {
        if (!level.isClientSide()) {
            BlockPos blockpos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(level, blockpos)) {
                onCaughtFire(state, level, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                level.removeBlock(blockpos, false);
            }
        }
    }

    @Deprecated
    @Override
    public boolean dropFromExplosion(@NotNull Explosion explosion) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE, BLUE_SCREEN, EXIT, FAIL_RESPOND);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (ModConfigs.INSTANCE.loliEnableBuffAttackTNT.get()) {
            tooltipComponents.add(Component.translatable("buffAttackTNT.enable").withStyle(ChatFormatting.GREEN));
        } else {
            tooltipComponents.add(Component.translatable("buffAttackTNT.disable").withStyle(ChatFormatting.RED));
        }
    }
}