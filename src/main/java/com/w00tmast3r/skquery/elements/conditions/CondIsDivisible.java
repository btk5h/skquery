package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.event.Event;


@Patterns({"%number% is divisible by %number%",
        "%number% is not divisible by %number%"})
public class CondIsDivisible extends Condition {

    private Expression<Number> num, div;

    @Override
    public boolean check(final Event event) {
        return num.check(event, new Checker<Number>() {
            @Override
            public boolean check(final Number in) {
                return ((in.floatValue() % div.getSingle(event).floatValue()) == 0);
            }
        }, isNegated());
    }

    @Override
    public String toString(Event event, boolean b) {
        return "is prime";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        num = (Expression<Number>) expressions[0];
        div = (Expression<Number>) expressions[1];
        setNegated(i == 1);
        return true;
    }
}
