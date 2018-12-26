package me.fadishawki.bungeemsg;

import java.util.Optional;

import me.fadishawki.bungeemsg.configurations.Config;
import me.fadishawki.bungeemsg.configurations.ConfigHandler;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMSG extends Plugin {

	private static BungeeMSG instance;

	private ConfigHandler configHandler;

	@Override
	public void onEnable() {
		instance = this;
		configHandler = new ConfigHandler(this);
		configHandler.setup();
	}
	
	public Optional<Config> getConfiguration(Config.Type type) {
		return configHandler.getConfig(type);
	}

	public ConfigHandler getConfigHandler() {
		return configHandler;
	}
	
	public static BungeeMSG getInstance() {
		return instance;
	}

}
