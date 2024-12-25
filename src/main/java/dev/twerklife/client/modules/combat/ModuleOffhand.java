package dev.twerklife.client.modules.combat;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.api.utilities.InventoryUtils;
import dev.twerklife.client.events.EventMotion;
import dev.twerklife.client.values.impl.ValueBoolean;
import dev.twerklife.client.values.impl.ValueEnum;
import dev.twerklife.client.values.impl.ValueNumber;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

@RegisterModule(name="Offhand", description="Automatically switch items to your offhand.", category=Module.Category.COMBAT)
public class ModuleOffhand extends Module {
    ValueEnum mode = new ValueEnum("Mode", "Mode", "Mode for offhand.", Modes.Totem);
    ValueNumber hp = new ValueNumber("Health", "Health", "Health of player", 12.0f, 1.0f, 20.0f);
    ValueNumber fall = new ValueNumber("Fall", "Fall", "Fall distance.", 10, 5, 30);
    ValueBoolean swordGap = new ValueBoolean("SwordGap", "Sword Gap", "Automatically switch to gap when sword.", false);

    @Override
    public void onMotion(EventMotion event) {
        super.onMotion(event);
        if (super.nullCheck()) {
            return;
        }
        if (mc.player.getHealth() + ModuleOffhand.mc.player.getAbsorptionAmount() <= this.hp.getValue().floatValue() || mc.player.fallDistance >= (float)this.fall.getValue().intValue() && !mc.player.isFallFlying()) {
            if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                InventoryUtils.offhandItem(Items.TOTEM_OF_UNDYING);
            }
        } else if (mc.player.getOffHandStack().getItem() instanceof SwordItem && this.swordGap.getValue() && mc.mouse.wasRightButtonClicked()) {
            if (mc.player.getOffHandStack().getItem() != Items.GOLDEN_APPLE) {
                InventoryUtils.offhandItem(Items.GOLDEN_APPLE);
            }
        } else if (this.mode.getValue().equals(Modes.Totem) && mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            InventoryUtils.offhandItem(Items.TOTEM_OF_UNDYING);
        } else if (this.mode.getValue().equals(Modes.Crystal) && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) {
            InventoryUtils.offhandItem(Items.END_CRYSTAL);
        } else if (this.mode.getValue().equals(Modes.Gapple) && mc.player.getOffHandStack().getItem() != Items.GOLDEN_APPLE) {
            InventoryUtils.offhandItem(Items.GOLDEN_APPLE);
        }
    }

    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }

    public enum Modes {
        Totem,
        Crystal,
        Gapple
    }
}