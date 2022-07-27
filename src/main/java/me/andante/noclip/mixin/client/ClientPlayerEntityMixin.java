package me.andante.noclip.mixin.client;

import com.mojang.authlib.GameProfile;
import me.andante.noclip.api.client.NoClipManager;
import me.andante.noclip.impl.NoClipAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.stat.StatHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    /**
     * Updates player clipping value based on set/received client value.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructor(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler handler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
        NoClipAccess clippingPlayer = NoClipAccess.cast(this);
        clippingPlayer.setClipping(NoClipManager.INSTANCE.isClipping());
    }
}
