package me.andante.noclip.impl;

import net.minecraft.entity.player.PlayerEntity;

public interface NoClipAccess {
    boolean isClipping();
    void setClipping(boolean clipping);

    static NoClipAccess cast(PlayerEntity player) {
        return (NoClipAccess) player;
    }
}
