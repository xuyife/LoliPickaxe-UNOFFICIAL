package net.xuyifei.lolipickaxe.common.registry.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.xuyifei.lolipickaxe.client.gui.GUILoliCardOnline;
import net.xuyifei.lolipickaxe.client.gui.GUILoliCardOnlineConfig;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoliCardOnline extends Item {
    public LoliCardOnline(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()) {
            Minecraft.getInstance().setScreen(new GUILoliCardOnlineConfig(stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag().getString("ImageUrl")));
        } else {
            Minecraft.getInstance().setScreen(new GUILoliCardOnline(stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag().getString("ImageUrl")));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).contains("ImageUrl")) {
            tooltipComponents.add(Component.literal(stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag().getString("ImageUrl")));
        }
    }
}
