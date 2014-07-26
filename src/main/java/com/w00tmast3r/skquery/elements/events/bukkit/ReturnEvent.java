package com.w00tmast3r.skquery.elements.events.bukkit;

import ch.njol.skript.lang.Expression;
import com.w00tmast3r.skquery.elements.effects.EffSync;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReturnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Expression<?> args;
    private final EffSync invoker;

    public ReturnEvent(EffSync invoker, Expression<?> args) {
        this.args = args;
        this.invoker = invoker;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public EffSync getInvoker() {
        return invoker;
    }

    public Expression<?> getArgs() {
        return args;
    }
}
