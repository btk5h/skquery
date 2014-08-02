package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.Collect;
import com.w00tmast3r.skquery.util.packet.particle.HoloParticle;
import com.w00tmast3r.skquery.util.packet.particle.Particle;
import org.bukkit.event.Event;

@Patterns("holo particle %string%")
public class ExprHoloParticle extends SimpleExpression<Particle> {

    private Expression<String> name;

    @Override
    protected Particle[] get(Event event) {
        String p = name.getSingle(event);
        if (p == null) return null;
        return Collect.asArray(new HoloParticle(p));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Particle> getReturnType() {
        return HoloParticle.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "particle";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        return true;
    }
}
