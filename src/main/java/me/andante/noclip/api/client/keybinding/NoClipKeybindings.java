package me.andante.noclip.api.client.keybinding;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.impl.client.keybinding.ToggleNoClipKeyBinding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public interface NoClipKeybindings {
    KeyBinding TOGGLE_NOCLIP = KeyBindingHelper.registerKeyBinding(new ToggleNoClipKeyBinding(
        "key." + NoClip.MOD_ID + ".toggle_noclip", GLFW.GLFW_KEY_GRAVE_ACCENT,
        "key.category." + NoClip.MOD_ID, () -> true
    ));
}
