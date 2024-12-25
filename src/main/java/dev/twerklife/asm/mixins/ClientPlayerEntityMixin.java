package dev.twerklife.asm.mixins;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventMotion;
import dev.twerklife.client.events.EventPush;
import dev.twerklife.client.events.EventTick;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Unique
    private EventMotion eventMotion = new EventMotion();

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    public void injectSendMovementPacketsPre(CallbackInfo ci) {
        EventTick event = new EventTick();
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.BEFORE), cancellable = true)
    private void onUpdateWalkingPlayerPre(CallbackInfo info) {
        this.eventMotion = new EventMotion(((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch());
        WonderWhale.EVENT_MANAGER.call(this.eventMotion);
        if (this.eventMotion.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.AFTER))
    private void onUpdateWalkingPlayerPost(CallbackInfo info) {
        if (this.eventMotion.isRotated()) {
            ((ClientPlayerEntity) (Object) this).setYaw(this.eventMotion.getRotationYaw());
            ((ClientPlayerEntity) (Object) this).setPitch(this.eventMotion.getRotationPitch());
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double x, double d, CallbackInfo ci) {
        EventPush event = new EventPush();
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}