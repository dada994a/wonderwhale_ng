package dev.twerklife.client.commands;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.command.Command;
import dev.twerklife.api.manager.command.RegisterCommand;
import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.modules.client.ModuleCommands;

@RegisterCommand(name="tag", description="Let's you customize any module's tag.", syntax="tag <module> <value>", aliases={"customname", "modtag", "moduletag", "mark"})
public class CommandTag extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 2) {
            boolean found = false;
            for (Module module : WonderWhale.MODULE_MANAGER.getModules()) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                found = true;
                module.setTag(args[1].replace("_", " "));
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + module.getName() + ModuleCommands.getFirstColor() + " is now marked as " + ModuleCommands.getSecondColor() + module.getTag() + ModuleCommands.getFirstColor() + ".", "Tag");
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Tag");
            }
        } else {
            this.sendSyntax();
        }
    }
}