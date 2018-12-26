package me.fadishawki.bungeemsg.handlers;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import me.fadishawki.bungeemsg.BungeeMSG;
import me.fadishawki.bungeemsg.ScrollerList;
import me.fadishawki.bungeemsg.objects.BungeePlayer;
import me.fadishawki.bungeemsg.runnables.BungeeRunnable;
import me.fadishawki.bungeemsg.runnables.Timer;

public class Announcer implements Sender {

	private final ScrollerList<Message> messages;
	private final List<String> servers;
	private final int interval;
	private final Plugin plugin;
	private final String name;
	private Timer timer;

	public Announcer(String name, List<String> servers, int interval, List<Message> messages) {
		this.servers = servers.contains("[ALL]") ? null : servers;
		this.messages = new ScrollerList<>(messages);
		this.plugin = BungeeMSG.getInstance();
		this.interval = interval;
		this.name = name;
		startTimer();
	}

	public int getInterval() {
		return interval;
	}
	
	public String getName() {
		return name;
	}

	public void cancel() {
		if (this.timer != null)
			this.timer.cancel();
	}

	private void startTimer() {
		this.timer = new Timer(plugin, new BungeeRunnable.Time(BungeeRunnable.TimeUnit.SECOND, interval)) {
			@Override
			public void onFinish() {
				Message message = messages.next();

				for (BungeePlayer player : getPlayers()) {
					message.copy(Announcer.this, player).send();
				}

				restart();
			}
		};
	}

	private List<BungeePlayer> getPlayers() {
		if (servers == null)
			return BungeePlayer.getPlayers();

		List<BungeePlayer> players = new ArrayList<>();
		for (BungeePlayer player : BungeePlayer.getPlayers()) {
			if (this.servers.contains(player.getConnectedServer().getServer().getName()))
				players.add(player);
		}

		return players;
	}
}
