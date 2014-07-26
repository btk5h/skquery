package com.w00tmast3r.skquery.elements.events.bukkit;

import com.w00tmast3r.skquery.elements.effects.EffSync;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FunctionEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final String cause;
    private final Object[] params;
    private final EffSync invoker;

    public FunctionEvent(String cause, Object[] params, EffSync invoker) {
        this.cause = cause;
        this.params = params;
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

    public String getCause() {
        return cause;
    }

    public Object[] getParams(){
        return params;
    }
}
