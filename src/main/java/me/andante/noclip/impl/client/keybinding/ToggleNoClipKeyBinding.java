package me.andante.noclip.impl.client.keybinding;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.BooleanSupplier;

public class ToggleNoClipKeyBinding extends StickyKeyBinding {
    public static final String ACTIONBAR_KEY = "text." + NoClip.MOD_ID + ".server_noclip_not_present";

    public ToggleNoClipKeyBinding(String id, int code, String category, BooleanSupplier toggleGetter) {
        super(id, code, category, toggleGetter);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (pressed) {
            NoClipManager clipping = NoClipManager.INSTANCE;
            if (!clipping.canClip()) {
                MinecraftClient client = MinecraftClient.getInstance();
                client.player.sendMessage(Text.translatable(ACTIONBAR_KEY).formatted(Formatting.RED), true);
            }
        }
    }
}
