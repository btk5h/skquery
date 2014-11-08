package com.w00tmast3r.skquery.elements.expressions;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.w00tmast3r.skquery.api.Patterns;
import com.w00tmast3r.skquery.util.Collect;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.HashMap;

@Patterns("%*classinfo% input")
public class ExprInput extends SimpleExpression<Object> {

    private static HashMap<Event, Object> in = new HashMap<>();
    private Class<?> returnType = Object.class;

    @Override
    protected Object[] get(Event e) {
        if (!in.containsKey(e)) return null;
        Object[] array = Collect.newArray(returnType, 1);
        array[0] = in.get(e);
        return array;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "input";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        returnType = ((Literal<ClassInfo>) exprs[0]).getSingle().getC();
        return true;
    }

    public static void setInput(Event e, Object o) {
        in.put(e, o);
    }

    public static void removeInput(Event e) {
        in.remove(e);
    }
}
