package net.panno.nifty.mixin;

import net.minecraft.client.font.FontRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.panno.nifty.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
abstract class EntityRendererMixin<T extends Entity> {
    @Shadow public abstract FontRenderer getFontRenderer();

    @Shadow @Final protected EntityRenderDispatcher renderManager;

    @Inject(method = "postRender", at = @At(value = "RETURN"))
    public void renderEntityHealth(Entity entity, double double_1, double double_2, double double_3, float inFloat, float inFloat2, CallbackInfo ci) {

        if (entity instanceof LivingEntity) {

            LivingEntity targetEntity = (LivingEntity)entity;
            // Construct health string
            float currentHealth = targetEntity.getHealth();
            currentHealth = roundToHalf(currentHealth);
            float maxHealth = targetEntity.getHealthMaximum();
            String healthString = currentHealth + "/" + maxHealth;

            // sync everything with main method below this
            double distance = entity.squaredDistanceTo(this.renderManager.field_4686);
            if (distance <= Config.DISTANCE) {
                boolean boolean_1 = entity.isSneaking();
                float float_1 = this.renderManager.field_4679;
                float float_2 = this.renderManager.field_4677;
                boolean boolean_2 = this.renderManager.settings.field_1850 == 2;
                float float_3 = entity.getHeight() + 0.5F - (boolean_1 ? 0.25F : 0.0F);
                int int_2 = -10;
                GameRenderer.method_3179(this.getFontRenderer(), healthString, (float) double_1, (float) double_2 + float_3, (float) double_3, int_2, float_1, float_2, boolean_2, boolean_1);
            }
        }
    }

    private static float roundToHalf(float f) {
        return Math.round(f * 2) / 2.0f;
    }

}