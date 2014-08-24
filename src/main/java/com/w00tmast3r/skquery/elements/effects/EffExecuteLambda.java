package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.skript.Lambda;
import org.bukkit.event.Event;

@Patterns("execute %lambda%")
public class EffExecuteLambda extends Effect {

    private Expression<Lambda> effect;


    @Override
    protected void execute(Event event) {
        Lambda l = effect.getSingle(event);
        if (l == null) return;
        l.walk(event);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "evaluate";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effect = (Expression<Lambda>) expressions[0];
        return true;
    }
}
