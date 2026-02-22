package net.xuyifei.lolipickaxe.common.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.registry.ModDataComponents;
import net.xuyifei.lolipickaxe.common.registry.ModItems;
import net.xuyifei.lolipickaxe.common.registry.custom.SmallLoliPickaxe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class LoliPickaxeJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, LoliPickaxe.MODID);
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.SMALL_LOLI_PICKAXE.get(), (stack, context) -> {
            if (!stack.getOrDefault(ModDataComponents.SMALL_LOLI_ATTRIBUTE.get(), CustomData.EMPTY).isEmpty()) {
                return "FullSmallLoliPickaxe";
            }
            return IIngredientSubtypeInterpreter.NONE;
        });
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        ClientLevel level = Minecraft.getInstance().level;

        if (level != null) {
            ItemStack stack = SmallLoliPickaxe.getFull(level.registryAccess());
            jeiRuntime.getIngredientManager().addIngredientsAtRuntime(
                    VanillaTypes.ITEM_STACK,
                    List.of(stack)
            );
        }
    }
}
