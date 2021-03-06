package me.fadishawki.bungeemsg.handlers;

import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

import me.fadishawki.bungeemsg.filter.Filter;
import me.fadishawki.bungeemsg.messengers.ActionBar;
import me.fadishawki.bungeemsg.messengers.ChatMessage;
import me.fadishawki.bungeemsg.messengers.MessageList;
import me.fadishawki.bungeemsg.messengers.Title;
import me.fadishawki.bungeemsg.objects.BungeePlayer;
import me.fadishawki.bungeemsg.objects.Variable;
import me.fadishawki.bungeemsg.utils.Utils;

public class Message {

    private Receiver receiver;
    private Sender sender;

    private Variable[] variables;

    private Instance[] instances;

    public Message(Sender sender, Receiver receiver, Variable[] variables, Instance... instances) {
        this.receiver = receiver;
        this.sender = sender;
        this.instances = instances;

        /* Clear redundant Variables */
        List<Variable> usedVariables = new ArrayList<>();
        for (Variable variable : variables) {
            boolean used = false;

            for (Instance instance : instances) {
                if (!instance.hasVariable(variable))
                    continue;

                used = true;
                break;
            }

            if (used)
                usedVariables.add(variable);
        }

        this.variables = usedVariables.toArray(new Variable[0]);
    }

    public Message(Sender sender, Receiver receiver, Message message) {
        this.receiver = receiver;
        this.sender = sender;
        this.instances = new Instance[message.instances.length];

        for (int i = 0; i < message.instances.length; i++) {
            this.instances[i] = message.instances[i].copy();
        }
    }

    /* MESSAGE - METHODS */
    public boolean adjustFilter(Filter filter) {
        boolean success = true;
        for (Instance instance : instances) {
            if (!instance.adjustFilter(filter))
                success = false;
        }
        return success;
    }

    public boolean applyVariables(BungeePlayer receiver) {
        boolean success = true;
        for (Instance instance : instances) {
            if (!instance.applyVariables(receiver, variables))
                success = false;
        }
        return success;
    }

    public boolean send() {
        return receiver.receive(this);
    }

    public boolean send(BungeePlayer receiver) {
        boolean success = true;
        for (Instance instance : instances) {
            if (!instance.send(receiver))
                success = false;
        }
        return success;
    }

    /* GETTERS */
    public Receiver getReceiver() {
        return receiver;
    }

    public Sender getSender() {
        return sender;
    }

    public Instance[] getInstances() {
        return instances;
    }

    public Message copy() {
        return new Message(sender, receiver, this);
    }

    public Message copy(Sender sender, Receiver receiver) {
        return new Message(sender, receiver, this);
    }

    public interface Instance {

        Type getType();

        boolean send(BungeePlayer receiver);

        boolean adjustFilter(Filter filter);

        boolean applyVariables(BungeePlayer receiver, Variable[] variables);

        boolean hasVariable(Variable variable);

        Instance copy();

    }

    public enum Type {

        CHAT("Message") {
            @Override
            public Instance load(Configuration configuration, String path) {
                String message = Utils.color(configuration.getString(path));

                return new ChatMessage(message) {
                    @Override
                    public boolean adjustFilter(Filter filter) {
                        throw new IllegalStateException();
                    }

                    @Override
                    public boolean send(BungeePlayer receiver) {
                        throw new IllegalStateException();
                    }
                };
            }
        },
        ACTION_BAR("ActionBar") {
            @Override
            public Instance load(Configuration configuration, String path) {
                String message = Utils.color(configuration.getString(path + ".ActionBar"));
                int stay = configuration.getInt(path + ".Stay");

                return new ActionBar(message, stay) {
                    @Override
                    public boolean adjustFilter(Filter filter) {
                        throw new IllegalStateException();
                    }

                    @Override
                    public boolean send(BungeePlayer receiver) {
                        throw new IllegalStateException();
                    }
                };
            }
        },
        MESSAGE_LIST("MessageList") {
            @Override
            public Instance load(Configuration configuration, String path) {
                List<String> messageList = new ArrayList<>();
                for (String message : configuration.getStringList(path)) {
                    messageList.add(Utils.color(message));
                }

                return new MessageList(messageList);
            }
        },
        TITLE("Title") {
            @Override
            public Instance load(Configuration configuration, String path) {
                String title = Utils.color(configuration.getString(path + ".Title"));
                String subTitle = Utils.color(configuration.getString(path + ".SubTitle"));
                int fadeIn = configuration.getInt(path + ".FadeIn");
                int stay = configuration.getInt(path + ".Stay");
                int fadeOut = configuration.getInt(path + ".FadeOut");

                return new Title(title, subTitle, fadeIn, stay, fadeOut) {
                    @Override
                    public boolean adjustFilter(Filter filter) {
                        throw new IllegalStateException();
                    }

                    @Override
                    public boolean send(BungeePlayer receiver) {
                        throw new IllegalStateException();
                    }
                };
            }
        };

        public static final Type[] values = values();

        private final String path;

        Type(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public Message.Instance load(Configuration configuration, String path) {
            throw new IllegalStateException();
        }
    }
}
