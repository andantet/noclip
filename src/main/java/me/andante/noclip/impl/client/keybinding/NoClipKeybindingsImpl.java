package me.andante.noclip.impl.client.keybinding;

import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.api.client.keybinding.NoClipKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;

@Environment(EnvType.CLIENT)
public final class NoClipKeybindingsImpl implements NoClipKeybindings {
    public static void onEndClientTick(MinecraftClient client) {
        if (client.player == null) return;

        NoClipManager clipping = NoClipManager.INSTANCE;
        if (clipping.canClip()) {
            boolean prev = clipping.isClipping();
            boolean curr = TOGGLE_NOCLIP.isPressed();
            if (prev != curr) {
                if (clipping.setClipping(curr)) {
                    // set flying
                    PlayerAbilities abilities = client.player.getAbilities();
                    if (abilities.allowFlying) abilities.flying = true;
                }
                clipping.updateClipping();
            }
        }
    }
}
