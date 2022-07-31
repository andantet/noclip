package me.andante.noclip.mixin;

import me.andante.noclip.impl.ClippingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPredicates.class)
public class EntityPredicatesMixin {
    /**
     * Adds an extra check to {@link EntityPredicates#VALID_LIVING_ENTITY} for clipping.
     * <p>This predicate is used when checking for players near spawners and dripstone landing.</p>
     */
    @Inject(method = "method_32878", at = @At("TAIL"), cancellable = true)
    private static void onValidLivingEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            if (entity instanceof ClippingEntity clippingEntity && clippingEntity.isClipping()) cir.setReturnValue(false);
        }
    }
}
