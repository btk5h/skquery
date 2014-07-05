package com.w00tmast3r.skquery.elements.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skriptaddon.skriptplus.util.Particle;
import org.bukkit.Location;
import org.bukkit.event.Event;


@Patterns("spawn %number% of %string% data %number% offset with %number%, %number%, %number% at %locations%")
public class EffDynamicParticles extends Effect {

    private Expression<String> effect;
    private Expression<Number> data, amt, x, y, z;
    private Expression<Location> loc;

    @Override
    protected void execute(Event event) {
        String e = effect.getSingle(event);
        Number d = data.getSingle(event);
        Number a = amt.getSingle(event);
        Number xO = x.getSingle(event);
        Number yO = y.getSingle(event);
        Number zO = z.getSingle(event);
        if(e == null || d == null || a == null || xO == null || yO == null || zO == null) return;
        for (Location l : loc.getAll(event)) {
            Particle.play(e, l, a.intValue(), xO.floatValue(), yO.floatValue(), zO.floatValue(), d.intValue());
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "sound";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        amt = (Expression<Number>) expressions[0];
        effect = (Expression<String>) expressions[1];
        data = (Expression<Number>) expressions[2];
        x = (Expression<Number>) expressions[3];
        y = (Expression<Number>) expressions[4];
        z = (Expression<Number>) expressions[5];
        loc = (Expression<Location>) expressions[6];
        return true;
    }
}
