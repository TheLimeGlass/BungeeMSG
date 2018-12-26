package me.fadishawki.bungeemsg.handlers;

public interface Receiver {

    boolean receive(Message message);

    Type getType();

    enum Type {

        PLAYER,
        CHANNEL,
        SERVER;

    }
}
