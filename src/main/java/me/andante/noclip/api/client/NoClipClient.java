package me.andante.noclip.api.client;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.config.NoClipConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface NoClipClient extends NoClip {
    ConfigHolder<NoClipConfig> CONFIG = NoClipConfig.initialize();

    static NoClipConfig getConfig() {
        return CONFIG.getConfig();
    }
}
