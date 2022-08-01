package me.andante.noclip.mixin;

import me.andante.noclip.impl.ClippingEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin {
    /**
     * Prevents clipping players from being affected by AECs.
     */
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"
        ),
        index = 0
    )
    private <K> K onAECContainsKey(K key) {
        PlayerEntity player = (PlayerEntity) key;
        ClippingEntity clippingPlayer = ClippingEntity.cast(player);
        return clippingPlayer.isClipping() ? null : key;
    }
}
