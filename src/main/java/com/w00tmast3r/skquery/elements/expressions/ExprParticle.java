package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.Collect;
import com.w00tmast3r.skquery.util.packet.particle.Particle;
import com.w00tmast3r.skquery.util.packet.particle.ParticleType;
import org.bukkit.event.Event;

@Patterns("particle %particletype%[:%-number%] [offset (at|by) %-number%, %-number%, %-number%]")
public class ExprParticle extends SimpleExpression<Particle> {

    private Expression<ParticleType> particle;
    private Expression<Number> data, x, y, z;

    @Override
    protected Particle[] get(Event event) {
        ParticleType p = particle.getSingle(event);
        if (p == null) return null;
        Particle product = new Particle(p);
        if (data != null && data.getSingle(event) != null) product.setData(data.getSingle(event).floatValue());
        if (x != null && x.getSingle(event) != null) product.setXOffset(x.getSingle(event).floatValue());
        if (y != null && y.getSingle(event) != null) product.setYOffset(y.getSingle(event).floatValue());
        if (z != null && z.getSingle(event) != null) product.setZOffset(z.getSingle(event).floatValue());
        return Collect.asArray(product);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Particle> getReturnType() {
        return Particle.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "particle";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        particle = (Expression<ParticleType>) expressions[0];
        data = (Expression<Number>) expressions[1];
        x = (Expression<Number>) expressions[2];
        y = (Expression<Number>) expressions[3];
        z = (Expression<Number>) expressions[4];
        return true;
    }
}
