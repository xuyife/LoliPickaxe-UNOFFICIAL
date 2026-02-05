package net.xuyifei.lolipickaxe.common.registry.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.xuyifei.lolipickaxe.client.gui.GUILoliCard;
import net.xuyifei.lolipickaxe.common.network.ServerboundUpdateLoliCardPacket;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.util.LoliCardUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoliCard extends Item {
    public LoliCard(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return stack.has(ModDataComponents.LOLI_CARD_DATA.get()) ? 64 : 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) {
            String name = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag().getString("picture");
            if (!name.isEmpty() && LoliCardUtil.getCustomArtNames() != null) {
                for (int i = 0; i < LoliCardUtil.getCustomArtNames().size(); i++) {
                    if (LoliCardUtil.getCustomArtNames().get(i).equals(name)) {
                        Minecraft.getInstance().setScreen(new GUILoliCard(name, LoliCardUtil.getCustomArtResources().get(i), LoliCardUtil.getCustomArtWidths().get(i), LoliCardUtil.getCustomArtHeights().get(i)));
                    }
                }
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide() && LoliCardUtil.getCustomArtNames() != null && !LoliCardUtil.getCustomArtNames().isEmpty() && (!stack.has(ModDataComponents.LOLI_CARD_DATA.get()) || !stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).contains("picture"))) {
            List<String> accessNames = LoliCardUtil.getCustomArtNames().stream()
                    .filter(name -> !name.contains("''"))
                    .toList();

            if (!accessNames.isEmpty()) {
                String randomName = accessNames.get(level.random.nextInt(accessNames.size()));
                if (entity instanceof LocalPlayer player) {
                    player.connection.send(new ServerboundUpdateLoliCardPacket(slotId, ServerboundUpdateLoliCardPacket.ItemType.LOLICARD, randomName));
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        CompoundTag tag = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag();
        if (tag.contains("picture")) {
            tooltipComponents.add(Component.literal(tag.getString("picture")));
        }
    }
}
