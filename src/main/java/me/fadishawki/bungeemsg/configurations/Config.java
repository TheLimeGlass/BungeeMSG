package me.fadishawki.bungeemsg.configurations;

import com.google.common.base.Charsets;

import me.fadishawki.bungeemsg.utils.ConsoleUtils;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Config {

	public enum Type {

		ANNOUNCER("Announcer");

		private final Set<ConfigLoader<?>> loaders = new HashSet<>();
		private final String name;

		Type(String name) {
			this.name = name;
		}

		public Set<ConfigLoader<?>> getLoaders() {
			return loaders;
		}

		/**
		 * Load all configuration values.
		 * @return If the load was successful.
		 */
		public boolean load(ConfigHandler handler) {
			return loaders.parallelStream().allMatch(loader -> loader.load(handler, this));
		}

		public String getFileName() {
			return name + ".yml";
		}
	}

	//public static final AnnouncerLoader ANNOUNCERS = new AnnouncerLoader(Type.ANNOUNCER);
	private Configuration configuration;
	private final Config.Type type;
	private File cachedFile;

	Config(Plugin plugin, Config.Type type) {
		this.type = type;

		cachedFile = new File(plugin.getDataFolder() + "/configs", type.getFileName());

		if (!cachedFile.exists()) {
			try {
				Files.copy(plugin.getResourceAsStream(type.getFileName()), cachedFile.toPath());
			} catch (IOException ex) {
				ConsoleUtils.printStackTrace(ex);
			}
		}

		load();
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public File getCachedFile() {
		return cachedFile;
	}
	
	public Type getType() {
		return type;
	}

	public boolean save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, cachedFile);
			return true;
		} catch(IOException ex) {
			ConsoleUtils.printStackTrace(ex);
			return false;
		}
	}

	public boolean load() {
		try {
			configuration = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(cachedFile), Charsets.UTF_8));
			return true;
		} catch (IOException ex) {
			ConsoleUtils.printStackTrace(ex);
			return false;
		}
	}

}
