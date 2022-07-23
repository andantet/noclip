package me.andante.noclip.api;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NoClip {
    String MOD_ID   = "noclip";
    String MOD_NAME = "noclip";
    Logger LOGGER   = LoggerFactory.getLogger(MOD_ID);

    String NBT_KEY = NoClip.MOD_ID + ":clipping";
    Identifier PACKET_ID = new Identifier(NoClip.MOD_ID, "update");
}
