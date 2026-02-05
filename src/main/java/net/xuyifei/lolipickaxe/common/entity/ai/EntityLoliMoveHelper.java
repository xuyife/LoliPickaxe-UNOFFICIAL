package net.xuyifei.lolipickaxe.common.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;

public class EntityLoliMoveHelper extends MoveControl {
    private final EntityLoli loli;

    public EntityLoliMoveHelper(EntityLoli loli) {
        super(loli);
        this.loli = loli;
    }

    @Override
    public void tick() {
        LivingEntity target = loli.getTarget();
        if (target != null && target.isInWater() && loli.isInWater()) {
            if (this.operation != Operation.MOVE_TO || loli.getNavigation().isDone()) {
                loli.setSpeed(0.0F);
                return;
            }

            double dx = this.wantedX - loli.getX();
            double dy = this.wantedY - loli.getY();
            double dz = this.wantedZ - loli.getZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (d < 1.0E-5) {
                loli.setSpeed(0.0F);
                return;
            }

            dy = dy / d;
            float targetYRot = (float)(Mth.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
            loli.setYRot(this.rotlerp(loli.getYRot(), targetYRot, 90.0F));
            loli.yBodyRot = loli.getYRot();

            float moveSpeed = (float)(this.speedModifier * loli.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float currentSpeed = loli.getSpeed();
            float newSpeed = currentSpeed + (moveSpeed - currentSpeed) * 0.125F;
            loli.setSpeed(newSpeed);

            Vec3 deltaMovement = loli.getDeltaMovement();
            double motionY = deltaMovement.y() + (double)newSpeed * dy * 0.4D;
            double motionX = deltaMovement.x() + (double)newSpeed * dx * 0.02D;
            double motionZ = deltaMovement.z() + (double)newSpeed * dz * 0.02D;

            loli.setDeltaMovement(motionX, motionY, motionZ);
        } else {
            super.tick();
        }
    }
}
