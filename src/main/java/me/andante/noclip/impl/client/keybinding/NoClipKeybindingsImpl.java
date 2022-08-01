package me.andante.noclip.impl.client.keybinding;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.api.client.config.NoClipConfig;
import me.andante.noclip.api.client.keybinding.NoClipKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public final class NoClipKeybindingsImpl implements NoClipKeybindings {
    private static final PlayerAbilities DEFAULT_ABILITIES = new PlayerAbilities();
    public static final String RESET_FLIGHT_SPEED_KEY = "text." + NoClip.MOD_ID + ".flight_speed.reset";

    public static void onEndClientTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        NoClipConfig config = NoClipClient.getConfig();
        PlayerAbilities abilities = player.getAbilities();

        /* Clipping */

        NoClipManager clipping = NoClipManager.INSTANCE;
        if (clipping.canClip()) {
            boolean prev = clipping.isClipping();
            boolean curr = TOGGLE_NOCLIP.isPressed() && !(config.onlyCreative && !client.interactionManager.getCurrentGameMode().isCreative());
            if (prev != curr) {
                if (clipping.setClipping(curr)) {
                    // set flying
                    if (config.enableFlightOnClip) {
                        if (abilities.allowFlying) abilities.flying = true;
                    }
                }

                clipping.updateClipping();
            }
        }

        /* Snappy Flight */

        if (abilities.flying) {
            if (config.snappyFlight && (!config.snappyFlightRequiresClipping || clipping.isClipping())) {
                player.setVelocity(Vec3d.ZERO);

                int sideways = 0;
                int upward = 0;
                int forward = 0;

                GameOptions options = client.options;
                if (options.leftKey.isPressed()) sideways += 1;
                if (options.rightKey.isPressed()) sideways -= 1;
                if (options.jumpKey.isPressed()) upward += 1;
                if (options.sneakKey.isPressed()) upward -= 1;
                if (options.forwardKey.isPressed()) forward += 1;
                if (options.backKey.isPressed()) forward -= 1;

                player.travel(new Vec3d(sideways, upward, forward));
                player.setVelocity(player.getVelocity().multiply(7.0D));
                player.addVelocity(0.0D, upward * abilities.getFlySpeed() * 2, 0.0D);
            }
        }

        /* Flight Speed */

        if (RESET_FLIGHT_SPEED.wasPressed()) {
            float speed = DEFAULT_ABILITIES.getFlySpeed();
            abilities.setFlySpeed(speed);
            player.sendMessage(Text.translatable(RESET_FLIGHT_SPEED_KEY, String.format("%.3f", speed)), true);
        }
    }
}
