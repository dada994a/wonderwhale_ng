package dev.twerklife.asm.mixins;

import dev.twerklife.WonderWhale;
import dev.twerklife.client.events.EventLogin;
import dev.twerklife.client.events.EventLogout;
import dev.twerklife.client.events.EventPacketReceive;
import dev.twerklife.client.events.EventPacketSend;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void injectSend(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof LoginHelloC2SPacket) {
            EventLogin event = new EventLogin();
            WonderWhale.EVENT_MANAGER.call(event);
        }
        EventPacketSend event = new EventPacketSend(packet);
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method="channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at=@At(value="HEAD"), cancellable=true)
    public void injectChannelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof DisconnectS2CPacket) {
            EventLogout event = new EventLogout();
            WonderWhale.EVENT_MANAGER.call(event);
        }
        EventPacketReceive event = new EventPacketReceive(packet);
        WonderWhale.EVENT_MANAGER.call(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
