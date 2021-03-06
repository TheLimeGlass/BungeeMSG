package me.fadishawki.bungeemsg.messengers;

import java.util.ArrayList;
import java.util.List;

import me.fadishawki.bungeemsg.filter.Filter;
import me.fadishawki.bungeemsg.handlers.Message;
import me.fadishawki.bungeemsg.objects.BungeePlayer;
import me.fadishawki.bungeemsg.objects.Variable;

public class MessageList implements Message.Instance {

    private List<ChatMessage> messages;

    public MessageList(List<String> messages) {
        this.messages = new ArrayList<>();

        for (String message : messages) {
            this.messages.add(new ChatMessage(message));
        }
    }

    public MessageList(MessageList messageList) {
        this.messages = new ArrayList<>();

        for (ChatMessage message : messageList.messages) {
            messages.add((ChatMessage) message.copy());
        }
    }

    @Override
    public Message.Type getType() {
        return Message.Type.MESSAGE_LIST;
    }

    @Override
    public boolean send(BungeePlayer receiver) {
        boolean sent = true;
        for (ChatMessage message : messages) {
            if (!message.send(receiver))
                sent = false;
        }
        return sent;
    }

    @Override
    public boolean adjustFilter(Filter filter) {
        boolean modified = false;
        for (ChatMessage message : messages) {
            modified = message.adjustFilter(filter);
        }
        return modified;
    }

    @Override
    public boolean applyVariables(BungeePlayer receiver, Variable[] variables) {
        //TODO
        return true;
    }

    @Override
    public boolean hasVariable(Variable variable) {
        for (ChatMessage message : messages) {
            if (message.hasVariable(variable))
                return true;
        }
        return false;
    }

    @Override
    public Message.Instance copy() {
        return new MessageList(this);
    }

    public void addChatMessage(ChatMessage message) {
        this.messages.add(message);
    }
}
