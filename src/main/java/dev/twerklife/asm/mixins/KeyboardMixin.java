package dev.twerklife.asm.mixins;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.utilities.IMinecraft;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin implements IMinecraft {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void injectOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key != -1) {
            if (mc.currentScreen == null && action == 1) {
                // for debug thing. we should add bind system later
                if (key == 344) {
                    mc.setScreen(WonderWhale.CLICK_GUI);
                }
            }
        }
    }
}
