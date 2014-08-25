package com.w00tmast3r.skquery.elements.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Patterns({"%boolean%"})
public class CondBoolean extends Condition {

    private Expression<Boolean> bool;

    @Override
    public boolean check(Event e) {
        return bool.check(e, new Checker<Boolean>() {
            @Override
            public boolean check(Boolean o) {
                return o;
            }
        });
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "bool condition";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        bool = (Expression<Boolean>) exprs[0];
        return true;
    }
}
