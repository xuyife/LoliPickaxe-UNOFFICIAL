package net.xuyifei.lolipickaxe.common.entity.render;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.LayerMaidHeldItem;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.neoforged.fml.ModList;
import net.xuyifei.lolipickaxe.common.entity.EntityLoli;
import net.xuyifei.lolipickaxe.common.registry.ModConfigs;
import org.jetbrains.annotations.NotNull;

public class RenderRemiliaLoli extends LivingEntityRenderer<EntityLoli, EntityModel<EntityLoli>> {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final ResourceLocation DEFAULT_MODEL_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TouhouLittleMaid.MOD_ID, "textures/entity/hakurei_reimu.png"
    );
    private ResourceLocation modelRes;

    public RenderRemiliaLoli(EntityRendererProvider.Context context, EntityModel<EntityLoli> model, float shadowSize) {
        super(context, model, shadowSize);
        this.modelRes = DEFAULT_MODEL_TEXTURE;
        if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
            init(context);
        }
    }

    @SuppressWarnings("unchecked")
    private void init(EntityRendererProvider.Context context) {
        RenderLayer<Mob, BedrockModel<Mob>> heldItemLayer = new LayerMaidHeldItem(new EntityMaidRenderer(context), context.getItemInHandRenderer());
        RenderLayer<EntityLoli, EntityModel<EntityLoli>> castedHeldItemLayer = (RenderLayer<EntityLoli, EntityModel<EntityLoli>>) (RenderLayer<?, ?>) heldItemLayer;
        this.addLayer(castedHeldItemLayer);
    }

    @Override
    public void render(@NotNull EntityLoli entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer,
                       int packedLight) {

        if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
            @SuppressWarnings("unchecked")
            EntityModel<EntityLoli> defaultModel = (EntityModel<EntityLoli>) (BedrockModel<? extends Mob>) CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID)
                    .orElseThrow(() -> new NullPointerException("Model not found: " + DEFAULT_MODEL_ID));

            this.model = defaultModel;

            CustomPackLoader.MAID_MODELS.getModel(ModConfigs.INSTANCE.loliModelId.get()).ifPresent(model -> {
                @SuppressWarnings("unchecked")
                EntityModel<EntityLoli> loliModel = (EntityModel<EntityLoli>) (BedrockModel<? extends Mob>) model;
                this.model = loliModel;
            });
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        RenderSystem.disableBlend();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityLoli entity) {
        this.modelRes = DEFAULT_MODEL_TEXTURE;

        if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
            CustomPackLoader.MAID_MODELS.getInfo(ModConfigs.INSTANCE.loliModelId.get())
                    .ifPresent(modelItem -> modelRes = modelItem.getTexture());
        }

        return modelRes;
    }

    @Override
    protected boolean shouldShowName(@NotNull EntityLoli entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName());
    }

    @Override
    protected void scale(@NotNull EntityLoli entity, @NotNull PoseStack poseStack, float partialTickTime) {
        super.scale(entity, poseStack, partialTickTime);
    }
}
