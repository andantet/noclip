package me.andante.noclip.impl.client;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipKeybindings;
import me.andante.noclip.api.client.NoClipManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public final class NoClipKeybindingsImpl implements NoClipKeybindings {
    private static final String ACTIONBAR_KEY = "text." + NoClip.MOD_ID + ".server_noclip_not_present";

    static void onEndClientTick(MinecraftClient client) {
        if (client.player == null) return;

        NoClipManager clipping = NoClipManager.INSTANCE;
        if (clipping.canClip()) {
            boolean prev = clipping.isClipping();
            boolean curr = TOGGLE_NOCLIP.isPressed();
            if (prev != curr) {
                if (clipping.setClipping(curr)) {
                    // set flying
                    PlayerAbilities abilities = client.player.getAbilities();
                    if (abilities.allowFlying) {
                        abilities.flying = true;
                    }
                }
                clipping.updateClipping();
            }
        } else {
            if (TOGGLE_NOCLIP.isPressed()) client.player.sendMessage(new TranslatableText(ACTIONBAR_KEY).formatted(Formatting.RED), true);
        }
    }
}
