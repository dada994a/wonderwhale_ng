package dev.twerklife.api.manager.element;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.event.EventListener;
import dev.twerklife.client.elements.ElementFriends;
import dev.twerklife.client.elements.ElementPlayerList;
import dev.twerklife.client.elements.ElementWatermark;
import dev.twerklife.client.values.Value;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ElementManager implements EventListener {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    private final ArrayList<Element> elements;

    public ElementManager() {
        WonderWhale.EVENT_MANAGER.register(this);
        this.elements = new ArrayList<>();
        //write elements here
        this.register(new ElementFriends());
        this.register(new ElementPlayerList());
        this.register(new ElementWatermark());
    }

    public void register(Element element) {
        try {
            for (Field field : element.getClass().getDeclaredFields()) {
                if (!Value.class.isAssignableFrom(field.getType())) continue;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                element.getValues().add((Value)field.get(element));
            }
            this.elements.add(element);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Element> getElements() {
        return this.elements;
    }

    public Element getElement(String name) {
        for (Element module : this.elements) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public boolean isElementEnabled(String name) {
        Element module = this.elements.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
        if (module != null) {
            return module.isToggled();
        }
        return false;
    }
}
