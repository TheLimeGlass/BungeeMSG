package me.fadishawki.bungeemsg.configurations;

import java.util.Optional;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.md_5.bungee.config.Configuration;

public abstract class ConfigLoader<T> {

	protected final Config.Type type;
	protected String path;
	protected T value;

	public ConfigLoader(Config.Type type) {
		this.type = type;
		this.type.getLoaders().add(this);
	}

	/**
	 * Make loading for instances easy
	 */
	public abstract boolean load(Configuration configuration);

	/**
	 * Load configuration
	 */
	public boolean load(ConfigHandler handler, Config.Type type) {
		Optional<Config> optional = handler.getConfig(type);
		if (!optional.isPresent())
			return false;
		Configuration configuration = handler.getConfig(type).get().getConfiguration();

		// Reset Value
		this.value = null;

		return load(configuration);
	}

	/**
	 * @return The loaded value
	 */
	public abstract T getValue();

}
