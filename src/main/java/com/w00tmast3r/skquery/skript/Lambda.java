package com.w00tmast3r.skquery.skript;

import ch.njol.skript.lang.Effect;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;

public class Lambda {

    private final ArrayList<Effect> chain = new ArrayList<Effect>();
    private final boolean isVoid;

    public Lambda(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public Lambda(Effect e) {
        chain.add(e);
        isVoid = false;
    }

    public Lambda add(Lambda e) {
        if (e != null) chain.addAll(e.getChain());
        return this;
    }

    public Collection<Effect> getChain() {
        if (isVoid) return new ArrayList<Effect>();
        return chain;
    }

    public void walk(Event e) {
        for (Effect effect : chain) {
            effect.run(e);
        }
    }

    public boolean isVoid() {
        return isVoid;
    }
}
