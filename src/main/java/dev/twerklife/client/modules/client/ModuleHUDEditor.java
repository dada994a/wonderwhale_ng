package dev.twerklife.client.modules.client;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import net.minecraft.util.Formatting;

@RegisterModule(name="HUDEditor", tag="HUD Editor", description="The client's HUD Editor.", category=Module.Category.CLIENT)
public class ModuleHUDEditor extends Module {
    public static ModuleHUDEditor INSTANCE;

    public ModuleHUDEditor() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null || mc.world == null) {
            this.disable(false);
            return;
        }
        mc.setScreen(WonderWhale.HUD_EDITOR);
    }

    public Formatting getSecondColor() {
        return Formatting.WHITE;
    }

    public enum secondColors {
        Normal,
        Gray,
        DarkGray,
        White
    }
}
