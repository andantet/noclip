package me.andante.noclip.mixin.client;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.api.client.keybinding.NoClipKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("InvalidInjectorMethodSignature")
@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {
    @Unique private static final String SET_FLIGHT_SPEED_KEY = "text." + NoClip.MOD_ID + ".flight_speed.set";
    @Shadow @Final private MinecraftClient client;

    /**
     * Enables scrolling to modify fly speed when in creative and a key binding held or toggled.
     */
    @Inject(
        method = "onMouseScroll",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci, double scroll, int delta) {
        if (NoClipKeybindings.ACTIVATE_FLIGHT_SPEED_SCROLL.isPressed()) {
            PlayerAbilities abilities = this.client.player.getAbilities();
            if (abilities.flying) {
                float old = abilities.getFlySpeed();

                float speed = MathHelper.clamp(old + (delta * 0.005f), 0.0f, NoClipClient.getConfig().flight.maxScrolledSpeed / 20f);
                abilities.setFlySpeed(speed);

                if (old != speed && NoClipClient.getConfig().displaySpeedOnActionBar) {
                    PlayerAbilities def = new PlayerAbilities();
                    this.client.player.sendMessage(Text.translatable(SET_FLIGHT_SPEED_KEY, String.format("%.1f", speed / def.getFlySpeed())).setStyle(NoClipClient.getTextStyle()), true);
                }

                ci.cancel();
            }
        }
    }
}
