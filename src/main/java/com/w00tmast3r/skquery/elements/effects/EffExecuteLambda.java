package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.skript.LambdaEffect;
import org.bukkit.event.Event;

@Patterns("do %lambda%")
public class EffExecuteLambda extends Effect {

    private Expression<LambdaEffect> effect;


    @Override
    protected void execute(Event event) {
        LambdaEffect l = effect.getSingle(event);
        if (l == null) return;
        l.walk(event);
    }

    @Override
    public String toString(Event event, boolean b) {
        return "evaluate";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effect = (Expression<LambdaEffect>) expressions[0];
        return true;
    }
}
