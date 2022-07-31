package me.andante.noclip.mixin;

import me.andante.noclip.impl.NoClipAccess;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    /**
     * Makes pistons and shulker boxes ignore clipping entities.
     */
    @Inject(method = "getPistonBehavior", at = @At("HEAD"), cancellable = true)
    private void onGetPistonBehavior(CallbackInfoReturnable<PistonBehavior> cir) {
        Entity that = Entity.class.cast(this);
        if (that instanceof NoClipAccess clippingEntity && clippingEntity.isClipping()) cir.setReturnValue(PistonBehavior.IGNORE);
    }
}
