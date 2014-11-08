package com.w00tmast3r.skquery.elements.events.lang;

import ch.njol.skript.lang.Expression;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomEffectEvent extends Event implements MethodEvent, Pullable {

    private static final HandlerList handlers = new HandlerList();
    private final String match;
    private final Expression<?>[] args;

    public CustomEffectEvent(String match, Expression<?>[] args) {
        this.match = match;
        this.args = args;
    }

    @Override
    public String getMatch() {
        return match;
    }

    @Override
    public Expression<?>[] getArgs() {
        return args;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
