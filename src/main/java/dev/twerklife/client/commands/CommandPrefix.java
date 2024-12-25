package dev.twerklife.client.commands;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.command.Command;
import dev.twerklife.api.manager.command.RegisterCommand;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.modules.client.ModuleCommands;

@RegisterCommand(name="Prefix", description="Let's you change your command prefix.", syntax="prefix <input>", aliases={"commandprefix", "cmdprefix", "commandp", "cmdp"})
public class CommandPrefix extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            if (args[0].length() > 2) {
                ChatUtils.sendMessage("The prefix must not be longer than 2 characters.", "Prefix");
            } else {
                WonderWhale.COMMAND_MANAGER.setPrefix(args[0]);
                ChatUtils.sendMessage("Prefix set to \"" + ModuleCommands.getSecondColor() + WonderWhale.COMMAND_MANAGER.getPrefix() + ModuleCommands.getFirstColor() + "\"!", "Prefix");
            }
        } else {
            this.sendSyntax();
        }
    }
}
