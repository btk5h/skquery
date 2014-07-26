package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Description;
import com.w00tmast3r.skquery.api.Name;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.custom.region.CuboidRegion;
import org.bukkit.Location;
import org.bukkit.event.Event;

@Name("Is Within Cuboid")
@Description("Checks whether or not a certain location is included in a 3d cube with 2 endpoints.")
@Patterns({"%location% is within %location% to %location%",
        "%location% is not within %location% to %location%"})
public class CondIsWithin extends Condition {

    private Expression<Location> loc, pos1, pos2;

    @Override
    public boolean check(Event event) {
        Location p1 = pos1.getSingle(event);
        Location p2 = pos2.getSingle(event);
        Location l = loc.getSingle(event);
        if(loc == null || p1 == null || p2 == null) return isNegated();
        return isNegated() ? new CuboidRegion(p1, p2).checkHas(l.toVector()) : !new CuboidRegion(p1, p2).checkHas(l.toVector());
    }

    @Override
    public String toString(Event event, boolean b) {
        return "is within";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        loc = (Expression<Location>) expressions[0];
        pos1 = (Expression<Location>) expressions[1];
        pos2 = (Expression<Location>) expressions[2];
        setNegated(i == 1);
        return true;
    }
}
