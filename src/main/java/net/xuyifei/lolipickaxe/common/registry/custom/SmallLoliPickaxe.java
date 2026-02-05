package net.xuyifei.lolipickaxe.common.registry.custom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thevortex.allthemodium.registry.TagRegistry;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.xuyifei.lolipickaxe.common.gui.ILoliInventory;
import net.xuyifei.lolipickaxe.common.gui.InventorySmallLoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import net.xuyifei.lolipickaxe.common.registry.tool.IContainer;
import net.xuyifei.lolipickaxe.common.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SmallLoliPickaxe extends PickaxeItem implements IContainer {

    public static Map<LoliPickaxeMaterial, String> componentsMap = Maps.newHashMap();
    public static boolean isharvesting = false;
    public static boolean autoFurnace = false;
    public static int fortuneLevel = 0;
    public static float exp = 0;
    public static NonNullList<ItemStack> blacklist = null;
    public static ILoliInventory inventory = null;

    public static void init() {
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_COAL_ADDON_MAX.get(), "LoliDodge");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_IRON_ADDON_MAX.get(), "LoliDiggingSpeed");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_GOLD_ADDON_MAX.get(), "LoliAttackDamage");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_REDSTONE_ADDON_MAX.get(), "LoliAttackSpeed");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_LAPIS_ADDON_MAX.get(), "LoliFortuneLevel");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_DIAMOND_ADDON_MAX.get(), "LoliDiggingLevel");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_EMERALD_ADDON_MAX.get(), "LoliDiggingRange");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_OBSIDIAN_ADDON_MAX.get(), "LoliAntiInjury");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_GLOW_ADDON_MAX.get(), "LoliBuff");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_QUARTZ_ADDON_MAX.get(), "LoliHitRange");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_NETHER_STAR_ADDON_MAX.get(), "LoliBackpackPage");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_AUTO_FURNACE_ADDON.get(), "LoliAutoFurnace");
        componentsMap.put((LoliPickaxeMaterial) ModItems.LOLI_FLY_ADDON.get(), "LoliFly");
    }

    @Override
    public int getDamage(@NotNull ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    public static ItemStack getFull(RegistryAccess registryAccess) {
        init();
        ItemStack full = new ItemStack(ModItems.SMALL_LOLI_PICKAXE.get(), 1);
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<LoliPickaxeMaterial, String> entry : componentsMap.entrySet()) {
            tag.putInt(entry.getValue(), entry.getKey().getSubCount() - 1);
        }
        full.set(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.of(tag));
        ((SmallLoliPickaxe) full.getItem()).updateEnchantment(full, registryAccess);
        return full;
    }

    public SmallLoliPickaxe(Properties properties) {
        super(Tiers.NETHERITE, properties.durability(Integer.MAX_VALUE).component(DataComponents.UNBREAKABLE, new Unbreakable(true)).component(DataComponents.DAMAGE, Integer.MAX_VALUE));
    }

    public void updateEnchantment(ItemStack stack, RegistryAccess registryAccess) {
        if (stack.has(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())) {
            if (Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).contains("LoliFortuneLevel")) {
                int fortuneLevel = getTransformValue("LoliFortuneLevel", Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).copyTag().getInt("LoliFortuneLevel"));
                Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);

                ResourceKey<Enchantment> fortuneKey = Enchantments.FORTUNE;
                Holder<Enchantment> fortuneHolder = enchantmentRegistry.getHolder(fortuneKey).orElse(null);

                ResourceKey<Enchantment> lootingKey = Enchantments.LOOTING;
                Holder<Enchantment> lootingHolder = enchantmentRegistry.getHolder(lootingKey).orElse(null);

                if (fortuneHolder != null) {
                    stack.enchant(fortuneHolder, fortuneLevel);
                }
                if (lootingHolder != null) {
                    stack.enchant(lootingHolder, fortuneLevel);
                }
            }
        } else {
            stack.set(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, @NotNull BlockState state) {
        if (stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliDiggingSpeed")) {
            return getTransformValue("LoliDiggingSpeed", stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).copyTag().getInt("LoliDiggingSpeed"));
        }
        return 1;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers.Builder attributeModifiers = ItemAttributeModifiers.builder();
        double damage = 0;
        double speed = 0;
        if (stack.has(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())) {
            if (stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).contains("LoliAttackDamage")) {
                damage = getDoubleTransformValue("LoliAttackDamage", stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliAttackDamage"));
            }
            if (stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).contains("LoliAttackSpeed")) {
                speed = getTransformValue("LoliAttackSpeed", stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliAttackSpeed"));
            }
        }

        Holder<Attribute> attackDamageHolder = Attributes.ATTACK_DAMAGE;
        Holder<Attribute> attackSpeedHolder = Attributes.ATTACK_SPEED;

        attributeModifiers.add(attackDamageHolder, new AttributeModifier(ResourceLocation.withDefaultNamespace("generic.attack_damage"), damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        attributeModifiers.add(attackSpeedHolder, new AttributeModifier(ResourceLocation.withDefaultNamespace("generic.attack_speed"), speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        return attributeModifiers.build();
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        if (stack.has(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())) {
            CompoundTag attrTag = stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag();
            if (attrTag.contains("LoliDiggingLevel")) {
                int havestLevel = getTransformValue("LoliDiggingLevel", attrTag.getInt("LoliDiggingLevel"));
                return canHarvestWithLevel(state, havestLevel);
            }
        }
        return false;
    }

    @Override
    public @NotNull Tier getTier() {
        return super.getTier();
    }

    private boolean canHarvestWithLevel(BlockState state, int harvestLevel) {
        if (state.is(TagRegistry.NEEDS_ALLOY_TOOL) && harvestLevel < 7) {
            return false;
        }
        if (state.is(TagRegistry.NEEDS_UNOBTAINIUM_TOOL) && harvestLevel < 6) {
            return false;
        }
        if (state.is(TagRegistry.NEEDS_VIBRANIUM_TOOL) && harvestLevel < 5) {
            return false;
        }
        if (state.is(TagRegistry.NEEDS_ALLTHEMODIUM_TOOL) && harvestLevel < 4) {
            return false;
        }
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL) && harvestLevel < 3) {
            return false;
        }
        if (state.is(BlockTags.NEEDS_IRON_TOOL) && harvestLevel < 2) {
            return false;
        }
        if (state.is(BlockTags.NEEDS_STONE_TOOL) && harvestLevel < 1) {
            return false;
        }

        return true;
    }

    public int getTransformValue(String key, int level) {
        return switch (key) {
            case "LoliDiggingSpeed" -> 4 << level;
            case "LoliDiggingLevel" -> switch (level) {
                case 0 -> 1;
                case 1 -> 3;
                case 2 -> 7;
                case 3 -> 13;
                case 4 -> 21;
                case 5 -> 32;
                default -> -1;
            };
            case "LoliDiggingRange" -> level * 2 + 3;
            case "LoliAttackSpeed", "LoliBackpackPage" -> 2 << level;
            case "LoliFortuneLevel" -> 1 << level;
            case "LoliBuff" -> level + 1;
            case "LoliHitRange" -> level * 10 + 6;
            case "LoliAutoFurnace", "LoliFly" -> level;
            default -> Integer.MIN_VALUE;
        };
    }

    public int getMaxPage(ItemStack stack) {
        return getTransformValue("LoliBackpackPage", stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliBackpackPage") ? stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliBackpackPage") : -1);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (stack.has(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())) {
            if (!level.isClientSide()) {
                if (player.isShiftKeyDown()) {
                    if (stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().contains("LoliHitRange")) {
                        int range = getTransformValue("LoliHitRange", stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliHitRange"));
                        List<Entity> list = level.getEntities(player,
                                player.getBoundingBox().inflate(range + 0.7, range + 0.1, range + 0.7),
                                entity -> !(entity instanceof Player ||
                                        entity instanceof ArmorStand ||
                                        entity instanceof AmbientCreature ||
                                        (entity instanceof PathfinderMob && !(entity instanceof Enemy)))
                        );

                        for (Entity entity : list) {
                            player.attack(entity);
                            player.resetAttackStrengthTicker();
                        }

                        CommonUtil.playLoliSuccessSound(player);
                        return InteractionResultHolder.success(stack);
                    }
                } else {
                    if (stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().contains("LoliDiggingRange")) {
                        int maxRange = (getTransformValue("LoliDiggingRange", stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliDiggingRange")) - 1) / 2 + 1;
                        if (stack.has(ModDataComponents.LOLI_MINING_RANGE.get())) {
                            stack.set(ModDataComponents.LOLI_MINING_RANGE.get(), (stack.get(ModDataComponents.LOLI_MINING_RANGE.get()) + 1) % maxRange);
                        } else {
                            stack.set(ModDataComponents.LOLI_MINING_RANGE.get(), 1);
                        }
                        player.sendSystemMessage(Component.translatable("loliPickaxe.range", 1 + 2 * stack.get(ModDataComponents.LOLI_MINING_RANGE.get())));
                        CommonUtil.playLoliSuccessSound(player);
                        return InteractionResultHolder.success(stack);
                    }
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public double getDoubleTransformValue(String key, int level) {
        return switch (key) {
            case "LoliDodge", "LoliAntiInjury" -> (level + 1) / 10.0;
            case "LoliAttackDamage" -> 4 + Math.pow(2, Math.pow(2, level));
            default -> Integer.MIN_VALUE;
        };
    }

    public double getDodge(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliDodge") ? getDoubleTransformValue("LoliDodge", Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).copyTag().getInt("LoliDodge")) : 0;
    }

    public double getAntiInjury(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliAntiInjury") ? getDoubleTransformValue("LoliAntiInjury", Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).copyTag().getInt("LoliAntiInjury")) : 0;
    }

    public double buffLevel(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliBuff") ? getTransformValue("LoliBuff", Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).copyTag().getInt("LoliBuff")) : -1;
    }

    public int getRange(ItemStack stack) {
        if (stack.has(ModDataComponents.LOLI_MINING_RANGE.get())) {
            return stack.get(ModDataComponents.LOLI_MINING_RANGE.get());
        }
        return 0;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity miningEntity) {
        if (!level.isClientSide() && miningEntity instanceof Player player) {
            int range = getRange(stack);
            isharvesting = true;
            autoFurnace = stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).contains("LoliAutoFurnace") && getTransformValue("LoliAutoFurnace", stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get()).copyTag().getInt("LoliAutoFurnace")) == 0;
            Registry<Enchantment> enchantmentRegistry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            fortuneLevel = stack.getEnchantmentLevel(enchantmentRegistry.getHolder(Enchantments.FORTUNE).orElseThrow());
            exp = 0;
            blacklist = NonNullList.create();
            if (stack.has(ModDataComponents.LOLI_BLACK_LIST_DATA.get())) {
                ListTag blackList = stack.get(ModDataComponents.LOLI_BLACK_LIST_DATA.get()).copyTag().getList("Blacklist", CompoundTag.TAG_COMPOUND);
                for (net.minecraft.nbt.Tag tag : blackList) {
                    CompoundTag black = (CompoundTag) tag;
                    if (black.contains("Name") && black.contains("Damage")) {
                        Item blackItem = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(black.getString("Name")));
                        ItemStack blackStack = new ItemStack(blackItem, 1);
                        blackStack.set(DataComponents.DAMAGE, black.getInt("Damage"));
                        blacklist.add(blackStack);
                    }
                }
            }
            if (hasInventory(stack)) {
                inventory = getInventory(stack, level.registryAccess());
            }
            List<ItemStack> listDrops = Lists.newArrayList();
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    for (int k = -range; k <= range; k++) {
                        BlockPos curPos = pos.offset(i, j, k);
                        if (NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player)).isCanceled()) {
                            continue;
                        }
                        BlockState curState = level.getBlockState(curPos);
                        Block curBlock = curState.getBlock();
                        if (!curBlock.isEmpty(curState)) {
                            curBlock.onDestroyedByPlayer(curState, level, curPos, player, true, level.getFluidState(curPos));
                            if (canHarvestWithLevel(curState, stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).copyTag().getInt("LoliDiggingLevel"))) {
                                if (level.getBlockEntity(curPos) == null) {
                                    LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) level)
                                            .withParameter(LootContextParams.TOOL, stack)
                                            .withParameter(LootContextParams.BLOCK_STATE, curState)
                                            .withParameter(LootContextParams.THIS_ENTITY, player)
                                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(curPos))
                                            .withLuck(player.getLuck());

                                    listDrops = curState.getDrops(paramsBuilder);
                                } else if (level.getBlockEntity(curPos) != null) {
                                    LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) level)
                                            .withParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(curPos))
                                            .withParameter(LootContextParams.TOOL, stack)
                                            .withParameter(LootContextParams.BLOCK_STATE, curState)
                                            .withParameter(LootContextParams.THIS_ENTITY, player)
                                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(curPos))
                                            .withLuck(player.getLuck());

                                    listDrops = curState.getDrops(paramsBuilder);
                                }
                                exp += curBlock.getExpDrop(curState, level, curPos, level.getBlockEntity(curPos), player, stack);
                            }
                        }
                    }
                }
            }
            NonNullList<ItemStack> furnaced = NonNullList.create();
            if (autoFurnace) {
                for (ItemStack itemStack : listDrops) {
                    Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().filter(recipeHolder -> recipeHolder.value().getIngredients().getFirst().test(itemStack)).findFirst();

                    if (recipe.isPresent()) {
                        SmeltingRecipe smeltingRecipe = recipe.get().value();
                        SingleRecipeInput recipeInput = new SingleRecipeInput(itemStack);
                        ItemStack result = smeltingRecipe.assemble(recipeInput, level.registryAccess());

                        if (!result.isEmpty()) {
                            float furnaceExp = smeltingRecipe.getExperience();
                            int resultCount = result.getCount() * itemStack.getCount();
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
                            itemStack.setCount(0);
                        }
                    }

                    listDrops.removeIf(ItemStack::isEmpty);
                    listDrops.addAll(furnaced);
                }
            }
            listDrops.removeIf(ItemStack::isEmpty);
            level.addFreshEntity(new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (int) exp));
            for (ItemStack itemStack : listDrops) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack));
            }
            
            fortuneLevel = 0;
            autoFurnace = false;
            isharvesting = false;
        }
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (stack.has(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())) {
            CompoundTag tag = Objects.requireNonNull(stack.get(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get())).copyTag();
            for (String levelKey : componentsMap.values()) {
                if (tag.contains(levelKey)) {
                    int level = tag.getInt(levelKey);
                    int value = getTransformValue(levelKey, level);
                    if (value == Integer.MIN_VALUE) {
                        tooltipComponents.add(Component.translatable("smallLoliPickaxe." + levelKey, getDoubleTransformValue(levelKey, level)).withStyle(ChatFormatting.GRAY));
                    } else {
                        tooltipComponents.add(Component.translatable("smallLoliPickaxe." + levelKey, value).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
        }
    }

    @Override
    public boolean hasInventory(ItemStack stack) {
        return true;
    }

    @Override
    public ILoliInventory getInventory(ItemStack stack, HolderLookup.Provider registryAccess) {
        return new InventorySmallLoliPickaxe(stack, registryAccess);
    }
}
