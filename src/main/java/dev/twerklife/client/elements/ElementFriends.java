package dev.twerklife.client.elements;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.element.Element;
import dev.twerklife.api.manager.element.RegisterElement;
import dev.twerklife.client.events.EventRender2D;
import dev.twerklife.client.modules.client.ModuleColor;
import dev.twerklife.client.modules.client.ModuleHUDEditor;
import dev.twerklife.client.values.impl.ValueString;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@RegisterElement(name = "Friends", description = "Gives you a list of friends in your chunk distance.")
public class ElementFriends extends Element {
    private final ValueString name;
    
    public ElementFriends() {
        this.name = new ValueString("Name", "Name", "The name for the group of friends.", "The Goons");
    }
    
    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        ArrayList<PlayerEntity> friends = mc.world.getPlayers().stream()
                .filter(p -> WonderWhale.FRIEND_MANAGER.isFriend(p.getName().getString()))
                .sorted(Comparator.comparing(player -> player.getName().getString()))
                .collect(Collectors.toCollection(ArrayList::new));
        this.frame.setWidth(friends.isEmpty() ? mc.textRenderer.getWidth(this.name.getValue()) : mc.textRenderer.getWidth(friends.get(0).getName()));
        this.frame.setHeight(mc.textRenderer.fontHeight + (friends.isEmpty() ? 0.0f : (1.0f + (mc.textRenderer.fontHeight + 1.0f) * (friends.size() + 1))));
        event.getContext().drawTextWithShadow(mc.textRenderer, this.name.getValue(), (int) this.frame.getX(), (int) this.frame.getY(), ModuleColor.getColor().getRGB());
        int index = 10;
        for (PlayerEntity player : friends) {
            event.getContext().drawTextWithShadow(mc.textRenderer, player.getName().getString(), (int) this.frame.getX(), (int) (this.frame.getY() + index), ModuleHUDEditor.INSTANCE.getSecondColor().getColorIndex());
            index += 10;
        }
    }
    
    public enum Colors {
        Normal,
        White,
        Gray
    }
}