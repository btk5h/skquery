package com.w00tmast3r.skquery.elements.lang;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;


@Patterns({"invoke %string%",
        "invoke %string% from %objects%"})
public class EffInvoke extends Effect {

    private Expression<String> str;
    private Expression<Object> args = null;

    @Override
    protected void execute(Event event) {
        String s = str.getSingle(event);
        if(s == null) return;
        Bukkit.getPluginManager().callEvent(new RoutineEvent(s, args == null ? null : args.getAll(event)));
    }

    @Override
    public String toString(Event event, boolean b) {
        return "custom event";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        str = (Expression<String>) expressions[0];
        if(i == 1) args = (Expression<Object>) expressions[1];
        return true;
    }
}
