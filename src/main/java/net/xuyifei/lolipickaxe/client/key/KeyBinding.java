package net.xuyifei.lolipickaxe.client.key;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {

    public static final KeyMapping LOLI_CONFIG = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_config", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.category." + LoliPickaxe.MODID); //N
    public static final KeyMapping LOLI_ENCHANTMENT = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_enchantment", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category." + LoliPickaxe.MODID); //M
    public static final KeyMapping LOLI_POTION = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_potion", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.category." + LoliPickaxe.MODID); //P
    public static final KeyMapping LOLI_SPACE_FOLDING = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_space_folding", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.category." + LoliPickaxe.MODID); //K
    public static final KeyMapping LOLI_PICKAXE_CONTAINER = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_container", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.category." + LoliPickaxe.MODID); //B
    public static final KeyMapping LOLI_PICKAXE_CONTAINER_BLACKLIST = new KeyMapping("key." + LoliPickaxe.MODID + ".loli_container_blacklist", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.category." + LoliPickaxe.MODID);//U

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(LOLI_CONFIG);
        event.register(LOLI_ENCHANTMENT);
        event.register(LOLI_POTION);
        event.register(LOLI_SPACE_FOLDING);
        event.register(LOLI_PICKAXE_CONTAINER);
        event.register(LOLI_PICKAXE_CONTAINER_BLACKLIST);
    }

}
