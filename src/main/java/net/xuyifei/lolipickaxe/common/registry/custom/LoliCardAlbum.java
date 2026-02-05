package net.xuyifei.lolipickaxe.common.registry.custom;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.xuyifei.lolipickaxe.client.gui.GUILoliCardAlbum;
import net.xuyifei.lolipickaxe.common.network.ServerboundUpdateLoliCardPacket;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.util.LoliCardUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class LoliCardAlbum extends Item {
    public LoliCardAlbum(Properties properties) {
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
            String groupName = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag().getString("PictureGroup");
            if (!groupName.isEmpty() && LoliCardUtil.getCustomArtNames() != null) {
                String name = groupName + "__";
                List<ResourceLocation> resources = Lists.newArrayList();
                List<Integer> widths = Lists.newArrayList();
                List<Integer> heights = Lists.newArrayList();
                for (int i = 0; i < LoliCardUtil.getCustomArtNames().size(); i++) {
                    if (LoliCardUtil.getCustomArtNames().get(i).startsWith(name)) {
                        resources.add(LoliCardUtil.getCustomArtResources().get(i));
                        widths.add(LoliCardUtil.getCustomArtWidths().get(i));
                        heights.add(LoliCardUtil.getCustomArtHeights().get(i));
                    }
                }
                if (!resources.isEmpty()) {
                    Minecraft.getInstance().setScreen(new GUILoliCardAlbum(groupName, resources, widths, heights));
                }
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide() && LoliCardUtil.getCustomArtNames() != null && !LoliCardUtil.getCustomArtNames().isEmpty() && !hasPictureGroupData(stack)) {
            List<String> groups = Lists.newArrayList();
            for (String name : LoliCardUtil.getCustomArtNames()) {
                int index = name.indexOf("__");
                if (index != -1) {
                    String group = name.substring(0, index);
                    if (!groups.contains(group)) {
                        groups.add(group);
                    }
                }
            }

            if (!groups.isEmpty()) {
                String randomGroup = groups.get(level.random.nextInt(groups.size()));
                if (entity instanceof LocalPlayer player) {
                    player.connection.send(new ServerboundUpdateLoliCardPacket(slotId, ServerboundUpdateLoliCardPacket.ItemType.LOLICARDALBUM, randomGroup));
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        getPictureGroup(stack).ifPresent(group -> tooltipComponents.add(Component.literal(group)));
    }

    private boolean hasPictureGroupData(ItemStack stack) {
        return getPictureGroup(stack).isPresent();
    }

    private Optional<String> getPictureGroup(ItemStack stack) {
        CompoundTag customData = stack.getOrDefault(ModDataComponents.LOLI_CARD_DATA.get(), CustomData.EMPTY).copyTag();
        if (customData.contains("PictureGroup")) {
            return Optional.of(customData.getString("PictureGroup"));
        }
        return Optional.empty();
    }
}
