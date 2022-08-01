package me.andante.noclip.api.client.config;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.impl.client.keybinding.NoClipKeybindingsImpl;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.Config.Gui.Background;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
@Background(Background.TRANSPARENT)
@Config(name = NoClip.MOD_ID)
public class NoClipConfig implements ConfigData {
    @Comment("Whether or not the icon displayed at the top-right of the HUD is present")
    @Tooltip(count = 2)
    public boolean hudIcon = true;

    @Comment("The color of text displayed by the mod")
    @ColorPicker
    @Excluded
    public int textColor = Formatting.GRAY.getColorValue();

    @CollapsibleObject(startExpanded = true)
    public Flight flight = new Flight();

    public static class Flight {
        @Comment("Bedrock-like snappy flight movement")
        @CollapsibleObject(startExpanded = true)
        public SnappyFlight snappyFlight = new SnappyFlight();

        public static final class SnappyFlight {
            public boolean enabled = false;

            @Comment("Whether snappy flight is only active when the player is in noclip")
            @Tooltip(count = 2)
            public boolean onlyInNoClip = true;
        }

        @Comment("The maximum value the flight speed scroll can be set to")
        @Tooltip(count = 2)
        @BoundedDiscrete(min = 4, max = Long.MAX_VALUE)
        public float maxScrolledSpeed = 4;

        @Comment("Whether to enable flight when entering noclip mode")
        @Tooltip(count = 2)
        public boolean enableFlightOnClip = true;

        @Comment("Whether the player is locked flying when in noclip mode")
        @Tooltip(count = 2)
        public boolean flyingLocked = true;
    }

    @CollapsibleObject(startExpanded = true)
    public KeyBehaviors keyBehaviors = new KeyBehaviors();

    public static class KeyBehaviors {
        @Comment("The behavior of the key binding to noclip")
        @Tooltip(count = 2)
        @EnumHandler(option = EnumDisplayOption.BUTTON)
        public KeyBehavior noClip = KeyBehavior.TOGGLE;

        @Comment("The behavior of the key binding to activate flight speed scrolling")
        @Tooltip(count = 2)
        @EnumHandler(option = EnumDisplayOption.BUTTON)
        public KeyBehavior flightSpeedActivation = KeyBehavior.HOLD;
    }

    @Comment("Allow Noclip In")
    @Tooltip()
    @CollapsibleObject(startExpanded = true)
    public AllowIn allowIn = new AllowIn();

    public static class AllowIn {
        public boolean survival = true, creative = true, adventure = true, spectator = true;
    }

    public static ConfigHolder<NoClipConfig> initialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, manager) -> NoClipClient.CONFIG.load());
        ConfigHolder<NoClipConfig> config = AutoConfig.register(NoClipConfig.class, JanksonConfigSerializer::new);
        config.registerSaveListener(NoClipKeybindingsImpl::onConfigSave);
        return config;
    }

    public static Screen createScreen(Screen parent) {
        return AutoConfig.getConfigScreen(NoClipConfig.class, parent).get();
    }
}
