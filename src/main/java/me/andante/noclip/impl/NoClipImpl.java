package me.andante.noclip.impl;

import me.andante.noclip.api.NoClip;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NoClipImpl implements NoClip, ModInitializer {
    @Override
    public void onInitialize() {
        // networking
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, this::receiveUpdate);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);

        // death
        ServerPlayerEvents.COPY_FROM.register(this::copyFrom);
    }

    /**
     * Updates the client player on server join.
     */
    private void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        ClippingEntity clippingPlayer = ClippingEntity.cast(handler.player);
        buf.writeBoolean(clippingPlayer.isClipping());
        ServerPlayNetworking.send(handler.player, PACKET_ID, buf);
    }

    /**
     * Receives a clipping update from the client.
     */
    private void receiveUpdate(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        boolean clipping = buf.readBoolean();
        ClippingEntity clippingPlayer = ClippingEntity.cast(player);
        clippingPlayer.setClipping(clipping);
    }

    /**
     * Copies data from a dead player to a new player.
     */
    private void copyFrom(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        ClippingEntity clippingOldPlayer = ClippingEntity.cast(oldPlayer);
        ClippingEntity clippingNewPlayer = ClippingEntity.cast(newPlayer);
        clippingNewPlayer.setClipping(clippingOldPlayer.isClipping());
    }
}
