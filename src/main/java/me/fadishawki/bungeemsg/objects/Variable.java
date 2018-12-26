package me.fadishawki.bungeemsg.objects;

import me.fadishawki.bungeemsg.handlers.Message;

public abstract class Variable {

    private final String variable;

    public Variable(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }

    public abstract String getReplacement(Message message);
}
