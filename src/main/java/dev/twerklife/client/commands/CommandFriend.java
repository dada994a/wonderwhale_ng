package dev.twerklife.client.commands;

import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.command.Command;
import dev.twerklife.api.manager.command.RegisterCommand;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.modules.client.ModuleCommands;
import net.minecraft.util.Formatting;

@RegisterCommand(name="Friend", description="Let's you add friends.", syntax="friend <add/del> <name>", aliases={"friend", "f"})
public class CommandFriend extends Command {
    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ChatUtils.sendMessage("You have " + (WonderWhale.FRIEND_MANAGER.getFriends().size() + 1) + " friends");
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (WonderWhale.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " is already a friend!");
                    return;
                }
                if (!WonderWhale.FRIEND_MANAGER.isFriend(args[1])) {
                    WonderWhale.FRIEND_MANAGER.addFriend(args[1]);
                    ChatUtils.sendMessage(Formatting.GREEN + "Added " + ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " to friends list");
                }
            }
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
                if (!WonderWhale.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " is not a friend!");
                    return;
                }
                if (WonderWhale.FRIEND_MANAGER.isFriend(args[1])) {
                    WonderWhale.FRIEND_MANAGER.removeFriend(args[1]);
                    ChatUtils.sendMessage(Formatting.RED + "Removed " + ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " from friends list");
                }
            }
        } else {
            this.sendSyntax();
        }
    }
}