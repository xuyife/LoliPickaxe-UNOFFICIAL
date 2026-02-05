package net.xuyifei.lolipickaxe.common.entity.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.jetbrains.annotations.NotNull;

public class EntityAILoliAttack extends MeleeAttackGoal {
    private int attackDelay = 0;

    public EntityAILoliAttack(Mob mob) {
        super((PathfinderMob) mob, 1.0, false);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        } else if (!LoliPickaxeUtil.getLoliPickaxe(target).isEmpty()) {
            this.mob.setTarget(null);
            return false;
        } else {
            if (ModConfigs.INSTANCE.loliTeleport.get()) {
                this.mob.stopRiding();
                this.mob.moveTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
                return true;
            } else {
                return super.canUse();
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (target instanceof Player && target.isSpectator() || !LoliPickaxeUtil.getLoliPickaxe(target).isEmpty()) {
            return false;
        } else {
            return !this.mob.getNavigation().isDone();
        }
    }

    @Override
    public void stop() {
        LivingEntity target = this.mob.getTarget();
        if (target instanceof Player && target.isSpectator() || !LoliPickaxeUtil.getLoliPickaxe(target).isEmpty()) {
            this.mob.setTarget(null);
        }
        this.mob.getNavigation().stop();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (attackDelay > 0) {
            attackDelay--;
            return;
        }
        if (ModConfigs.INSTANCE.loliAttack.get()) {
            if (this.getTicksUntilNextAttack() <= 0) {
                double distanceSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                double attackReach = this.mob.getBbWidth() * 2.0F;
                double attackReachSqr = attackReach * attackReach + target.getBbWidth();

                if (distanceSqr <= attackReachSqr) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);

                    boolean attacked = this.mob.doHurtTarget(target);
                    if (attacked) {
                        attackDelay = 10;
                    }
                }
            }
        }
    }

    @Override
    protected int getAttackInterval() {
        return super.getAttackInterval();
    }
}
