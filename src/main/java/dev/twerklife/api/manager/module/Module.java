package dev.twerklife.api.manager.module;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.event.EventListener;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.api.utilities.IMinecraft;
import dev.twerklife.client.events.EventRender2D;
import dev.twerklife.client.events.EventRender3D;
import dev.twerklife.client.modules.client.ModuleCommands;
import dev.twerklife.client.values.Value;
import dev.twerklife.client.values.impl.ValueBind;
import dev.twerklife.client.values.impl.ValueBoolean;
import dev.twerklife.client.values.impl.ValueString;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Module implements IMinecraft, EventListener {
    private ArrayList<Value> values = null;
    public String name;
    public String description;
    public Category category;
    private boolean toggled;
    private boolean persistent = false;
    private Color randomColor;
    public ValueString tag = new ValueString("Tag", "Tag", "The module's display name.", "4GquuoBHl7gkSDaNeMb5");
    public ValueBoolean chatNotify = new ValueBoolean("ChatNotify", "Chat Notify", "Notifies you in chat when the module is toggled on or off.", true);
    public ValueBoolean drawn = new ValueBoolean("Drawn", "Drawn", "Makes the module appear on the array list.", true);
    public ValueBind bind = new ValueBind("Bind", "Bind", "The module's toggle bind.", 0);

    public Module() {
        RegisterModule annotation = this.getClass().getAnnotation(RegisterModule.class);
        if (annotation != null) {
            this.name = annotation.name();
            this.tag.setValue(annotation.tag().equals("4GquuoBHl7gkSDaNeMb5") ? annotation.name() : annotation.tag());
            this.description = annotation.description();
            this.category = annotation.category();
            this.persistent = annotation.persistent();
            this.bind.setValue(annotation.bind());
            this.values = new ArrayList<>();
            if (this.persistent) {
                this.setToggled(true);
                WonderWhale.EVENT_MANAGER.register(this);
            }
            this.randomColor = this.generateRandomColor();
        }
    }

    public Color generateRandomColor() {
        Random random = new Random();
        int randomRed = random.nextInt(255);
        int randomGreen = random.nextInt(255);
        int randomBlue = random.nextInt(255);
        return new Color(randomRed, randomGreen, randomBlue);
    }


    public void onTick() {
    }

    public void onUpdate() {
    }

    public void onRender2D(EventRender2D event) {
    }

    public void onRender3D(EventRender3D event) {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onDeath() {
    }

    public boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

    public String getHudInfo() {
        return "";
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public String getTag() {
        return this.tag.getValue();
    }

    public void setTag(String tag) {
        this.tag.setValue(tag);
    }

    public boolean isChatNotify() {
        return this.chatNotify.getValue();
    }

    public void setChatNotify(boolean chatNotify) {
        this.chatNotify.setValue(chatNotify);
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public int getBind() {
        return this.bind.getValue();
    }

    public void setBind(int bind) {
        this.bind.setValue(bind);
    }

    public Color getRandomColor() {
        return this.randomColor;
    }

    public void toggle(boolean message) {
        if (this.isToggled()) {
            this.disable(message);
        } else {
            this.enable(message);
        }
    }

    public void enable(boolean message) {
        if (!this.persistent) {
            this.setToggled(true);
            WonderWhale.EVENT_MANAGER.register(this);
            if (message) {
                this.doToggleMessage();
            }
            this.onEnable();
        }
    }

    public void disable(boolean message) {
        if (!this.persistent) {
            this.setToggled(false);
            WonderWhale.EVENT_MANAGER.unregister(this);
            if (message) {
                this.doToggleMessage();
            }
            this.onDisable();
        }
    }

    public void doToggleMessage() {
        if (this.isChatNotify()) {
            int number = 0;
            for (char character : this.getTag().toCharArray()) {
                number += character;
                number *= 10;
            }
            ChatUtils.sendMessage(ModuleCommands.getSecondColor() + "" + Formatting.BOLD + this.getTag() + " " + ModuleCommands.getFirstColor() + "has been toggled " + (this.isToggled() ? Formatting.GREEN + "on" : Formatting.RED + "off") + ModuleCommands.getFirstColor() + "!", number);
        }
    }

    public ArrayList<Value> getValues() {
        return this.values;
    }

    public enum Category {
        COMBAT("Combat"),
        PLAYER("Player"),
        MISCELLANEOUS("Miscellaneous"),
        MOVEMENT("Movement"),
        VISUALS("Visuals"),
        CLIENT("Client"),
        HUD("HUD");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
