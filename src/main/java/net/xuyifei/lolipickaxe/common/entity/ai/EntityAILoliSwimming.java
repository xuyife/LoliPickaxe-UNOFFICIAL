package net.xuyifei.lolipickaxe.common.entity.ai;

import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;

public class EntityAILoliSwimming extends FloatGoal {
    private final EntityLoli loli;
    private boolean obstructed;

    public EntityAILoliSwimming(EntityLoli loli) {
        super(loli);
        this.loli = loli;
        this.obstructed = false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && !this.obstructed;
    }

    @Override
    public void tick() {
        if (loli.getNavigation().isDone() && loli.getTarget() == null) {
            super.tick();
        }
    }

    @Override
    public void start() {
        obstructed = false;
        super.start();
    }
}
