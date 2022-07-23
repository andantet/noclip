package me.andante.noclip.mixin;

import me.andante.noclip.api.NoClip;
import me.andante.noclip.impl.NoClipAccess;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements NoClipAccess {
    @Unique private boolean clipping;

    private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Unique
    @Override
    public boolean isClipping() {
        return this.clipping;
    }

    @Unique
    @Override
    public void setClipping(boolean clipping) {
        this.clipping = clipping;
    }

    /**
     * Updates the player's no-clip value based on our custom parameters.
     */
    @Inject(
        method = "tick",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z",
            shift = At.Shift.AFTER
        )
    )
    private void onTickAfterNoClip(CallbackInfo ci) {
        if (this.isClipping()) {
            this.noClip = true;
            this.onGround = false;
        }
    }

    /**
     * Prevents the player's post from updating when no-clipping.
     */
    @Inject(
        method = "updatePose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void onUpdatePose(CallbackInfo ci, EntityPose pose) {
        if (this.isClipping()) {
            this.setPose(pose);
            ci.cancel();
        }
    }

    /* NBT */

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean(NoClip.NBT_KEY, this.isClipping());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.setClipping(nbt.getBoolean(NoClip.NBT_KEY));
    }
}
