package me.andante.noclip.api.client.config;

import me.andante.noclip.api.NoClip;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

@Environment(EnvType.CLIENT)
@Config(name = NoClip.MOD_ID)
public class NoClipConfig implements ConfigData {
    @Comment("Whether the player is locked flying when clipping")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean flyingLocked = true;

    @Comment("Whether or not the icon displayed at the top-right of the HUD is present")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean hudIcon = true;

    public static ConfigHolder<NoClipConfig> initialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, manager) -> AutoConfig.getConfigHolder(NoClipConfig.class).load());
        return AutoConfig.register(NoClipConfig.class, JanksonConfigSerializer::new);
    }
}
