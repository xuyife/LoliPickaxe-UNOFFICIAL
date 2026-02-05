package net.xuyifei.lolipickaxe.common.registry.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;
import org.jetbrains.annotations.NotNull;

public class LoliDispersal extends Item {
    public LoliDispersal(Properties properties) {
        super(properties
                .stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (interactionTarget instanceof EntityLoli entityLoli) {
            if (!player.level().isClientSide()) {
                player.swing(usedHand);
                entityLoli.setDispersal(true);
                interactionTarget.discard();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
