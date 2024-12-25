package dev.twerklife.client.elements;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.element.Element;
import dev.twerklife.api.manager.element.RegisterElement;
import dev.twerklife.client.events.EventRender2D;
import dev.twerklife.client.modules.client.ModuleColor;
import dev.twerklife.client.values.impl.ValueNumber;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@RegisterElement(name="PlayerList", tag="Player List", description="Shows the players that are near")
public class ElementPlayerList extends Element {
    ValueNumber maxPlayers = new ValueNumber("MaxPlayers", "Max Players", "Max players that can be shown in the player list.", 8, 3, 20);

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        if (this.nullCheck()) {
            return;
        }
        ArrayList<PlayerEntity> players = new ArrayList<>();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || !player.isAlive() || mc.player.getHealth() <= 0.0f) continue;
            players.add(player);
        }
        int i = 0;
        for (PlayerEntity player : players.stream().sorted(Comparator.comparing(p -> mc.player.distanceTo(p))).collect(Collectors.toList())) {
            if (i + 1 > this.maxPlayers.getValue().intValue()) continue;
            event.getContext().drawTextWithShadow(mc.textRenderer, "" + this.getHealthColor(player) + (int)(player.getHealth() + player.getAbsorptionAmount()) + " " + (WonderWhale.FRIEND_MANAGER.isFriend(player.getName().getString()) ? Formatting.AQUA : Formatting.RESET) + player.getName() + " " + this.getDistanceColor(player) + (int)mc.player.distanceTo(player), (int) this.frame.getX(), (int) (this.frame.getY() + (float)(10 * i)), ModuleColor.getColor().getRGB());
            ++i;
        }
        float longestName = 0.0f;
        for (PlayerEntity entityPlayer : players) {
            StringBuilder stringBuilder = new StringBuilder().append("").append(this.getHealthColor(entityPlayer)).append((int)(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount())).append(" ");
            Formatting chatFormatting = WonderWhale.FRIEND_MANAGER.isFriend(entityPlayer.getName().getString()) ? Formatting.AQUA : Formatting.RESET;
            String text = stringBuilder.append(chatFormatting).append(entityPlayer.getName()).append(" ").append(this.getDistanceColor(entityPlayer)).append((int)mc.player.distanceTo(entityPlayer)).toString();
            if (!(mc.textRenderer.getWidth(text) > longestName)) continue;
            longestName = mc.textRenderer.getWidth(text);
        }
        this.frame.setWidth(longestName);
        this.frame.setHeight(10 * i);
    }

    public Formatting getHealthColor(LivingEntity player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 5.0f) {
            return Formatting.RED;
        }
        if (player.getHealth() + player.getAbsorptionAmount() > 5.0f && player.getHealth() + player.getAbsorptionAmount() < 15.0f) {
            return Formatting.YELLOW;
        }
        if (player.getHealth() + player.getAbsorptionAmount() >= 15.0f) {
            return Formatting.GREEN;
        }
        return Formatting.WHITE;
    }

    public Formatting getDistanceColor(LivingEntity player) {
        if (mc.player.distanceTo(player) < 20.0f) {
            return Formatting.RED;
        }
        if (mc.player.distanceTo(player) >= 20.0f && mc.player.distanceTo(player) < 50.0f) {
            return Formatting.YELLOW;
        }
        if (mc.player.distanceTo(player) >= 50.0f) {
            return Formatting.GREEN;
        }
        return Formatting.WHITE;
    }
}