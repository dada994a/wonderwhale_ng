package dev.twerklife;

import dev.twerklife.api.manager.command.CommandManager;
import dev.twerklife.api.manager.element.ElementManager;
import dev.twerklife.api.manager.event.EventManager;
import dev.twerklife.api.manager.friend.FriendManager;
import dev.twerklife.api.manager.miscellaneous.ConfigManager;
import dev.twerklife.api.manager.miscellaneous.PlayerManager;
import dev.twerklife.api.manager.module.ModuleManager;
import dev.twerklife.api.utilities.TPSUtils;
import dev.twerklife.client.gui.click.ClickGuiScreen;
import dev.twerklife.client.gui.hud.HudEditorScreen;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class WonderWhale implements ModInitializer {
	public static final String NAME = "WonderWhale";
	public static final String VERSION = "190622";
	public static final Logger LOGGER = LoggerFactory.getLogger("WonderWhale");

	public static Color COLOR_CLIPBOARD;
	public static CommandManager COMMAND_MANAGER;
	public static EventManager EVENT_MANAGER;
	public static FriendManager FRIEND_MANAGER;
	public static ModuleManager MODULE_MANAGER;
	public static ElementManager ELEMENT_MANAGER;
	public static PlayerManager PLAYER_MANAGER;
	public static ClickGuiScreen CLICK_GUI;
	public static HudEditorScreen HUD_EDITOR;
	public static ConfigManager CONFIG_MANAGER;

	@Override
	public void onInitialize() {
		LOGGER.info("Initialization process for WonderWhale v190622 has started!");
		EVENT_MANAGER = new EventManager();
		COMMAND_MANAGER = new CommandManager();
		FRIEND_MANAGER = new FriendManager();
		MODULE_MANAGER = new ModuleManager();
		ELEMENT_MANAGER = new ElementManager();
		PLAYER_MANAGER = new PlayerManager();
		CLICK_GUI = new ClickGuiScreen();
		HUD_EDITOR = new HudEditorScreen();
		new TPSUtils();
		CONFIG_MANAGER = new ConfigManager();
		CONFIG_MANAGER.load();
		CONFIG_MANAGER.attach();
		LOGGER.info("Initialization process for WonderWhale v190622 has finished!");
	}
}
