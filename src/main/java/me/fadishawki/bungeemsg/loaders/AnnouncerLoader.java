package me.fadishawki.bungeemsg.loaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.fadishawki.bungeemsg.configurations.*;
import me.fadishawki.bungeemsg.handlers.Announcer;
import me.fadishawki.bungeemsg.handlers.Message;
import me.fadishawki.bungeemsg.handlers.Message.Instance;
import net.md_5.bungee.config.Configuration;

public class AnnouncerLoader extends ConfigLoader<Set<Announcer>> {

	private final Set<Announcer> announcers = new HashSet<>();
	
	public AnnouncerLoader() {
		super(Config.Type.ANNOUNCER);
	}

	@Override
	public boolean load(Configuration configuration) {
		for (String node : configuration.getSection("Announcers").getKeys()) {
			String name = node;
			path = "Announcers." + name;
			List<String> servers = configuration.getStringList(path + ".Servers");
			int interval = configuration.getInt(path + ".Interval", 100);
			List<Message> messages = new ArrayList<>();
			List<Instance> instances = new ArrayList<>();
			for (String index : configuration.getSection(path + ".Messages").getKeys()) {
				path = path + ".Messages.";
				for (String type : configuration.getSection(path + index).getKeys()) {
					Message.Type messageType = Message.Type.valueOf(type);
					if (messageType != null) {
						path = path + index + "." + type;
						instances.add(messageType.load(configuration, path));
					}
					messages.add(new Message(null, null, null, instances.toArray(new Instance[instances.size()])));
				}
			}
			announcers.add(new Announcer(name, servers, interval, messages));
		}
		return true;
	}

	@Override
	public Set<Announcer> getValue() {
		return announcers;
	}

}
