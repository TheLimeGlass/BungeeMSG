package me.fadishawki.bungeemsg.runnables;

public class Cooldown {

	protected long cooldown;

	public Cooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public long getCooldown() {
		return cooldown;
	}

	public boolean onCooldown(long cooldown) {
		return cooldown != -1 && System.currentTimeMillis() - cooldown < this.cooldown;
	}

}
