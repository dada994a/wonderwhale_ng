package dev.twerklife.client.commands;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.command.Command;
import dev.twerklife.api.manager.command.RegisterCommand;
import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.modules.client.ModuleCommands;
import net.minecraft.util.Formatting;

@RegisterCommand(name="toggle", description="Let's you toggle a module by name.", syntax="toggle <module>", aliases={"t"})
public class CommandToggle extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            boolean found = false;
            for (Module module : WonderWhale.MODULE_MANAGER.getModules()) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                module.toggle(false);
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + "" + Formatting.BOLD + module.getTag() + ModuleCommands.getFirstColor() + " has been toggled " + (module.isToggled() ? Formatting.GREEN + "on" : Formatting.RED + "off") + ModuleCommands.getFirstColor() + "!", "Toggle");
                found = true;
                break;
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Toggle");
            }
        } else {
            this.sendSyntax();
        }
    }
}
