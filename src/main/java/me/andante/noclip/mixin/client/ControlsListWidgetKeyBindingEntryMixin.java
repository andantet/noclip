package me.andante.noclip.mixin.client;

import me.andante.noclip.api.client.keybinding.NoClipKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class ControlsListWidgetKeyBindingEntryMixin {
    @Shadow @Final private KeyBinding binding;
    @Shadow @Final private ButtonWidget editButton;

    /**
     * Removes the error formatting from {@link NoClipKeybindings#ACTIVATE_FLIGHT_SPEED_SCROLL} and its co-binding.
     */
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setMessage(Lnet/minecraft/text/Text;)V",
            ordinal = 2,
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onRender(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci, boolean selected, boolean incompatible) {
        if (incompatible) {
            if (this.binding == NoClipKeybindings.ACTIVATE_FLIGHT_SPEED_SCROLL
                || this.binding == KeyBindingAccessor.getKEY_TO_BINDINGS().get(((KeyBindingAccessor) this.binding).getBoundKey())
            ) this.editButton.setMessage(this.editButton.getMessage().copy().formatted(Formatting.WHITE));
        }
    }
}
