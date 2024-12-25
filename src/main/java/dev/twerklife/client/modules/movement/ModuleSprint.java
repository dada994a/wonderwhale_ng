package dev.twerklife.client.modules.movement;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;

@RegisterModule(name="Sprint", tag="Sprint", description="Always be sprinting.", category=Module.Category.MOVEMENT)
public class ModuleSprint extends Module {
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (mc.player.forwardSpeed > 0.0f && !mc.player.horizontalCollision) {
            mc.player.setSprinting(true);
        }
    }
}
