package me.andante.noclip.impl.client;

import me.andante.noclip.api.client.NoClipKeybindings;
import me.andante.noclip.api.client.NoClipManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;

@Environment(EnvType.CLIENT)
public final class NoClipKeybindingsImpl implements NoClipKeybindings {
    static void onEndClientTick(MinecraftClient client) {
        NoClipManager noclip = NoClipManager.INSTANCE;
        boolean prev = noclip.isClipping();
        boolean curr = TOGGLE_NOCLIP.isPressed();
        if (prev != curr) {
            if (curr) {
                if (client.player != null) {
                    // set flying
                    PlayerAbilities abilities = client.player.getAbilities();
                    if (abilities.allowFlying) abilities.flying = true;
                }
            }

            noclip.setClipping(curr);
            noclip.updateClipping();
        }
    }
}
