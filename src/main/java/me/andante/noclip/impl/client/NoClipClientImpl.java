package me.andante.noclip.impl.client;

import com.google.common.reflect.Reflection;
import me.andante.noclip.api.client.NoClipClient;
import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.api.client.command.NoClipClientCommand;
import me.andante.noclip.api.client.keybinding.NoClipKeyBindings;
import me.andante.noclip.api.client.render.NoClipHudRenderer;
import me.andante.noclip.impl.client.keybinding.NoClipKeyBindingsImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public final class NoClipClientImpl implements NoClipClient, ClientModInitializer {
    public static final NoClipHudRenderer NOCLIP_HUD_RENDERER = new NoClipHudRenderer();

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {}-CLIENT", MOD_NAME);

        Reflection.initialize(NoClipClient.class, NoClipKeyBindings.class, NoClipManager.class);

        // networking
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID, NoClipManagerImpl::onServerUpdate);
        ClientPlayConnectionEvents.DISCONNECT.register(NoClipManagerImpl::onDisconnect);

        // keybinding
        ClientTickEvents.END_CLIENT_TICK.register(NoClipKeyBindingsImpl::onEndClientTick);
        HudRenderCallback.EVENT.register(NOCLIP_HUD_RENDERER);

        // command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            NoClipClientCommand.register(dispatcher);
        });
    }
}
