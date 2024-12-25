package dev.twerklife.client.events;

import dev.twerklife.api.manager.event.EventArgument;
import dev.twerklife.api.manager.event.EventListener;
import net.minecraft.network.packet.Packet;

public class EventPacketSend extends EventArgument {
    private final Packet<?> packet;

    public EventPacketSend(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    @Override
    public void call(EventListener listener) {
        listener.onPacketSend(this);
    }
}
