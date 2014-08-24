package com.w00tmast3r.skquery.skript;

import ch.njol.skript.lang.Effect;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;

public class Lambda {

    private final ArrayList<Effect> chain = new ArrayList<Effect>();

    public Lambda() {
    }

    public Lambda(Effect e) {
        chain.add(e);
    }

    public Lambda add(Lambda e) {
        chain.addAll(e.getChain());
        return this;
    }

    public Collection<Effect> getChain() {
        return chain;
    }

    public void walk(Event e) {
        for (Effect effect : chain) {
            effect.run(e);
        }
    }
}
