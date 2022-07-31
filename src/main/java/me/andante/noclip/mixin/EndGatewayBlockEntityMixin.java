package me.andante.noclip.mixin;

import me.andante.noclip.impl.NoClipAccess;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    /**
     * Prevents end gateway teleportation when an entity is clipping.
     */
    @Inject(method = "canTeleport", at = @At("RETURN"), cancellable = true)
    private static void onCanTeleport(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            if (entity instanceof NoClipAccess clippingPlayer && clippingPlayer.isClipping()) cir.setReturnValue(false);
        }
    }
}
