package dev.twerklife.client.modules.miscellaneous;

import dev.twerklife.api.manager.module.Module;
import dev.twerklife.api.manager.module.RegisterModule;
import dev.twerklife.api.utilities.ChatUtils;
import dev.twerklife.client.modules.client.ModuleCommands;
import dev.twerklife.client.values.impl.ValueBoolean;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.Random;

@RegisterModule(name = "Welcomer", tag = "Welcomer", description = "Sends a message when a player joins or leaves the server.", category = Module.Category.MISCELLANEOUS)
public class ModuleWelcomer extends Module {
    ValueBoolean privateMsg;
    static ArrayList<PlayerListEntry> playerMap = new ArrayList<>();
    static int cachePlayerCount;
    boolean isOnServer;
    public static ArrayList<String> joinMessages = new ArrayList<>();
    public static ArrayList<String> leaveMessages = new ArrayList<>();
    
    public ModuleWelcomer() {
        this.privateMsg = new ValueBoolean("Private", "Private", "Send the message as a client notification.", false);
        joinMessages.add("Hello, ");
        joinMessages.add("Welcome to the server, ");
        joinMessages.add("Nice to see you, ");
        joinMessages.add("Hey how are you, ");

        leaveMessages.add("Goodbye, ");
        leaveMessages.add("See you later, ");
        leaveMessages.add("Bye bye, ");
        leaveMessages.add("I hope you had a good time, ");
    }
    
    @Override
    public void onUpdate() {
        if (mc.player != null) {
            if (mc.player.age % 10 == 0) {
                this.checkPlayers();
            }
            else if (mc.isInSingleplayer()) {
                this.toggle(true);
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (ModuleWelcomer.mc.player == null || ModuleWelcomer.mc.world == null) {
            return;
        }
        this.onJoinServer();
    }

    private void checkPlayers() {
        ArrayList<PlayerListEntry> infoMap = new ArrayList(mc.getNetworkHandler().getPlayerList());
        int currentPlayerCount = infoMap.size();
        if (currentPlayerCount != cachePlayerCount) {
            ArrayList<PlayerListEntry> currentInfoMap = (ArrayList)infoMap.clone();
            currentInfoMap.removeAll(playerMap);
            if (currentInfoMap.size() > 5) {
                cachePlayerCount = playerMap.size();
                this.onJoinServer();
                return;
            }
            ArrayList<PlayerListEntry> playerMapClone = (ArrayList)playerMap.clone();
            playerMapClone.removeAll(infoMap);
            for (PlayerListEntry npi : currentInfoMap) {
                this.playerJoined(npi);
            }
            for (PlayerListEntry npi : playerMapClone) {
                this.playerLeft(npi);
            }
            cachePlayerCount = playerMap.size();
            this.onJoinServer();
        }
    }

    private void onJoinServer() {
        if (mc.getNetworkHandler() == null) {
            return;
        }

        playerMap = new ArrayList<>(mc.getNetworkHandler().getPlayerList());
        cachePlayerCount = playerMap.size();
        this.isOnServer = true;
    }

    protected void playerJoined(PlayerListEntry playerInfo) {
        Random random = new Random();
        if (!joinMessages.isEmpty()) {
            String message = joinMessages.get(random.nextInt(joinMessages.size())) + playerInfo.getProfile().getName();
            if (this.privateMsg.getValue()) {
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + message + ModuleCommands.getFirstColor());
            } else {
                mc.getNetworkHandler().sendChatMessage(message);
            }
        }
    }

    protected void playerLeft(PlayerListEntry playerInfo) {
        Random random = new Random();
        if (!leaveMessages.isEmpty()) {
            String message = leaveMessages.get(random.nextInt(leaveMessages.size())) + playerInfo.getProfile().getName();
            if (this.privateMsg.getValue()) {
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + message + ModuleCommands.getFirstColor());
            } else {
                mc.getNetworkHandler().sendChatMessage(message);
            }
        }
    }
}