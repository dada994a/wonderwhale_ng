package dev.twerklife.client.modules.client;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.client.values.impl.ValueCategory;
import dev.twerklife.client.values.impl.ValueColor;
import dev.twerklife.client.values.impl.ValueNumber;

import java.awt.*;

@RegisterModule(name="Color", description="Manages the client's global color.", category=Module.Category.CLIENT, persistent=true)
public class ModuleColor extends Module {
    public static ModuleColor INSTANCE;
    public final ValueColor color = new ValueColor("Color", "Color", "The client's global color.", new Color(255, 0, 0));
    ValueCategory rainbowCategory = new ValueCategory("Rainbow", "Manage rainbow");
    public ValueNumber rainbowOffset = new ValueNumber("RainbowOffset", "Offset", "", this.rainbowCategory, 255, 0, 1000);
    public ValueNumber rainbowSat = new ValueNumber("RainbowSaturation", "Saturation", "", this.rainbowCategory, 255, 0, 255);
    public ValueNumber rainbowBri = new ValueNumber("RainbowBrightness", "Brightness", "", this.rainbowCategory, 255, 0, 255);

    public ModuleColor() {
        INSTANCE = this;
    }

    public static Color getColor() {
        Color color = INSTANCE == null ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    public static Color getColor(int alpha) {
        Color color = INSTANCE == null ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}