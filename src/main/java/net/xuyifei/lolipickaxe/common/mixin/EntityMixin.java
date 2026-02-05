package net.xuyifei.lolipickaxe.common.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void onIsInvisibleTo(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack loli = LoliPickaxeUtil.getLoliPickaxe(player);
        if (!loli.isEmpty() && ConfigLoader.getBoolean(loli, "loliPickaxeShowInvisible")) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
