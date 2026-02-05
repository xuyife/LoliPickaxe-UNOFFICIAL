package net.xuyifei.lolipickaxe.common.entity.render;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.xuyifei.lolipickaxe.LoliPickaxe;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;

public class EntityLoliRenderer extends MobRenderer<EntityLoli, ModelLoli> {
    private final ModelLoli loli;
    private final ModelNevermore nevermore;
    private final RenderRemiliaLoli remilia;

    private static final ResourceLocation TEXTURE_LOLI = ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/entity/loli.png");
    private static final ResourceLocation TEXTURE_PAPER_LOLI = ResourceLocation.fromNamespaceAndPath(LoliPickaxe.MODID, "textures/entity/paper_loli.png");

    public EntityLoliRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelLoli(context.bakeLayer(ModelLoli.LAYER_LOCATION)), 0.5F);
        this.loli = new ModelLoli(context.bakeLayer(ModelLoli.LAYER_LOCATION));
        this.nevermore = new ModelNevermore(context);
        if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
            this.remilia = new RenderRemiliaLoli(context, model, 1.0f);
        } else {
            this.remilia = null;
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityLoli entity) {
        switch (ModConfigs.INSTANCE.loliModelType.get()) {
            case 2:
                return TEXTURE_PAPER_LOLI;
            case 3:
                if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
                    return remilia.getTextureLocation(entity);
                } else {
                    return TEXTURE_LOLI;
                }
            case 0, 1:
            default:
                return TEXTURE_LOLI;
        }
    }

    @Override
    public void render(@NotNull EntityLoli entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float limbSwing = entity.walkAnimation.position(partialTicks);
        float limbSwingAmount = entity.walkAnimation.speed(partialTicks);
        float ageInTicks = entity.tickCount + partialTicks;
        float netHeadYaw = entity.getYHeadRot() - entity.yBodyRot;
        float headPitch = entity.getXRot();
        switch (ModConfigs.INSTANCE.loliModelType.get()) {
            case 0:
                model = loli;
                model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

                break;
            case 1:
                nevermore.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                ResourceLocation texture = getTextureLocation(entity);

                RenderType renderType = RenderType.entityCutoutNoCull(texture);
                VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

                poseStack.pushPose();
                poseStack.scale(1.0f, 1.0f, 1.0f);
                poseStack.translate(0, 1.5, 0);

                nevermore.renderToBuffer(poseStack, vertexConsumer, packedLight,
                        OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                poseStack.popPose();
                break;
            case 3:
                if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
                    remilia.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
                    poseStack.popPose();
                } else {
                    model = loli;
                    model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                }
                break;
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
