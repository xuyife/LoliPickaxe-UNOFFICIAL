package net.xuyifei.lolipickaxe.common.registry.custom;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.xuyifei.lolipickaxe.common.config.ConfigLoader;
import net.xuyifei.lolipickaxe.common.gui.ILoliInventory;
import net.xuyifei.lolipickaxe.common.gui.InventoryLoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import net.xuyifei.lolipickaxe.common.util.CommonUtil;
import net.xuyifei.lolipickaxe.common.util.LoliPickaxeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LoliPickaxe extends Item implements IContainer {

    public LoliPickaxe(Properties properties) {
        super(
                properties
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .fireResistant()
                        .component(ModDataComponents.LOLI_CONFIG.get(), CustomData.EMPTY)
        );
    }

    public LoliPickaxe() {
        this(new Properties());
    }

    public void onLeftClick(Level level, Player player, BlockPos clickedPos) {
        if (!level.isClientSide()) {
            CommonUtil.playLoliSuccessSound(player);
            destroyBlock(level, clickedPos, player);
        }
    }

    private void destroyBlock(Level level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        ItemStack tool = player.getMainHandItem();
        int range = Math.clamp(tool.getOrDefault(ModDataComponents.LOLI_MINING_RANGE.get(), 1), 0, ModConfigs.INSTANCE.loliPickaxeMaxRange.get());
        boolean mandatoryDrop = ConfigLoader.getBoolean(tool, "loliPickaxeMandatoryDrop");
        Registry<Enchantment> enchantmentRegistry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        int fortuneLevel = tool.getEnchantmentLevel(enchantmentRegistry.getHolder(Enchantments.FORTUNE).orElseThrow());
        boolean autoFurnace = tool.getEnchantmentLevel(enchantmentRegistry.getHolder(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(net.xuyifei.lolipickaxe.LoliPickaxe.MODID, "loli_auto_furnace"))).orElseThrow()) > 0;
        boolean auto = ConfigLoader.getBoolean(tool, "loliPickaxeAutoAccept");
        ILoliInventory inventory = null;
        if (auto) {
            inventory = ((IContainer) tool.getItem()).getInventory(tool, player.level().registryAccess());
            inventory.startOpen(player);
        }

        NonNullList<ItemStack> drops = NonNullList.create();
        float exp = 0;

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    BlockPos curPos = pos.offset(i, j, k);
                    if (ModConfigs.INSTANCE.loliPickaxeTriggerBreakEvent.get() && NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player)).isCanceled()) {
                        continue;
                    }
                    BlockState curState = level.getBlockState(curPos);
                    Block block = curState.getBlock();
                    if (block == Blocks.AIR) {
                        continue;
                    }
                    NonNullList<ItemStack> dropStacks = NonNullList.create();

                    List<ItemStack> listDrops = Lists.newArrayList();
                    if (level.getBlockEntity(curPos) == null) {
                        LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) level)
                                .withParameter(LootContextParams.TOOL, tool)
                                .withParameter(LootContextParams.BLOCK_STATE, curState)
                                .withParameter(LootContextParams.THIS_ENTITY, player)
                                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(curPos))
                                .withLuck(player.getLuck());

                        listDrops = curState.getDrops(paramsBuilder);
                    } else if (level.getBlockEntity(curPos) != null) {
                        LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) level)
                                .withParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(curPos))
                                .withParameter(LootContextParams.TOOL, tool)
                                .withParameter(LootContextParams.BLOCK_STATE, curState)
                                .withParameter(LootContextParams.THIS_ENTITY, player)
                                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(curPos))
                                .withLuck(player.getLuck());

                        listDrops = curState.getDrops(paramsBuilder);
                    }

                    dropStacks.addAll(listDrops);
                    exp += block.getExpDrop(state, level, curPos, null, player, tool);

                    NonNullList<ItemStack> furnaced = NonNullList.create();
                    if (autoFurnace) {
                        for (ItemStack stack : dropStacks) {
                            Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().filter(recipeHolder -> recipeHolder.value().getIngredients().getFirst().test(stack)).findFirst();

                            if (recipe.isPresent()) {
                                SmeltingRecipe smeltingRecipe = recipe.get().value();
                                SingleRecipeInput recipeInput = new SingleRecipeInput(stack);
                                ItemStack result = smeltingRecipe.assemble(recipeInput, level.registryAccess());

                                if (!result.isEmpty()) {
                                    float furnaceExp = smeltingRecipe.getExperience();
                                    int resultCount = result.getCount() * stack.getCount();
                                    if (fortuneLevel > 0) {
                                        int power = level.random.nextInt(fortuneLevel + 2);
                                        if (power == 0) {
                                            power = 1;
                                        }
                                        resultCount *= power;
                                        furnaceExp *= power;
                                    }
                                    exp += furnaceExp;
                                    while (resultCount > 64) {
                                        furnaced.add(new ItemStack(Holder.direct(result.getItem()), 64, result.getComponentsPatch()));
                                        resultCount -= 64;
                                    }
                                    furnaced.add(new ItemStack(Holder.direct(result.getItem()), resultCount, result.getComponentsPatch()));
                                    stack.setCount(0);
                                }
                            }

                            dropStacks.removeIf(ItemStack::isEmpty);
                            dropStacks.addAll(furnaced);
                        }
                    }
                    if (dropStacks.isEmpty() && mandatoryDrop) {
                        ItemStack dropStack = new ItemStack(block, 1);
                        dropStacks.add(dropStack);
                    }
                    drops.addAll(dropStacks);
                    level.setBlockAndUpdate(curPos, Blocks.AIR.defaultBlockState());
                }
            }
        }

        NonNullList<ItemStack> blacklist = NonNullList.create();
        if (tool.has(ModDataComponents.LOLI_BLACK_LIST_DATA.get())) {
            ListTag blackList = tool.get(ModDataComponents.LOLI_BLACK_LIST_DATA.get()).copyTag().getList("Blacklist", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < blackList.size(); i++) {
                CompoundTag black = (CompoundTag) blackList.get(i);
                if (black.contains("Name") && black.contains("Damage")) {
                    Item blackItem = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(black.getString("Name")));
                    ItemStack blackStack = new ItemStack(blackItem, 1);
                    blackStack.set(DataComponents.DAMAGE, black.getInt("Damage"));
                    blacklist.add(blackStack);
                }
            }
        }
        if (!blacklist.isEmpty()) {
            drops.removeIf(stack -> {
                for (ItemStack black : blacklist) {
                    if (ItemStack.isSameItem(black, stack)) {
                        return true;
                    }
                }
                return false;
            });
        }

        if (auto) {
            for (ItemStack dropStack : drops) {
                for (int i = 0; i < inventory.getMaxPage(); i++) {
                    NonNullList<ItemStack> stacks = inventory.getPage(i);
                    for (int j = 0; j < stacks.size(); j++) {
                        ItemStack slotStack = stacks.get(j);
                        if (slotStack.isEmpty()) {
                            stacks.set(j, dropStack.copy());
                            dropStack.setCount(0);
                            break;
                        } else {
                            int maxCount = inventory.getMaxStackSize();
                            if (stacks.get(j).getCount() < maxCount) {
                                int count = dropStack.getCount();
                                if (count > 0 && ItemStack.isSameItem(slotStack, dropStack) && ItemStack.isSameItemSameComponents(slotStack, dropStack)) {
                                    slotStack.grow(count);
                                    dropStack.shrink(count);
                                    if (dropStack.isEmpty()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (dropStack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        drops.removeIf(ItemStack::isEmpty);
        if (!drops.isEmpty()) {
            for (ItemStack dropStack : drops) {
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack);
                level.addFreshEntity(item);
            }
        }
        if (auto) {
            inventory.stopOpen(player);
        }
        if ((int) exp > 0) {
            level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (int) exp));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide()) return;

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.getCapability(Capabilities.EnergyStorage.ITEM) != null) {
                    if (ConfigLoader.getBoolean(stack, "loliPickaxeInfiniteBattery")) {
                        if (itemStack.getCapability(Capabilities.EnergyStorage.ITEM).canReceive()) {
                            itemStack.getCapability(Capabilities.EnergyStorage.ITEM).receiveEnergy(Integer.MAX_VALUE, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                if (ConfigLoader.getBoolean(itemStack, "loliPickaxeKillRangeEntity")) {
                    int range = ConfigLoader.getInt(itemStack, "loliPickaxeKillRange");
                    int count = LoliPickaxeUtil.killRangeEntity(level, player, range);
                    player.sendSystemMessage(Component.translatable("loliPickaxe.killrangeentity", range * 2, count));
                    CommonUtil.playLoliSuccessSound(player);
                    return InteractionResultHolder.success(itemStack);
                }
            } else {
                if (itemStack.has(ModDataComponents.LOLI_MINING_RANGE.get())) {
                    int range = itemStack.get(ModDataComponents.LOLI_MINING_RANGE.get());
                    itemStack.set(ModDataComponents.LOLI_MINING_RANGE.get(), range >= ModConfigs.INSTANCE.loliPickaxeMaxRange.get() ? 0 : range + 1);
                } else {
                    itemStack.set(ModDataComponents.LOLI_MINING_RANGE.get(), 1);
                }
                player.sendSystemMessage(Component.translatable("loliPickaxe.range", 1 + 2 * itemStack.get(ModDataComponents.LOLI_MINING_RANGE.get())));
                CommonUtil.playLoliSuccessSound(player);
                return InteractionResultHolder.success(itemStack);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);

        if (ConfigLoader.getBoolean(stack, "loliPickaxeMandatoryDrop")) {
            tooltip.add(Component.translatable("loliPickaxe.mandatoryDrop").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeStopOnLiquid")) {
            tooltip.add(Component.translatable("loliPickaxe.stopOnLiquid").withStyle(ChatFormatting.GRAY));
        }
        double distance = ConfigLoader.getDouble(stack, "loliPickaxeBlockReachDistance");
        if (distance > 0) {
            tooltip.add(Component.translatable("loliPickaxe.blockReachDistance", distance).withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoAccept")) {
            tooltip.add(Component.translatable("loliPickaxe.autoAccept").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeThorns")) {
            tooltip.add(Component.translatable("loliPickaxe.thorns").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeKillRangeEntity")) {
            tooltip.add(Component.translatable("loliPickaxe.killRange", 2 * ConfigLoader.getInt(stack, "loliPickaxeKillRange")).withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoKillRangeEntity")) {
            tooltip.add(Component.translatable("loliPickaxe.autoKillRange", 2 * ConfigLoader.getInt(stack, "loliPickaxeAutoKillRange")).withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeCompulsoryRemove")) {
            tooltip.add(Component.translatable("loliPickaxe.compulsoryRemove").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
            tooltip.add(Component.translatable("loliPickaxe.validToAmityEntity").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity")) {
            tooltip.add(Component.translatable("loliPickaxe.validToAllEntity").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeClearInventory")) {
            tooltip.add(Component.translatable("loliPickaxe.clearInventory").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeDropItems")) {
            tooltip.add(Component.translatable("loliPickaxe.dropItems").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeKickPlayer")) {
            tooltip.add(Component.translatable("loliPickaxe.kickPlayer").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeReincarnation")) {
            tooltip.add(Component.translatable("loliPickaxe.reincarnation").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeBeyondRedemption")) {
            tooltip.add(Component.translatable("loliPickaxe.beyondRedemption").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeBlueScreenAttack")) {
            tooltip.add(Component.translatable("loliPickaxe.blueScreenAttack").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeExitAttack")) {
            tooltip.add(Component.translatable("loliPickaxe.exitAttack").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeFailRespondAttack")) {
            tooltip.add(Component.translatable("loliPickaxe.failRespondAttack").withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeKillFacing")) {
            tooltip.add(Component.translatable("loliPickaxe.killFacing", ConfigLoader.getInt(stack, "loliPickaxeKillFacingRange"), ConfigLoader.getDouble(stack, "loliPickaxeKillFacingSlope")).withStyle(ChatFormatting.GRAY));
        }
        if (ConfigLoader.getBoolean(stack, "loliPickaxeInfiniteBattery")) {
            tooltip.add(Component.translatable("loliPickaxe.infiniteBattery").withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" " + Component.translatable("loliPickaxe.damage").getString() + " " + Component.translatable("attribute.name.generic.attack_damage").getString()).withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal(" " + Component.translatable("loliPickaxe.speed").getString() + " " + Component.translatable("attribute.name.generic.attack_speed").getString()).withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public boolean hasInventory(ItemStack stack) {
        return true;
    }

    @Override
    public ILoliInventory getInventory(ItemStack stack, HolderLookup.Provider registryAccess) {
        return new InventoryLoliPickaxe(stack, registryAccess);
    }

    public static class EnergyStorage implements IEnergyStorage {
        public EnergyStorage() {

        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return maxExtract;
        }

        @Override
        public int getEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    }
}