package me.andante.noclip.impl.client;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.impl.ClippingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public final class NoClipManagerImpl implements NoClipManager {
    private boolean clipping;
    private boolean canClip;

    @Override
    public boolean isClipping() {
        return this.canClip && this.clipping;
    }

    @Override
    public boolean setClipping(boolean clipping) {
        this.clipping = clipping;
        return this.clipping;
    }

    @Override
    public boolean canClip() {
        return this.canClip;
    }

    @Override
    public void setCanClip(boolean canClip) {
        this.canClip = canClip;
        if (!this.canClip) this.updateClipping();
    }

    @Override
    public void updateClipping() {
        boolean clipping = this.isClipping();

        // update client player
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            ClippingEntity clippingPlayer = ClippingEntity.cast(client.player);
            clippingPlayer.setClipping(clipping);
        }

        // send to server
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(clipping);
        ClientPlayNetworking.send(NoClip.PACKET_ID, buf);
    }

    /* Events */

    /**
     * Receives a clipping update from the server.
     */
    public static void onServerUpdate(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        NoClipManager clipping = NoClipManager.INSTANCE;
        clipping.setCanClip(true);
        clipping.setClipping(buf.readBoolean());
    }

    /**
     * Resets clipping value upon disconnecting a server.
     */
    public static void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        NoClipManager clipping = NoClipManager.INSTANCE;
        clipping.setClipping(false);
        clipping.setCanClip(false);
    }
}
