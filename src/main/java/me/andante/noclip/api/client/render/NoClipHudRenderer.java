package me.andante.noclip.api.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.impl.client.NoClipClientImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Predicate;

import static net.minecraft.util.math.MathHelper.*;

/**
 * Responsible for rendering an indicator on the hud of the player's current clipping state.
 *
 * The current instance used by the client can be obtained by {@link NoClipClientImpl#NOCLIP_HUD_RENDERER}.
 */
@Environment(EnvType.CLIENT)
public class NoClipHudRenderer extends DrawableHelper implements HudRenderCallback {
    public static final Identifier TEXTURE = new Identifier(NoClip.MOD_ID, "textures/gui/noclip.png");
    private String activeDebugLine;

    public NoClipHudRenderer() {}

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if (!NoClipManager.INSTANCE.isClipping()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        Window window =  client.getWindow();
        int scaledWidth = window.getScaledWidth();

        // calculate effects
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        boolean hasStatusEffect = effects.stream().anyMatch(StatusEffectInstance::shouldShowIcon);
        boolean hasNonBeneficialEffect = effects.stream().map(StatusEffectInstance::getEffectType).anyMatch(Predicate.not(StatusEffect::isBeneficial));

        // render
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();

        float alpha = abs(sin(client.world.getTime() / 20f)) + 0.2F;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, clamp(alpha, 0.0F, 1.0F));

        if (client.options.debugEnabled) {
            this.renderIcon(matrices, scaledWidth - 18 - (client.textRenderer.getWidth(this.activeDebugLine) + 4), client.textRenderer.fontHeight + 1);
        } else this.renderIcon(matrices, scaledWidth - 18 - 2, (2 + (hasStatusEffect ? 25 + (hasNonBeneficialEffect ? 25 + 1 : 0) : 0)));

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderIcon(MatrixStack matrices, int x, int y) {
        drawTexture(matrices, x, y, 0, 0, 18, 18, 18, 18);
    }

    public void setActiveDebugLine(String activeDebugLine) {
        this.activeDebugLine = activeDebugLine;
    }
}
