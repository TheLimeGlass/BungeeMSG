package me.fadishawki.bungeemsg.objects;

import java.util.ArrayList;
import java.util.List;

import me.fadishawki.bungeemsg.handlers.Message;
import me.fadishawki.bungeemsg.handlers.Receiver;

public class Channel implements Receiver {

    private List<BungeePlayer> players;

    private String name;

    public Channel(String name){
        this.name = name;
        this.players = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<BungeePlayer> getPlayers() {
        return players;
    }

    @Override
    public boolean receive(Message message) {
        boolean sent = true;
        for (BungeePlayer player : players) {
            if (!player.receive(message.copy()))
                sent = false;
        }
        return sent;
    }

    @Override
    public Type getType() {
        return Type.CHANNEL;
    }
}
