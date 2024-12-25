package dev.twerklife.client.gui.click.components;

import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.gui.click.manage.Component;
import dev.twerklife.client.gui.click.manage.Frame;
import dev.twerklife.client.values.impl.ValueBind;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class BindComponent extends Component {
    private final ValueBind value;
    private boolean binding;

    public BindComponent(ValueBind value, int offset, Frame parent) {
        super(offset, parent);
        this.value = value;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        String keyName = this.value.getValue() == 0 ? "NONE" : GLFW.glfwGetKeyName(this.value.getValue(), 0);
        context.drawTextWithShadow(mc.textRenderer,
                this.value.getTag() + " " + Formatting.GRAY + (this.binding ? "..." : keyName),
                this.getX() + 3,
                this.getY() + 3,
                -1
        );
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight()) {
            this.binding = !this.binding;
        }
    }

    @Override
    public void charTyped(char typedChar, int keyCode) {
        super.charTyped(typedChar, keyCode);
        if (this.binding) {
            if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                this.value.setValue(0);
            } else if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                this.value.setValue(keyCode);
            }
            this.binding = false;
        }
    }
}
