package me.andante.noclip.api.client;

import me.andante.noclip.api.NoClip;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public interface NoClipKeybindings {
    KeyBinding TOGGLE_NOCLIP = register("toggle_noclip", GLFW.GLFW_KEY_GRAVE_ACCENT);

    private static KeyBinding register(String id, int code) {
        return KeyBindingHelper.registerKeyBinding(new StickyKeyBinding("key.%s.%s".formatted(NoClip.MOD_ID, id), code, "key.category." + NoClip.MOD_ID, () -> true));
    }
}
