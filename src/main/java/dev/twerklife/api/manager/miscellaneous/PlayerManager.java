package dev.twerklife.api.manager.miscellaneous;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.event.EventListener;
import dev.twerklife.api.utilities.IMinecraft;
import dev.twerklife.client.events.EventPacketSend;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class PlayerManager implements IMinecraft, EventListener {
    private boolean switching;
    private boolean sneaking;
    private int slot;
    private int sentPackets;
    private int receivedPackets;

    public PlayerManager() {
        WonderWhale.EVENT_MANAGER.register(this);
    }

    @Override
    public void onPacketSend(EventPacketSend event) {
        if (event.getPacket() instanceof ClientCommandC2SPacket a) {
            if (a.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY) {
                this.sneaking = true;
            } else if (a.getMode() == ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY) {
                this.sneaking = false;
            }
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket b) {
            this.slot = b.getSelectedSlot();
        }
    }

    public boolean isSwitching() {
        return this.switching;
    }

    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getSentPackets() {
        return this.sentPackets;
    }

    public int getReceivedPackets() {
        return this.receivedPackets;
    }
}
