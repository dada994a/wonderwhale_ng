package dev.twerklife.asm.mixins;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventChatSend;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        EventChatSend event = new EventChatSend(message);
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
