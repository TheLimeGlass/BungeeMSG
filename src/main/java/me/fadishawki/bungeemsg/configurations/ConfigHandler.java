package me.fadishawki.bungeemsg.configurations;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.fadishawki.bungeemsg.utils.ConsoleUtils;

public class ConfigHandler {

	private final Set<Config> configurations = new HashSet<>();
	private Plugin instance;

	public ConfigHandler(Plugin plugin) {
		this.instance = plugin;
	}

	/**
	 * Called when the server starts.
	 */
	public void setup() {
		loadLoaders("me.fadishawki.bungeemsg", "loaders");
		File file = new File(instance.getDataFolder() + "/configs");
		if (!file.exists())
			file.mkdir();
		for (Config.Type type : Config.Type.values()) {
			Config config = new Config(instance, type);
			type.getLoaders().forEach(loader -> loader.load(config.getConfiguration()));
			configurations.add(config);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Set<Class<? extends ConfigLoader<?>>> loadLoaders(String basePackage, String... subPackages) {
		if (subPackages == null)
			return null;
		for (int i = 0; i < subPackages.length; i++) {
			subPackages[i] = subPackages[i].replace('.', '/') + "/";
		}
		Set<Class<? extends ConfigLoader<?>>> loaders = new HashSet<>();
		basePackage = basePackage.replace('.', '/') + "/";
		try {
			JarFile jar = new JarFile(instance.getFile());
			for (Enumeration<JarEntry> jarEntry = jar.entries(); jarEntry.hasMoreElements();) {
				String name = jarEntry.nextElement().getName();
				if (name.startsWith(basePackage) && name.endsWith(".class")) {
					for (String sub : subPackages) {
						if (name.startsWith(sub, basePackage.length())) {
							String clazz = name.replace("/", ".").substring(0, name.length() - 6);
							Class<?> c = Class.forName(clazz, true, instance.getClass().getClassLoader());
							if (ConfigLoader.class.isAssignableFrom(c)) {
								c.newInstance();
								loaders.add((Class<? extends ConfigLoader<?>>) c);
							}
						}
					}
				}
			}
			jar.close();
		} catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException ex) {
			ConsoleUtils.printStackTrace(ex);
		}
		return loaders;
	}

	/**
	 * Reload all configurations on reload
	 */
	public boolean reload() {
		return configurations.parallelStream().allMatch(config -> config.load());
	}
	
	/**
	 * @return Configuration by file type if found.
	 */
	public Optional<Config> getConfig(Config.Type type) {
		return configurations.parallelStream()
				.filter(config -> config.getType() == type)
				.findFirst();
	}

	/**
	 * @return All configurations registered to this handler.
	 */
	public Set<Config> getConfigurations() {
		return configurations;
	}
	
	/**
	 * @return Plugin instance for this ConfigHandler.
	 */
	public Plugin getPlugin() {
		return instance;
	}

}
