package dev.twerklife.client.gui.click.components;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.utilities.RenderUtils;
import dev.twerklife.api.utilities.TimerUtils;
import dev.twerklife.client.gui.click.manage.Component;
import dev.twerklife.client.gui.click.manage.Frame;
import dev.twerklife.client.values.impl.ValueString;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StringComponent extends Component {
    private final ValueString value;
    private boolean listening;
    private String currentString = "";
    private final TimerUtils timer = new TimerUtils();
    private boolean selecting = false;
    private boolean line = false;

    public StringComponent(ValueString value, int offset, Frame parent) {
        super(offset, parent);
        this.value = value;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (this.timer.hasTimeElapsed(400L)) {
            this.line = !this.line;
            this.timer.reset();
        }
        RenderUtils.drawRect(context.getMatrices(), this.getX() + 1, this.getY(), this.getX() + this.getWidth() - 1, this.getY() + 14, WonderWhale.CLICK_GUI.getColor());
        if (this.selecting) {
            RenderUtils.drawRect(context.getMatrices(), this.getX() + 3, this.getY() + 3, (float) (this.getX() + 3) + mc.textRenderer.getWidth(this.currentString), (float) this.getY() + mc.textRenderer.fontHeight + 3.0f, new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100));
        }
        if (this.listening) {
            context.drawTextWithShadow(mc.textRenderer, this.currentString + (this.selecting ? "" : (this.line ? "|" : "")), this.getX() + 3, this.getY() + 3, Color.LIGHT_GRAY.getRGB());
        } else {
            context.drawTextWithShadow(mc.textRenderer, this.value.getValue(), this.getX() + 3, this.getY() + 3, Color.LIGHT_GRAY.getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.listening = !this.listening;
            this.currentString = this.value.getValue();
        }
    }

    @Override
    public void charTyped(char typedChar, int keyCode) {
        super.charTyped(typedChar, keyCode);
        if (this.listening) {
            if (keyCode == InputUtil.GLFW_KEY_ENTER) {
                this.updateString();
                this.selecting = false;
                this.listening = false;
            } else if (keyCode == InputUtil.GLFW_KEY_BACKSPACE) {
                this.currentString = this.selecting ? "" : this.removeLastCharacter(this.currentString);
                this.selecting = false;
            } else if (keyCode == InputUtil.GLFW_KEY_V && (InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_RIGHT_CONTROL))) {
                try {
                    this.currentString = this.currentString + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException exception) {
                    exception.printStackTrace();
                }
            } else if (isValidChatCharacter(typedChar)) {
                this.currentString = this.selecting ? "" + typedChar : this.currentString + typedChar;
                this.selecting = false;
            }
            if (keyCode == InputUtil.GLFW_KEY_A && (InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.GLFW_KEY_RIGHT_CONTROL))) {
                this.selecting = true;
            }
        }
    }

    private boolean isValidChatCharacter(char c) {
        return c >= ' ' && c != 127;
    }

    private void updateString() {
        if (this.currentString.length() > 0) {
            this.value.setValue(this.currentString);
        }
        this.currentString = "";
    }

    private String removeLastCharacter(String input) {
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }

    public ValueString getValue() {
        return this.value;
    }
}