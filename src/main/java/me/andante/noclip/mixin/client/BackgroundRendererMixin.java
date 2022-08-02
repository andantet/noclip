package me.andante.noclip.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.andante.noclip.impl.ClippingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    /**
     * Improves vision when clipping.
     */
    @Inject(
        method = "applyFog",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci, CameraSubmersionType submersion, Entity entity, FogShape shape) {
        if (entity instanceof ClippingEntity clippingEntity && clippingEntity.isClipping()) {
            if (submersion == CameraSubmersionType.LAVA || submersion == CameraSubmersionType.POWDER_SNOW) {
                RenderSystem.setShaderFogStart(-8.0f);
                RenderSystem.setShaderFogEnd(viewDistance * 0.5f);
                RenderSystem.setShaderFogShape(shape);
                ci.cancel();
            }
        }
    }
}
