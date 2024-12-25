package dev.twerklife.client.modules.movement;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.asm.mixins.IEntityVelocityUpdateS2CPacket;
import dev.twerklife.asm.mixins.IExplosionS2CPacket;
import dev.twerklife.client.events.EventPacketReceive;
import dev.twerklife.client.events.EventPush;
import dev.twerklife.client.values.impl.ValueBoolean;
import dev.twerklife.client.values.impl.ValueNumber;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Formatting;

@RegisterModule(name="Velocity", description="Remove the knockback of the player.", category=Module.Category.MOVEMENT)
public class ModuleVelocity extends Module {
    public static ValueBoolean noPush = new ValueBoolean("NoPush", "NoPush", "", false);
    public static ValueNumber horizontal = new ValueNumber("Horizontal", "Horizontal", "", 0.0f, 0.0f, 100.0f);
    public static ValueNumber vertical = new ValueNumber("Vertical", "Vertical", "", 0.0f, 0.0f, 100.0f);

    @Override
    public void onPacketReceive(EventPacketReceive event) {
        EntityVelocityUpdateS2CPacket sPacketEntityVelocity;
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket && (sPacketEntityVelocity = (EntityVelocityUpdateS2CPacket) event.getPacket()).getEntityId() == mc.player.getId()) {
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.cancel();
            } else {
                ((IEntityVelocityUpdateS2CPacket) sPacketEntityVelocity).setX((int) (sPacketEntityVelocity.getVelocityX() * horizontal.getValue().floatValue()));
                ((IEntityVelocityUpdateS2CPacket) sPacketEntityVelocity).setY((int) (sPacketEntityVelocity.getVelocityY() * vertical.getValue().floatValue()));
                ((IEntityVelocityUpdateS2CPacket) sPacketEntityVelocity).setZ((int) (sPacketEntityVelocity.getVelocityZ() * horizontal.getValue().floatValue()));
            }
        }
        if (event.getPacket() instanceof ExplosionS2CPacket sPacketExplosion) {
            if (horizontal.getValue().floatValue() == 0.0f && vertical.getValue().floatValue() == 0.0f) {
                event.cancel();
            } else {
                ((IExplosionS2CPacket) sPacketExplosion).setX((int) (sPacketExplosion.getPlayerVelocityX() * horizontal.getValue().floatValue()));
                ((IExplosionS2CPacket) sPacketExplosion).setY((int) (sPacketExplosion.getPlayerVelocityY() * vertical.getValue().floatValue()));
                ((IExplosionS2CPacket) sPacketExplosion).setZ((int) (sPacketExplosion.getPlayerVelocityZ() * horizontal.getValue().floatValue()));
            }
        }
    }

    @Override
    public void onPush(EventPush event) {
        if (noPush.getValue()) {
            event.cancel();
        }
    }

    @Override
    public String getHudInfo() {
        return "H" + horizontal.getValue().floatValue() + "%" + Formatting.GRAY + "," + Formatting.WHITE + "V" + vertical.getValue().floatValue() + "%";
    }
}
