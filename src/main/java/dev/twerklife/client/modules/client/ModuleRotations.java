package dev.twerklife.client.modules.client;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.client.values.impl.ValueNumber;

@RegisterModule(name="Rotations", description="Rotations of the client", category=Module.Category.CLIENT)
public class ModuleRotations extends Module {
    public static ModuleRotations INSTANCE;
    public ValueNumber smoothness = new ValueNumber("RotationSmoothness", "Smoothness", "", 60, 1, 100);

    public ModuleRotations() {
        INSTANCE = this;
    }
}
