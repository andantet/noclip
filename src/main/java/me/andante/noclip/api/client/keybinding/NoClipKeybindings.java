package me.andante.noclip.api.client.keybinding;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.impl.client.keybinding.ToggleNoClipKeyBinding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public interface NoClipKeybindings {
    String CATEGORY = "key.category." + NoClip.MOD_ID;

    KeyBinding TOGGLE_NOCLIP = KeyBindingHelper.registerKeyBinding(new ToggleNoClipKeyBinding(
        "key." + NoClip.MOD_ID + ".toggle_noclip",
        GLFW.GLFW_KEY_GRAVE_ACCENT, CATEGORY, () -> true
    ));

    KeyBinding ENABLE_CREATIVE_FLIGHT_SPEED_SCROLL = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding(
        "key." + NoClip.MOD_ID + ".enable_creative_flight_speed_scroll",
        GLFW.GLFW_KEY_LEFT_CONTROL, CATEGORY, () -> NoClipClient.getConfig().toggleCreativeFlightSpeed
    ));

    KeyBinding RESET_FLIGHT_SPEED = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key." + NoClip.MOD_ID + ".reset_flight_speed", GLFW.GLFW_KEY_UNKNOWN, CATEGORY
    ));
}
