package dev.twerklife.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventRender3D;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void injectRenderPre(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        EventRender3D event = new EventRender3D(tickCounter.getTickDelta(true), new MatrixStack());
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
