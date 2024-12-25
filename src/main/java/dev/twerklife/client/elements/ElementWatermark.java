package dev.twerklife.client.elements;

import dev.twerklife.api.manager.element.Element;
import dev.twerklife.api.manager.element.RegisterElement;
import dev.twerklife.client.events.EventRender2D;
import dev.twerklife.client.modules.client.ModuleColor;
import dev.twerklife.client.values.impl.ValueCategory;
import dev.twerklife.client.values.impl.ValueEnum;
import dev.twerklife.client.values.impl.ValueString;

@RegisterElement(name="Watermark", description="The watermark for the client.")
public class ElementWatermark extends Element {
    private final ValueCategory watermarkCategory = new ValueCategory("Watermark", "The category for the watermark.");
    private final ValueEnum mode = new ValueEnum("Mode", "Mode", "The mode for the watermark.", this.watermarkCategory, Modes.Normal);
    private final ValueString customValue = new ValueString("WatermarkValue", "Value", "The value for the Custom Watermark.", this.watermarkCategory, "WonderWhale");
    private final ValueCategory versionCategory = new ValueCategory("Version", "The category for the version.");
    private final ValueEnum version = new ValueEnum("Version", "Version", "Renders the Version on the watermark.", this.versionCategory, Versions.Normal);

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        this.frame.setWidth(mc.textRenderer.getWidth(this.getText()));
        this.frame.setHeight(mc.textRenderer.fontHeight);
        event.getContext().drawTextWithShadow(mc.textRenderer, this.getText(), (int) this.frame.getX(), (int) this.frame.getY(), ModuleColor.getColor().getRGB());
    }

    private String getText() {
        return (this.mode.getValue().equals(Modes.Custom) ? this.customValue.getValue() : "WonderWhale") + (!this.version.getValue().equals(Versions.None) ? " " + (this.version.getValue().equals(Versions.Normal) ? "v" : "") + "190622" : "");
    }

    public enum Versions {
        None,
        Simple,
        Normal
    }

    public enum Modes {
        Normal,
        Custom
    }
}
